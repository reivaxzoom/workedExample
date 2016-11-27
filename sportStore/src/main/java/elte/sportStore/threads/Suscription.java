package elte.sportStore.threads;

import elte.sportStore.view.Launcher;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.qpid.client.AMQAnyDestination;

/**
 *
 * @author Xavier
 */
public class Suscription {
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Suscription.class.getName());

    public void setUpQueue() {

		try {
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream(
					"message.properties"));
			Context context = new InitialContext(properties);

			ConnectionFactory connectionFactory = (ConnectionFactory) context
					.lookup("localConnectionFactory");
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(true,
					Session.SESSION_TRANSACTED);
			Topic requestTopic = session.createTopic("requestTopic");

			// MessageConsumer subscriber2 =
			// session.createDurableSubscriber(requestTopic, "addidas",
			// "category='shoes' OR category='sport' OR category= 'test'  ",true);
			// MessageConsumer subscriber1 =
			// session.createDurableSubscriber(requestTopic, "bestBuy",
			// "category='computer' OR category='software' OR category='hardware' OR category= 'test'",true);
			// MessageConsumer subscriber3 =
			// session.createDurableSubscriber(requestTopic, "wallmart",
			// "category='cleaners' OR category='personal care' OR category='meat' OR category= 'beverages' OR category= 'test' ",true);
			// MessageConsumer subscriber4 =
			// session.createDurableSubscriber(requestTopic, "supermarket");

			Destination responseQueue = null;
			responseQueue = new AMQAnyDestination(
					properties.get("queue.responseQueue")
							+ "; {create: always}");
			Session session1 = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			session1.createProducer(responseQueue);
			session.close();

		} catch (JMSException | IOException | NamingException
				| URISyntaxException ex) {
			Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
    
    
	public static void suscribeTopic(String topicName, String selector) {

		Session session = null;
		Connection connection = null;
		try {
			Properties properties = new Properties();
			properties.load(Suscription.class.getClassLoader().getResourceAsStream(
					"message.properties"));
			Context context = new InitialContext(properties);

			ConnectionFactory connectionFactory = (ConnectionFactory) context
					.lookup("localConnectionFactory");
			connection = connectionFactory.createConnection();
			connection.start();

			session = connection
					.createSession(true, Session.SESSION_TRANSACTED);
			Topic requestTopic = session.createTopic("requestTopic");
			// MessageConsumer subscriber1 =
			// session.createDurableSubscriber(requestTopic, "addidas");
			MessageConsumer subscriber1 = session.createDurableSubscriber(
					requestTopic, topicName, selector, true);
			Message message;

			int receivedMsg = 0;
			while ((message = (Message) subscriber1.receive(3000)) != null) {
				System.out.println("\n------------- Msg -------------");
				System.out.println(message);
				System.out.println("-------------------------------\n");
				receivedMsg = receivedMsg + 1;
			}

			session.commit();

			// session.unsubscribe("tesco");
			session.close();
			connection.close();
			// Assert.assertEquals(numberSampleMessages,receivedMsg);

		} catch (JMSException ex) {
			Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException | NamingException ex) {
			Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
    
    
    
    // UNSUSCRIBE TOPICS FROM QPID
	 public void unsuscribe() {
	 try {
	 Properties properties = new Properties();
	 properties.load(this.getClass().getClassLoader().getResourceAsStream("message.properties"));
	 Context context = new InitialContext(properties);
	
	 ConnectionFactory connectionFactory = (ConnectionFactory)
	 context.lookup("localConnectionFactory");
	 Connection connection = connectionFactory.createConnection();
	 connection.start();
	
	 Session session = connection.createSession(true,
	 Session.SESSION_TRANSACTED);
	 session.unsubscribe("tesco");
	 session.unsubscribe("auchan");
	 session.unsubscribe("supermaxi");
	 // session.("responseQueue");
	 session.close();
	 connection.close();
	 } catch (JMSException ex) {
	 Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
	 } catch (IOException ex) {
	 Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
	 } catch (NamingException ex) {
	 Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
	 }
	
	 }
    
    
    
    
//    public static void suscribeTopic(String topicName, String selector ) {
//        Session session = null;
//        Connection connection = null;
//        try {
//            Properties properties = new Properties();
//            properties.load(Suscription.class.getClassLoader().getResourceAsStream("message.properties"));
//            Context context = new InitialContext(properties);
//
//            javax.jms.ConnectionFactory connectionFactory = (javax.jms.ConnectionFactory) context.lookup("localConnectionFactory");
//            connection = connectionFactory.createConnection();
//            connection.start();
//
//            session = connection.createSession(true, Session.SESSION_TRANSACTED);
//            Topic requestTopic = session.createTopic("requestTopic");
////            MessageConsumer subscriber1 = session.createDurableSubscriber(requestTopic, "suscribeTopic");
//            MessageConsumer subscriber1 = session.createDurableSubscriber(requestTopic, topicName, selector,true);
//            Message message;
//
//            int receivedMsg = 0;
//            while ((message = (Message) subscriber1.receive(3000)) != null) {
//                System.out.println("\n------------- Msg -------------");
//                System.out.println(message);
//                System.out.println("-------------------------------\n");
//                receivedMsg = receivedMsg + 1;
//            }
//
//            session.commit();
//            session.close();
//            connection.close();
//        } catch (JMSException ex) {
//            Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException | NamingException ex) {
//            Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
 
    
    
    public static void suscribeReplier(String queue)   {
        MessageProducer producer = null;
        Session session = null;
        Connection connection = null;
        log.info("Preparing responses");
        Context context = null;
        try {
         Properties properties = new Properties();
            properties.load(Suscription.class.getClassLoader().getResourceAsStream("message.properties"));
            context = new InitialContext(properties);

            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("localConnectionFactory");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
            
            Destination respQueue = new AMQAnyDestination(queue +"; {create: always}");
            producer = session.createProducer(respQueue);

            log.info("Connected to queue " + queue);
//                TextMessage m = session.createTextMessage("test");
//                producer.send((javax.jms.Message) m);
        } catch (JMSException | IOException | NamingException |URISyntaxException ex) {
            Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
            connection.close();
            session.close();
            producer.close();
            context.close();
            } catch (NamingException | JMSException ex) {
                Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void unsuscriberTopic(String destination) {
        Session session = null;
        Connection connection = null;
        try {
            Properties properties = new Properties();
            properties.load(Suscription.class.getClassLoader().getResourceAsStream("message.properties"));
            Context context = new InitialContext(properties);

            javax.jms.ConnectionFactory connectionFactory = (javax.jms.ConnectionFactory) context.lookup("localConnectionFactory");
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(true, Session.SESSION_TRANSACTED);
           session.unsubscribe(destination);       
            session.commit();
            session.close();
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NamingException ex) {
            Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    
}
