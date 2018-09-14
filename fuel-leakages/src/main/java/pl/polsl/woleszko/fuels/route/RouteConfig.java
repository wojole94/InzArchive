package pl.polsl.woleszko.fuels.route;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import pl.polsl.woleszko.fuels.agreggators.ArrayListStrategy;
import pl.polsl.woleszko.fuels.agreggators.ByTankSplitter;
import pl.polsl.woleszko.fuels.model.utils.NozzleDataExtractor;
import pl.polsl.woleszko.fuels.model.utils.ResultHandler;
import pl.polsl.woleszko.fuels.model.utils.TankDataExtractor;
import pl.polsl.woleszko.fuels.processors.CVSumsProcessor;
import pl.polsl.woleszko.fuels.processors.DebugProcessor;
import pl.polsl.woleszko.fuels.processors.FilteringProcessor;
import pl.polsl.woleszko.fuels.processors.RefuelSumProcessor;
import pl.polsl.woleszko.fuels.processors.SalesProcessor;
import pl.polsl.woleszko.fuels.processors.TankVolumeProcessor;
import pl.polsl.woleszko.fuels.processors.TubeAnalyser;
import pl.polsl.woleszko.fuels.processors.TubeGenerator;
import pl.polsl.woleszko.fuels.processors.VarSumsProcessor;
import pl.polsl.woleszko.fuels.processors.datautils.DayHeaderProcessor;
import pl.polsl.woleszko.fuels.processors.datautils.DecapsuleProcessor;
import pl.polsl.woleszko.fuels.processors.datautils.StreamFormatter;
import pl.polsl.woleszko.fuels.processors.datautils.TankStatusChecker;
import pl.polsl.woleszko.fuels.route.utils.StatusChecker;

public class RouteConfig {

	
	private CamelContext mainContext = new DefaultCamelContext();
	private Double tolMin = 2D;
	private Double tolMax = 10D;
	private Double tolFactor = 4D;
	private Double tightLimit = 0.4;
	private Double leakLimit = 0.6;
	private Double threshold = 0.5;
	private Integer k = 3;
	private Integer window = 4; //okno uzywane przy filtracji
	private String outputFolder = "D://outputs";
	
	public RouteConfig() {
		configure();
	}
	

	public RouteConfig(Integer window, Double tolMin, Double tolMax, Double tolFactor, Double tightLimit, Double leakLimit,
			Double threshold, Integer k, String outputFolder) {
		super();
		this.tolMin = tolMin;
		this.tolMax = tolMax;
		this.tolFactor = tolFactor;
		this.tightLimit = tightLimit;
		this.leakLimit = leakLimit;
		this.threshold = threshold;
		this.k = k; //liczba elementow branych do regresji w generowaniu tub
		this.window = window;
		this.outputFolder = outputFolder;
		configure();
        ((Logger) LoggerFactory.getLogger("org.apache.camel")).setLevel(Level.INFO);
	}


	public void configure() {

		// log.debug("||--------------------||");
		// log.debug("||---Configuring...---||");
		// log.debug("||--------------------||");

		// create a Main instance
		// ListHolder holderBean = new ListHolder();

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
				ActiveMQConnection.DEFAULT_BROKER_URL);
		mainContext.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));


		// add routes
		try {
			mainContext.addRoutes(new RouteBuilder() {

				@Override
				public void configure() throws Exception {

					DebugProcessor debugProcessor = new DebugProcessor();
					DecapsuleProcessor decapProc = new DecapsuleProcessor();
					SalesProcessor salesProcessor = new SalesProcessor();
					TankVolumeProcessor tankVolumeProcessor = new TankVolumeProcessor();
					DayHeaderProcessor dayHeaderProc = new DayHeaderProcessor();
					RefuelSumProcessor refuelSumProcessor = new RefuelSumProcessor();
					TankStatusChecker tankStatusChecker = new TankStatusChecker();
					VarSumsProcessor varSummingProcessor = new VarSumsProcessor();
					CVSumsProcessor cvSummingProcessor = new CVSumsProcessor();
					StreamFormatter streamFormat = new StreamFormatter();
					FilteringProcessor filter = new FilteringProcessor(window);
					TubeGenerator tubeGenerator = new TubeGenerator(tolMin,tolMax,tolFactor,k);
					TubeAnalyser tubeAnalysis = new TubeAnalyser(threshold,tightLimit,leakLimit);
					StatusChecker checker = StatusChecker.getInstance();
					ResultHandler results  = ResultHandler.getInstance();

					
					from("test-jms:queue:NozzleMeasuresEntity")
						.process(decapProc)	
					//USTAWIC ZWROTNICE w ZALEZNOSCI OD PODZIALU
						.process(dayHeaderProc)	

//					//--ROZWIAZANIE BIZNESOWE---------------	
//					//.aggregate(header("date-id"), new ArrayListStrategy())
//					//.completionTimeout(<czas_w_ms>).ignoreInvalidCorrelationKeys()
//					//--------------------------------------

//					//--ROZWIAZANIE SYMULACYJNE---------------
						.aggregate(header("date-id"), new ArrayListStrategy())	
							//.completionSize(17268).ignoreInvalidCorrelationKeys()
							.completionTimeout(10000).ignoreInvalidCorrelationKeys()	
						
//					//----------------------------------------
						.bean(NozzleDataExtractor.class, "loadNozzlesAssign")
						.process(salesProcessor)
						.to("direct:sintesis");

					from("test-jms:queue:TankMeasuresEntity")
						.process(decapProc)	
					//USTAWIC ZWROTNICE w ZALEZNOSCI OD PODZIALU
						.process(dayHeaderProc)
					
//					//--ROZWIAZANIE BIZNESOWE---------------	
//					//.aggregate(header("date-id"), new ArrayListStrategy())
//					//.completionTimeout(<czas_w_ms>).ignoreInvalidCorrelationKeys()
//					//--------------------------------------
					
					//--ROZWIAZANIE SYMULACYJNE---------------
						.aggregate(header("date-id"), new ArrayListStrategy())
							//.completionSize(1152).ignoreInvalidCorrelationKeys()
							.completionTimeout(10000).ignoreInvalidCorrelationKeys()	
				    //----------------------------------------
						.bean(TankDataExtractor.class, "loadTanksIndexes")
						
						.process(tankVolumeProcessor)	
						.to("direct:sintesis");

					from("test-jms:queue:RefuelEntity")
						.process(decapProc)
						.process(dayHeaderProc)					
						.aggregate(header("date-id"), new ArrayListStrategy())
							.completionTimeout(10000).ignoreInvalidCorrelationKeys()	
						.to("direct:getStatus");
							
						from("direct:getStatus")
						.process(tankStatusChecker)
						.choice()
							.when(header("tanks-ready").isEqualTo(false)).to("direct:wait")
							.when(header("tanks-ready").isEqualTo(true)).to("direct:go");
						
						from("direct:wait")
						.delay(1000)
						.to("direct:getStatus");
						
						from("direct:go")
						.process(refuelSumProcessor)
						.to("direct:sintesis");
					
//====================== 1b. KOMPLETOWANIE I WYZNACZENIE VARÓW =====================					
					from("direct:sintesis")
						.aggregate(header("date-id"), new ArrayListStrategy())
						//TYLKO DO SYMULACJI (tutaj trzeba ustawic zwyczajnie z koncem dnia...)
							.completionTimeout(30000).ignoreInvalidCorrelationKeys()	
						//-----------------------------------------------------------
							.to("direct:varSumming");
					
					from("direct:varSumming")
						.bean(checker, "dataAqusitionDoneStatus")
						.delay(1000)
						.process(varSummingProcessor)
						.split().method(ByTankSplitter.class, "split")
						.to("direct:varBuffer");
					
//======================= 2. FILTRACJA VARÓW =======================================	
					from("direct:varBuffer")
					
					.aggregate(header("tank-id"), new ArrayListStrategy())
					//completionTimeout tylko dla potrzeb symulacji. w rozwiazaniu koncowym z ca³ego miesiaca
					.completionTimeout(30000).ignoreInvalidCorrelationKeys()
					.process(debugProcessor)
					.setHeader("data-type", constant("varSums"))
					.multicast()
					.to("direct:varFilter", "direct:array2File");

					from("direct:varFilter")
						.process(filter)
					.multicast()
					.to("direct:array2File", "direct:cvSumming");
//======================= 3. ZLICZANIE CVÓW =======================================					
					from("direct:cvSumming")
					.bean(checker, "varSummingDoneStatus")
					.delay(1000)
					.process(cvSummingProcessor)
					.multicast()
					.to("direct:cvFilter", "direct:array2File");
				
//======================= 4. FILTRACJA CVÓW =======================================						
					from("direct:cvFilter")
					.process(filter)
					.multicast()
					.to("direct:tubeGen", "direct:array2File");

//======================= 5. WYZNACZENIE TUB =======================================						
					from("direct:tubeGen")
					.bean(checker, "cvSummingDoneStatus")
					.delay(1000)
						.process(tubeGenerator)
						.multicast()
					.to("direct:tubeAnalysys", "direct:array2File");
					
//======================= 6. ANALIZA ===============================================
					from("direct:tubeAnalysys")
					.bean(checker, "tubeGenerationDoneStatus")
					.delay(1000)
					.process(tubeAnalysis)
					.process(debugProcessor)
					.bean(checker, "doneStatus")
					.delay(1000)
					.bean(results,"resultsGroupper")
					.bean(checker, "resetStatus")
					.to("mock:foo");
					
					from("direct:array2File")
						.process(streamFormat)
					.to("file://"+outputFolder+"?fileName=${header.data-type}_tank${header.tank-id}_${header.date-first}-${header.date-last}.csv");
				}

			});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Starting Camel.\n");
		try {

			mainContext.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() throws Exception {
		mainContext.stop();
	}

}
