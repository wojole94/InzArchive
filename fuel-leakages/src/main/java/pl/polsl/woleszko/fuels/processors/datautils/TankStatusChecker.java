package pl.polsl.woleszko.fuels.processors.datautils;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import pl.polsl.woleszko.fuels.model.utils.TankDataExtractor;

public class TankStatusChecker implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		Boolean status = TankDataExtractor.checkIDStatus();
		exchange.getIn().setHeader("tanks-ready", status);

	}

}
