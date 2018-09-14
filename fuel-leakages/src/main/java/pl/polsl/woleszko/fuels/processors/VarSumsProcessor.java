package pl.polsl.woleszko.fuels.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class VarSumsProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<Map<Integer, Double>> periodValues = (List<Map<Integer, Double>>) exchange.getIn().getBody();
		
		Map<Integer, Double> varsPerTank = new HashMap<>();
		
		for(Map<Integer,Double> valuesMap : periodValues) {
			for(Integer tankID : valuesMap.keySet()) {
				if(!varsPerTank.containsKey(tankID))
					varsPerTank.put(tankID, valuesMap.get(tankID));
				else {
					Double value = varsPerTank.get(tankID);
					value+=valuesMap.get(tankID);
					varsPerTank.put(tankID,value);
				}
			}
		}
		exchange.getIn().setHeader("data-type", "vars");
		exchange.getIn().setBody(varsPerTank);
	}

}
