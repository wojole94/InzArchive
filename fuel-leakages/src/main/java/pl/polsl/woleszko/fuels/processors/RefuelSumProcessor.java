package pl.polsl.woleszko.fuels.processors;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import pl.polsl.woleszko.fuels.model.types.RefuelEntity;
import pl.polsl.woleszko.fuels.model.utils.TankDataExtractor;

public class RefuelSumProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		List<RefuelEntity> list = (List<RefuelEntity>) exchange.getIn().getBody();
		// Map<Integer, List<RefuelEntity>> splitedByTankID =
		// list.stream().collect(Collectors.groupingBy(RefuelEntity::getTankId));

		Map<Integer, List<RefuelEntity>> splitedByTankID = new HashMap<>();
		for (Integer tankID : TankDataExtractor.getTankIndexes())
			splitedByTankID.put(tankID, new ArrayList<RefuelEntity>());

		//if (list.size() != 0) {
			for (RefuelEntity entity : list) {
				Integer tankID = entity.getTankId();
				List<RefuelEntity> entryList = splitedByTankID.get(tankID);
				entryList.add(entity);
			}
		//}

		Map<Integer, Double> totals = splitedByTankID.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
					DoubleSummaryStatistics sum = entry.getValue().stream()
							.collect(Collectors.summarizingDouble(entity -> entity.getFuelVol()));
					return sum.getSum();
				}));

		exchange.getIn().setBody(totals);
	}

}
