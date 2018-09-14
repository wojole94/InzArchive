package pl.polsl.woleszko.fuels.model.types;

import java.util.ArrayList;
import java.util.List;


public class ListHolder {
	public static List<NozzleMeasuresEntity> nozzleEntities = new ArrayList<>();
	public static List<TankMeasuresEntity> tankEntities = new ArrayList<>();
	public static List<RefuelEntity> refuelEntities = new ArrayList<>();
	
	public void nozzleHandler(NozzleMeasuresEntity body) {
		nozzleEntities.add(body);
	}
	
}
