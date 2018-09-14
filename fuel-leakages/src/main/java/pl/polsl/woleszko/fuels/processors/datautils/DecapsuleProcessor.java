package pl.polsl.woleszko.fuels.processors.datautils;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jms.JmsMessage;

import pl.polsl.woleszko.fuels.model.types.Entity;

public class DecapsuleProcessor implements Processor{

	@Override
	public void process(Exchange arg0) throws Exception {
		// TODO Auto-generated method stub
		JmsMessage objmsg = (JmsMessage) arg0.getIn();
		ActiveMQObjectMessage mqMessage = (ActiveMQObjectMessage) objmsg.getJmsMessage();
		
		Entity entity = (Entity) mqMessage.getObject();
		arg0.getIn().setHeader("valid-data", true);
		arg0.getIn().setBody(entity);
	}
	
}
