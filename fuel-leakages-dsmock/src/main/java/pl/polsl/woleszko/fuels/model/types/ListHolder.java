package pl.polsl.woleszko.fuels.model.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ListHolder {
	public static List<NozzleMeasuresEntity> nozzleEntities = new ArrayList<>();
	public static List<TankMeasuresEntity> tankEntities = new ArrayList<>();
	public static List<RefuelEntity> refuelEntities = new ArrayList<>();

	public void nozzleHandler(List<NozzleMeasuresEntity> body) {
		nozzleEntities = body;
		mqSender(NozzleMeasuresEntity.class, nozzleEntities);
	}

	public void tankHandler(List<TankMeasuresEntity> body) {
		tankEntities = body;
		mqSender(TankMeasuresEntity.class, tankEntities);
	}

	public void refuelHandler(List<RefuelEntity> body) {
		refuelEntities = body;
		mqSender(RefuelEntity.class, refuelEntities);
		
	}

	private <T extends Entity> void mqSender(Class<T> type, List<T> msgs) {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
					ActiveMQConnection.DEFAULT_BROKER_URL);
			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(type.getSimpleName());

			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			for(T msg : msgs) {
				ObjectMessage objMsg = session.createObjectMessage();
				objMsg.setObject(msg);				
				producer.send(objMsg);
			}
			
			session.close();
			connection.close();

			System.out.println("MessageSent");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
