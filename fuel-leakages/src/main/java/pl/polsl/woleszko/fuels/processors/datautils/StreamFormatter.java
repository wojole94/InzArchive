package pl.polsl.woleszko.fuels.processors.datautils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class StreamFormatter implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		List<Object> inputList = (List<Object>) exchange.getIn().getBody();
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < inputList.size(); i++) {

			Integer x = i + 1;
			String saveString = x + ";" + inputList.get(i).toString() + "\r\n";
			builder.append(saveString);
		}

		InputStream stream = new ByteArrayInputStream(builder.toString().getBytes());
		exchange.getIn().setBody(stream);
	}

}
