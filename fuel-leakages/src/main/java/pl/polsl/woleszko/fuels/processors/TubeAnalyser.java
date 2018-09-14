package pl.polsl.woleszko.fuels.processors;

import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import pl.polsl.woleszko.fuels.model.types.AnalysisResult;
import pl.polsl.woleszko.fuels.model.types.Tube;

public class TubeAnalyser implements Processor {
	Double tubeTreshold;
	Double TL;
	Double LL;

	public TubeAnalyser(Double tubeTreshold, Double TL, Double LL) {
		this.LL = LL;
		this.TL = TL;
		this.tubeTreshold = tubeTreshold;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		List<Tube> tubesList = (List<Tube>) exchange.getIn().getBody();

		Double LDR = 0D;
		Double leakRatio = 0D;
		String decision = "internal error";

		Double totalLength = 0D;

		for (Tube tube : tubesList) {
			if (tube.getSlope() < -tubeTreshold)
				LDR += tube.getLength();
			totalLength += tube.getLength();
		}

		LDR /= totalLength;

		if (LDR <= TL)
			decision = "tight";
		if (LDR > TL && LL > LDR)
			decision = "inconclusive";
		if (LDR >= LL) {
			decision = "leak";
			Double dropTubesLength = LDR;
			for (Tube tube : tubesList) {
				if (tube.getSlope() < -tubeTreshold) {
					leakRatio += -tube.getSlope() * tube.getLength();
					dropTubesLength += tube.getLength();
				}
			}
			
			leakRatio /= dropTubesLength;
		}
		
		AnalysisResult result = new AnalysisResult(
				(String) exchange.getIn().getHeader("date-first"), 
				(String) exchange.getIn().getHeader("date-last"), 
				decision, 
				leakRatio,
				(Integer) exchange.getIn().getHeader("tank-id"));
		
		exchange.getIn().setBody(result);

	}

}
