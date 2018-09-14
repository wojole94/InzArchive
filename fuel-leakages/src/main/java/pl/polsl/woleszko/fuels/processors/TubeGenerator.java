package pl.polsl.woleszko.fuels.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import pl.polsl.woleszko.fuels.model.types.Tube;

public class TubeGenerator implements Processor {

	Double tolMin;
	Double tolMax;
	Double tolFactor;
	Integer k;
	List<Double> periodValues;

	public TubeGenerator(Double tolMin, Double tolMax, Double tolFactor, Integer k) {
		this.tolMin = tolMin;
		this.tolMax = tolMax;
		this.tolFactor = tolFactor;
		this.k = k;
	}

	@Override
	public void process(Exchange exchange) throws Exception {

		periodValues = (List<Double>) exchange.getIn().getBody();
		List<Tube> tubeList = new ArrayList<>();

		// first TUBE generation
		SimpleRegression regression = new SimpleRegression();
		for (int i = 0; i < k; i++) {
			regression.addData(i, periodValues.get(i));
		}
		Tube currTube = new Tube(				 
				regression.getSlope(), 
				regression.getIntercept(),
				getTolerance(regression, 0, k));
		// ----------------------------------------------------

		for (int i = 0; i < periodValues.size(); i++) {
			if (currTube.isInTube(i, periodValues.get(i)))
				currTube.incrementLength();
			else {
				tubeList.add(currTube);
				regression.clear();
				for (int j = i; j < i+k && j < periodValues.size(); j++) {
					regression.addData(j, periodValues.get(j));
				}
				currTube = new Tube(						
						regression.getSlope(), 
						regression.getIntercept(),
						getTolerance(regression, i, i+k));
			}			
		}
		tubeList.add(currTube);
		exchange.getIn().setHeader("data-type", "tubes");
		exchange.getIn().setBody(tubeList);
		
		
	}

	private Double getTolerance(SimpleRegression regression, Integer a, Integer b) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		Double slope = regression.getSlope();
		Double intercept = regression.getIntercept();

		for (int x = a; x <= b && x < periodValues.size(); x++) {
			Double tubeS = x * slope + intercept;
			Double abs = Math.abs(periodValues.get(x) - tubeS);
			stats.addValue(abs);
		}

		Double tubeDev = stats.getMean();

		stats.clear();
		stats.addValue(tubeDev * tolFactor);
		stats.addValue(tolMin);
		stats.addValue(tolMax);

		Double tol = stats.getPercentile(50);

		return tol;
	}

}
