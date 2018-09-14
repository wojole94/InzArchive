package pl.polsl.woleszko.fuels.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class FilteringProcessor implements Processor {
	//static final Integer windowSize = 4; // w przysz³oœci pobierane z headera
	private Integer windowSize;
	
	
	public FilteringProcessor(Integer windowSize) {
		super();
		this.windowSize = windowSize;
	}


	@Override
	public void process(Exchange exchange) throws Exception {
		List<Double> originalList = (List<Double>) exchange.getIn().getBody();
		List<Double> resultList = new ArrayList<>();
				
		//AugMed 
		for (int i = 0; i < originalList.size(); i++) {
			DescriptiveStatistics window = new DescriptiveStatistics();
			for (int j = i - windowSize; j < i + windowSize; j++) {
				if (j < 0)
					window.addValue(originalList.get(0));
				else if (j >= originalList.size())
					window.addValue(originalList.get(originalList.size() - 1));
				else
					window.addValue(originalList.get(j));
			}
			Double mean = window.getMean();
			window.addValue(mean);
			Double median = window.getPercentile(50);
			resultList.add(i, median);
		}
		
		exchange.getIn().setBody(resultList);
		String header = (String) exchange.getIn().getHeader("data-type");
		header = header.concat("-filtered");
		exchange.getIn().setHeader("data-type", header);
	}

}
