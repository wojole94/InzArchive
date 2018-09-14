package pl.polsl.woleszko.fuels.processors.datautils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import pl.polsl.woleszko.fuels.model.types.Tube;

public class Tubes2File implements Processor{
	


	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<Tube> inputList = (List<Tube>) exchange.getIn().getBody();
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
