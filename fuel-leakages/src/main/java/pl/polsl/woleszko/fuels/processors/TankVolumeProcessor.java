package pl.polsl.woleszko.fuels.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import pl.polsl.woleszko.fuels.model.types.Entity;
import pl.polsl.woleszko.fuels.model.types.TankMeasuresEntity;

public class TankVolumeProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		//Map<Integer, Double> checkValues = new HashMap<>();
		List<TankMeasuresEntity> list = (List<TankMeasuresEntity>) exchange.getIn().getBody();

		Map<Integer, List<TankMeasuresEntity>> splitedByTankID = list.stream()
				.collect(Collectors.groupingBy(TankMeasuresEntity::getTankId));

		Map<Integer, Double> volumeValues = splitedByTankID.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
					TankMeasuresEntity startValue = entry.getValue().stream().min(Entity::compareTo).get();
					TankMeasuresEntity endValue = entry.getValue().stream().max(Entity::compareTo).get();
					Double results = endValue.getFuelVol() - startValue.getFuelVol();
					//checkValues.put(entry.getKey(),endValue.getFuelVol());
					return results;
				}));
		
		//exchange.getIn().setHeader("check-values", checkValues);
		exchange.getIn().setBody(volumeValues);

		
	}

}
