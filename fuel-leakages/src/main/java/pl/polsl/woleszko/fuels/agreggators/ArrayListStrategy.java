package pl.polsl.woleszko.fuels.agreggators;

import java.util.ArrayList;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import pl.polsl.woleszko.fuels.model.types.Entity;

public class ArrayListStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Object newBody = (Object) newExchange.getIn().getBody();
		Date date = (Date) newExchange.getIn().getHeader("date-id");
		Integer year = date.getYear()+1900; 
		String stringDate = date.getHours()+".00_"+ date.getDate() + "_" + date.getMonth()+1 + "_" +year;
		ArrayList<Object> list = null;
		if (oldExchange == null) {
			list = new ArrayList<Object>();
			list.add(newBody);
			newExchange.getIn().setBody(list);
			newExchange.getIn().setHeader("list-size", list.size());
			newExchange.getIn().setHeader("date-first", stringDate);
			return newExchange;
		} else {
			list = oldExchange.getIn().getBody(ArrayList.class);
			list.add(newBody);
			oldExchange.getIn().setHeader("list-size", list.size());
			oldExchange.getIn().setHeader("date-last", stringDate);
			return oldExchange;
		}
	}

}
