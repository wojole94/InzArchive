package pl.polsl.woleszko.fuels.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CVSumsProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		List<Double> periodValues = (List<Double>) exchange.getIn().getBody();
		List<Double> cumulativeVars = new ArrayList<>();
		Double sum = new Double(0);
		for(int i=0; i<periodValues.size(); i++) {
			sum += periodValues.get(i);
			cumulativeVars.add(i, sum);
		}
		exchange.getIn().setBody(cumulativeVars);
		exchange.getIn().setHeader("data-type", "cvSums");
	}

}
