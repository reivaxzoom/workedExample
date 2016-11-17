package elte.supermarket.threads;

import elte.supermarket.view.Launcher;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
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

    public static void suscribeTopic(String topicName, String selector ) {
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
            Topic requestTopic = session.createTopic("requestTopic");
//            MessageConsumer subscriber1 = session.createDurableSubscriber(requestTopic, "suscribeTopic");
            MessageConsumer subscriber1 = session.createDurableSubscriber(requestTopic, topicName, selector,true);
            javax.jms.Message message;

            int receivedMsg = 0;
            while ((message = (javax.jms.Message) subscriber1.receive(3000)) != null) {
                System.out.println("\n------------- Msg -------------");
                System.out.println(message);
                System.out.println("-------------------------------\n");
                receivedMsg = receivedMsg + 1;
            }

            session.commit();
            session.close();
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NamingException ex) {
            Logger.getLogger(Suscription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    
    
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
