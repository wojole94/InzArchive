package pl.polsl.woleszko.fuels.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import pl.polsl.woleszko.fuels.model.types.NozzleMeasuresEntity;
import pl.polsl.woleszko.fuels.model.utils.NozzleDataExtractor;
import pl.polsl.woleszko.fuels.model.types.Entity;

public class SalesProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<NozzleMeasuresEntity> list = (List<NozzleMeasuresEntity>) exchange.getIn().getBody();
		
		
		Map<Integer, List<NozzleMeasuresEntity>> splitedByTankID = list.stream()
				.collect(Collectors.groupingBy(NozzleMeasuresEntity::getNozId));

		Map<Integer, Double> nozzTotals = splitedByTankID.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
					NozzleMeasuresEntity endEntity = entry.getValue().stream().max(Entity::compareTo).get();
					NozzleMeasuresEntity startEntity = entry.getValue().stream().min(Entity::compareTo).get();
					return endEntity.getTotalCounter() - startEntity.getTotalCounter();
				}));

		

		Double sum = 0D;
		Map<Integer, Double> tankSums = new HashMap<>();


		for (Integer nozz : nozzTotals.keySet()) {
			if (!tankSums.containsKey(NozzleDataExtractor.getNozzleAssign(nozz))) {
				tankSums.put(NozzleDataExtractor.getNozzleAssign(nozz), 0D);
			}
			sum = tankSums.get(NozzleDataExtractor.getNozzleAssign(nozz));
			sum += nozzTotals.get(nozz);
			tankSums.put(NozzleDataExtractor.getNozzleAssign(nozz), sum);
		}
		
		
		exchange.getIn().setBody(tankSums);
		
	}

	

}
