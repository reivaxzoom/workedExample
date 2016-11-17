package elte.electronicStore.view;

import com.querydsl.core.BooleanBuilder;
import elte.electronicStore.threads.DbOperation;
import elte.electronicStore.model.QRequestData;
import elte.electronicStore.model.RequestData;
import elte.electronicStore.QueryPlayground.QueryGeneratorSample;
import elte.electronicStore.threads.Processor;
import elte.electronicStore.threads.Response;
import elte.electronicStore.threads.Suscription;
import elte.electronicStore.util.ConcurrentUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.log4j.Priority;

/**
 *
 * @author Xavier
 */
public class Launcher {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Launcher.class.getName());
    final static String[] options ={"reply","process", "suscr","susreply","unsuscrTopic","unsuscrQueue","show","size","dboperations","conf","shutdown","cancelProcess","cancelReplier","help","exit" };
    
    
    
    
    static ExecutorService executor = Executors.newFixedThreadPool(2);

//     public static final String addidasSelector = "rd.category = 'sport' OR rd.category = 'shoes' OR rd.category = 'test'";
//     public static final String addidasSelector = "category='shoes' OR category='sport' OR category= 'test'  ";
    public static final String reqTopicName = "requestTopic";
    public static final String topicName = "sportStore";
    public static final String respQueueName = "responseQueue";

    public static String expressionComposer() {
        QRequestData rd = QRequestData.requestData;
        QueryGeneratorSample qg = new QueryGeneratorSample();

//        Expression query=rd.category.eq("sport").or(rd.category.eq("soccer")).and(rd.country.eq("Hungary").or(rd.country.eq("France")).or(rd.country.eq("Germany"))).and(rd.itemNumber.goe(2));
//        return qg.genQuery(query);
        List<String> countries = Arrays.asList("Hungary", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "United Kingdom");
        List formatedCountries = countries.stream().map(x -> "'" + x + "'").collect(Collectors.toList());
        BooleanBuilder bb = new BooleanBuilder(rd.country.in(formatedCountries));
        bb.and(rd.category.eq("sport").or(rd.category.eq("gym")).or(rd.category.eq("shoes")).or(rd.category.eq("beverages")).or(rd.category.eq("futbol")));
        bb.and(rd.date.isNotNull());
        bb.and(rd.budget.between(10,600));
        bb.and(rd.phone.startsWith("36"));
        bb.and(rd.frecuent.isTrue());
        
        

//         Jms like is not able to check wheter a word belogs to an argument or not, like only checks if it start or ends. 
//         .comments.contains("party")
//         bb.and(rd.comments.contains("party").or(rd.comments.contains("game")).or(rd.comments.contains("outdoor")));
//         bb.and(rd.date)
        return qg.genQuery(bb);
    }
    public static BlockingQueue<RequestData> queue;

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        log.info("Starting program");

        queue = new ArrayBlockingQueue<>(100);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter some text, or '" + "exit" + "' to quit");
        Future fprocess = null;
        Future freply = null;

        while (true) {
            System.out.print("> ");
            String option = br.readLine();

            switch (option) {
                case "reply": {
                    log.info("starting procesor");
                    Response cons = new Response(queue);
                    Runnable consRun = () -> {
                        cons.startConsumer();
                    };
                    Thread consumer = new Thread(consRun);
                    consumer.setName("consumer");
                    freply = executor.submit(consumer);
                    log.info("queue size:" + queue.size() + " is done: " + freply.isDone());
                    break;
                }
                case "process": {
                    log.info("starting procesor");
                    Processor processor = new Processor(queue, expressionComposer());
                    Runnable procRun = () -> {
                        processor.consumeAll();
                    };
                    Thread process = new Thread(procRun);
                    process.setName("processor");
                    fprocess = executor.submit(process);
                    log.info("cart size:" + queue.size() + " is done: " + fprocess.isDone());
                    break;
                }
                case "suscr": {
                    log.info("Register topic: " + topicName);
                    log.info("JMS Selector: " + expressionComposer());
                    Suscription.suscribeTopic(topicName, expressionComposer());
                    break;
                }
                case "susreply": {
                    log.info("Register reply:" + respQueueName);
                    Suscription.suscribeReplier(respQueueName);
                    break;
                }
                case "unsuscrTopic": {
                    log.info("Unsuscribing topic: " + topicName);
                    Suscription.unsuscriberTopic(topicName);
                    break;
                }
                case "unsuscrQueue": {
                    log.info("Unsuscribing reply queue: " + respQueueName);
                    Suscription.unsuscriberTopic(respQueueName);
                    break;
                }
                case "show": {
                    log.info("showing queue");
                    queue.stream().forEach(System.out::println);
                    break;
                }
                case "size": {
                    log.info("showing size");
                    log.info("queue size:" + queue.size());
                    break;
                }
                case "dboperations": {
                    log.info("processing regenerationDb");
                    DbOperation.regenerateDb();
                    break;
                }
                case "conf": {
                    log.info("checking configuration");
                    DbOperation.displayrConf();
                    break;
                }
                case "shutdown": {
                    log.info("checking configuration");
                    checkProcess(fprocess, freply);
                    break;
                }
                case "cancelProcess":{
                    log.info("starting shutdown of process");
                     if ((fprocess == null) || fprocess.isDone() || fprocess.isCancelled()) {
                        System.out.println("process was not started or already finish");
                     }else{
                        try {
                            fprocess.get(4000, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException ex) {
                            log.info(fprocess.cancel(true)+" "+ex);
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException ex) {
                            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (TimeoutException ex) {
                            log.info(fprocess.cancel(true)+" "+ex);
//                            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     }
                    break;
                }
                case "cancelReplier":{
                    log.info("checking configuration");
                     if ((freply == null) || freply.isDone() || freply.isCancelled()) {
                        System.out.println("replier was not started or already finish");
                     }else{
                        log.info(fprocess.cancel(true));
                     }
                    break;
                }
                 case "help": {
                    System.out.println("-------Options-------------");
                    Arrays.asList(options).stream().forEach(System.out::println);
                    break;
                }
                case "exit": {
                    System.out.println("Exiting.");
                    System.exit(0);
                }
                default: {
                    log.warn("no valid option selected");
                }
            }
        }
    }

    private static void checkProcess(Future fprocess, Future freply)  {

        try {
            if ((fprocess == null) || (fprocess.isDone()) || (fprocess.isCancelled())) {
                // submit a task and return a Future
                System.out.println("process fprod didnt finish");
            }

            if ((freply == null) || (freply.isDone()) || (freply.isCancelled())) {
                System.out.println("process fprod didnt finish");
            }
            // if null the task has finished
            if (fprocess.get() == null) {
                System.out.println(") Productor terminated successfully");
            } else {
                // if it doesn't finished, cancel it
                fprocess.cancel(true);
            }
            if (freply.get() == null) {
                System.out.println(") TaskConsume terminated successfully");
            } else {
                freply.cancel(true);
            }
            ConcurrentUtils.stop(executor);
            System.out.println("Simulation complete");
         } catch (InterruptedException | ExecutionException ex) {
             log.log(Priority.FATAL,ex);
        }

    }
}
