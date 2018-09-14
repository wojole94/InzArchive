package pl.polsl.woleszko.fuels.processors;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mortbay.log.Log;

import pl.polsl.woleszko.fuels.main.Main;

public class DebugProcessor implements Processor{
	String text;
	
	public DebugProcessor(){
		
	}
	
	public DebugProcessor(String string){
		this.text = string;
	}
	@Override
	public void process(Exchange arg0) throws Exception {
		// TODO Auto-generated method stub
		
//		JmsMessage objmsg = (JmsMessage) arg0.getIn();
//		ActiveMQObjectMessage mqMessage = (ActiveMQObjectMessage) objmsg.getJmsMessage();
//		NozzleMeasuresEntity entity = (NozzleMeasuresEntity) mqMessage.getObject();
//		arg0.getIn().setBody(entity);
		Object body = arg0.getIn().getBody();
		Log.info("arg 0 value = {}", body);	
	//	List<Long> ids = Main.dateIds.stream().sorted().collect(Collectors.toList());
		
//		Map<Integer, Double> header = (Map<Integer,Double>) arg0.getIn().getHeader("check-values");
//		Log.info("arg 0 value = {}", arg0);
		Object hTankID = arg0.getIn().getHeader("tank-id");
		Object hDatetimeID = arg0.getIn().getHeader("date-id");
//		Log.info("header {}", hTankID);
		Log.info(text);	
	}

}
