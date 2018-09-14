package pl.polsl.woleszko.fuels.agreggators;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;

public class ByTankSplitter {
	public List split(Exchange exchange) {
		Map<Integer, Double> mapToSplit = (Map<Integer,Double>) exchange.getIn().getBody();
		List<Message> splittedMsg = new ArrayList<>();
		Date date = (Date) exchange.getIn().getHeader("date-id");
		for(Integer tankId : mapToSplit.keySet()) {
			Message msg = new DefaultMessage();
			msg.setBody(mapToSplit.get(tankId));
			msg.setHeader("tank-id", tankId);
			msg.setHeader("date-id", date);
			splittedMsg.add(msg);
		}
		return splittedMsg;
		
	}

}
