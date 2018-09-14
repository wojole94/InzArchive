package pl.polsl.woleszko.fuels.model.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.polsl.woleszko.fuels.model.types.Entity;
import pl.polsl.woleszko.fuels.model.types.TankMeasuresEntity;

public class TankDataExtractor {
	private static Set<Integer> indexes = null;
	private static Boolean idStatus = false;

	public static Set<Integer> getTankIndexes() {
		return indexes;
	}
	
	public static boolean checkIDStatus() {
		return idStatus;
	}
	
	public static void loadTanksIndexes(List<Entity> list) {
		if (indexes == null) {
			indexes = new HashSet<>();
			for (Entity entity0 : list) {
				TankMeasuresEntity entity = (TankMeasuresEntity) entity0; 
				if (!indexes.contains(entity.getTankId())) {
					indexes.add(entity.getTankId());
				}
			}
			idStatus = true;
		}
	}
}
