package pl.polsl.woleszko.fuels.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

import pl.polsl.woleszko.fuels.model.types.ListHolder;
import pl.polsl.woleszko.fuels.model.types.NozzleMeasuresEntity;
import pl.polsl.woleszko.fuels.route.utils.BindyCsvDataFormat;


public class RouteConfig {
	private String directoryName = "C:\\Users\\Wojtek\\Desktop\\Projekt Inzynierski\\Dane\\dane\\Zestaw 1\\";
	private Main mainContext = new Main();
	
	public RouteConfig(){
		configure();
	}
	
	
	public void configure() {

		// log.debug("||--------------------||");
		// log.debug("||---Configuring...---||");
		// log.debug("||--------------------||");

		// create a Main instance
		ListHolder holderBean =	new ListHolder(); 
		
		// bind MyBean into the registry
		 mainContext.bind("entitiesHandler", holderBean);
		// mainContext.bind("terminate", this);

		// add routes
		mainContext.addRouteBuilder(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				BindyCsvDataFormat nozzleBindy = new BindyCsvDataFormat(NozzleMeasuresEntity.class);					
//				
//				BindyCsvDataFormat tankBindy = new BindyCsvDataFormat(TankMeasuresEntity.class);
//				BindyCsvDataFormat refuelBindy = new BindyCsvDataFormat(RefuelEntity.class);
//				
				
//				from("file:"+directoryName+"?fileName=nozzleMeasures.log&consumer.delay=1000&noop=true&lazyLoad=1")
				from("file:"+directoryName+"?fileName=nozzleMeasures.log&noop=true")		
					.unmarshal(nozzleBindy)
					.to("bean:entitiesHandler?method=nozzleHandler");
				
				
			}

		});
		System.out.println("Starting Camel.\n");
		try {

			mainContext.run();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// log.debug("||------------------------||");
		// log.debug("||---Context started...---||");
		// log.debug("||------------------------||");
	}

	public void close() {
		mainContext.completed();
	}

}
