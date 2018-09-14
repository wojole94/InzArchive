package pl.polsl.woleszko.fuels.processors.datautils;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import pl.polsl.woleszko.fuels.main.Main;
import pl.polsl.woleszko.fuels.model.types.Entity;

public class DayHeaderProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		//Splitted by hours
		Entity body = (Entity) exchange.getIn().getBody();
		@SuppressWarnings("deprecation")
		Date date = new Date(
					body.getDate().getYear(),
					body.getDate().getMonth(),
					body.getDate().getDate(),
					body.getDate().getHours(),	0);

//		Long uniqueTimestamp = new Long(
//				body.getDate().getYear()
//				+ body.getDate().getMonth()*10 
//				+ body.getDate().getDate()*100
//				+ body.getDate().getHours()*1000);

	
		//if(!Main.dateIds.contains(uniqueTimestamp)) Main.dateIds.add(uniqueTimestamp);
		exchange.getIn().setHeader("date-id", date);

	}

}
