package elte.supermarket.threads;

import elte.client.model.Item;
import elte.supermarket.model.RequestData;
import elte.client.model.ShoppingCart;
import elte.supermarket.singleBussiness.StoreOperations;
import elte.supermarket.singleBussiness.StoreOperationsImpl;
import elte.supermarket.view.Launcher;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.qpid.client.message.JMSObjectMessage;

/**
 *
 * @author Xavier
 */
public class Processor  {
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Processor.class.getName());

    private RequestData reqData ;
    private  StoreOperations stOps;
    private final String selector;
    
     private final BlockingQueue<RequestData> queue;
    
    
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    
     public Processor(BlockingQueue<RequestData> queue, String selector) {
        this.queue = queue;
        this.selector=selector;
    }
    
    
     public void consumeAll() {

        Session session = null;
        Connection connection = null;
        try {
            log.info("connecting qpid ");
            Properties properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream("message.properties"));
            Context context = new InitialContext(properties);

            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("localConnectionFactory");
            
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            
             Topic destination = (Topic) context.lookup(Launcher.reqTopicName);
             MessageConsumer subscriber1 = session.createDurableSubscriber(destination,Launcher.topicName, this.selector,false);
             
            
//            Topic requestTopic = session.createTopic(Launcher.reqTopicName);
//            MessageConsumer subscriber1 = session.createDurableSubscriber(requestTopic, Launcher.topicName, Launcher.addidasSelector,true);
             
            
            log.info("Connected to topic "+Launcher.topicName);
            log.info("JMS Selector: "+selector);

            stOps = new StoreOperationsImpl();
            ObjectMessage message;
            context.close();
            long timeout = 5000;
            int minimum=2;
            int maximum=3;
            while ((message = (ObjectMessage) subscriber1.receive(timeout)) != null) {
                JMSObjectMessage objMes = (JMSObjectMessage) message;
                 log.info("\n--- Message to be processed -------------\n");
                log.info(objMes);
                 reqData = new RequestData();
                reqData.setDate(new Date(objMes.getStringProperty("date")));
                reqData.setId(objMes.getStringProperty("id"));
                reqData.setAddress(objMes.getStringProperty("address"));
                reqData.setComments(objMes.getStringProperty("comments"));
                reqData.setPhone(objMes.getStringProperty("phone"));
                reqData.setExpDelivery(new Date(objMes.getStringProperty("expDelivery")));
                reqData.setCountry(objMes.getStringProperty("country"));
                reqData.setCategory(objMes.getStringProperty("category"));
                reqData.setDeliverAddress(objMes.getStringProperty("deliverAddress"));
                reqData.setBudget(Short.valueOf(objMes.getStringProperty("budget")));
                reqData.setSubOrd(objMes.getStringProperty("subOrd"));
                reqData.setItemNumber(((ShoppingCart) (objMes.getObject())).size());
                reqData.setItems(objMes.getObject());
                log.info("\n---end Message-------------------------\n");
            session.commit();
            
                ShoppingCart clientCart = (ShoppingCart) objMes.getObject();
                List<Item> resultList = stOps.checkAvailableItems(clientCart);
                
            //  setting random time for process
                int randomDuration = minimum + (int)(Math.random() * maximum); 
                Thread.sleep(randomDuration*1000);
                log.info("Message processed");
                reqData.setItems(resultList);
                queue.put(reqData);
                
//                sendMessages(resultCart);
//                receivedMsg = receivedMsg + 1;
            }
            log.info("Sending response done");
            subscriber1.close();
            session.close();
            connection.close();
            log.info("session closed");

        } catch (JMSException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NamingException | InterruptedException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
}
