package pl.polsl.woleszko.fuels.model.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.polsl.woleszko.fuels.model.types.Entity;
import pl.polsl.woleszko.fuels.model.types.NozzleMeasuresEntity;

public class NozzleDataExtractor {
	private static Map<Integer, Integer> assigns = null;

	public static Integer getNozzleAssign(Integer nozzleID) {
		return assigns.get(nozzleID);
	}

	/**
	 * Loads the list which specify which nozzle correspond to which tank HashMap
	 * elems: 1. Long - nozzle 2. Long - tank
	 */

	public void loadNozzlesAssign(List<Entity> list) {
		
		if (assigns == null) {
			assigns = new HashMap<>();
			for (Entity entity0 : list) {
				NozzleMeasuresEntity entity = (NozzleMeasuresEntity) entity0;
				if (!assigns.containsKey(entity.getNozId())) {
					assigns.put(entity.getNozId(), entity.getTankId());
				}
				
			}
		}
	}
}
