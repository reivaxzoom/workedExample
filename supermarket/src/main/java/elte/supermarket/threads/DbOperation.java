package elte.supermarket.threads;

import elte.supermarket.singleBussiness.StoreOperations;
import elte.supermarket.singleBussiness.StoreOperationsImpl;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;


public class DbOperation {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DbOperation.class.getName());
    private static StoreOperations stOps;
    
    public static void regenerateDb() {
        stOps = new StoreOperationsImpl();
        log.info("removing database");
        stOps.removeAllListItems();
        log.info("populating database");
        stOps.createSampleItems();
        stOps.findAllItems().stream().forEach(System.out::println);
    }

    public static void displayrConf() {
      String path="";
        try {
            log.info("Displaing configuration");
        File file = new File("resources");
        URL[] urls = {file.toURI().toURL()};
        path=urls[0].getFile();
        ClassLoader loader = new URLClassLoader(urls);
        
        ResourceBundle bundle = ResourceBundle.getBundle("message", Locale.getDefault(), loader);
        System.out.println("qpid properties");
        System.out.println("topic.requestTopic="+bundle.getString("topic.requestTopic"));
        System.out.println("queue.responseQueue="+bundle.getString("queue.responseQueue"));
        System.out.println("connectionfactory.localConnectionFactory="+bundle.getString("connectionfactory.localConnectionFactory"));
        System.out.println("jms properties");
        System.out.println("SPORTSTOPICNAME="+bundle.getString("SPORTSELECTION"));
        System.out.println("SPORTSTOPICNAME="+bundle.getString("SPORTSTOPICNAME"));
        
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("looking at: "+path);
        }
    }
}
