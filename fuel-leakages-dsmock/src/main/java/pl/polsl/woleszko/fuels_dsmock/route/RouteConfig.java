package pl.polsl.woleszko.fuels_dsmock.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

import pl.polsl.woleszko.fuels.model.types.ListHolder;
import pl.polsl.woleszko.fuels.model.types.NozzleMeasuresEntity;
import pl.polsl.woleszko.fuels.model.types.RefuelEntity;
import pl.polsl.woleszko.fuels.model.types.TankMeasuresEntity;
import pl.polsl.woleszko.fuels_dsmock.route.utils.BindyCsvDataFormat;


public class RouteConfig {
	private String directoryName = "C:\\Users\\Wojtek\\Desktop\\Projekt Inzynierski\\Dane\\Dane paliwowe na³o¿one wycieki\\Z wyciekami";
	private String fileNo = "7";
	private Main mainContext = new Main();
	
	public RouteConfig(String directoryName, String fileNo){
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
		 mainContext.bind("terminate", this);
		// mainContext.bind("terminate", this);

		// add routes
		mainContext.addRouteBuilder(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				BindyCsvDataFormat nozzleBindy = new BindyCsvDataFormat(NozzleMeasuresEntity.class);								
				BindyCsvDataFormat tankBindy = new BindyCsvDataFormat(TankMeasuresEntity.class);
				BindyCsvDataFormat refuelBindy = new BindyCsvDataFormat(RefuelEntity.class);
//				
				
//				from("file:"+directoryName+"?fileName=nozzleMeasures.log&consumer.delay=1000&noop=true&lazyLoad=1")
				from("file:"+directoryName+"?fileName=nozzleMeasures_"+fileNo+".txt&noop=true")		
					.unmarshal(nozzleBindy)
					.to("bean:entitiesHandler?method=nozzleHandler")
					.to("bean:terminate?method=close");
				
				
				from("file:"+directoryName+"?fileName=refuel_"+fileNo+".txt&noop=true")		
					.unmarshal(refuelBindy)
					.to("bean:entitiesHandler?method=refuelHandler");
				  //.to("bean:terminate?method=close");
				
				from("file:"+directoryName+"?fileName=tankMeasures_"+fileNo+".txt&noop=true")		
					.unmarshal(tankBindy)
					.to("bean:entitiesHandler?method=tankHandler");
				//.to("bean:terminate?method=close");
				
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
