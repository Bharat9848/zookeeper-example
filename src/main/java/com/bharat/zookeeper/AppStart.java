package com.bharat.zookeeper;

import com.bharat.zookeeper.config.AppConfig;
import com.bharat.zookeeper.config.Registry;
import com.bharat.zookeeper.simple.data.SimpleDataStore;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static java.lang.String.format;

public class AppStart {
    private static final Logger LOG = LoggerFactory.getLogger(AppStart.class);
    private static Registry registry;
    public static void main(String[] args) throws Exception {
        LOG.info("app start");
        AppConfig appConfig = new AppConfig();
        appConfig.loadProperties();
        registry = new Registry(appConfig);

        readFromSystemIn();
        LOG.info("end of app");
    }

        public static void readFromSystemIn() {
            Scanner scanner = new Scanner(System.in);
            SimpleDataStore dataStore = new SimpleDataStore(registry.getClient());
            String help = "help: use following commands \n "+
                    "add path value \n" +
                    "get path \n" +
                    "watch path \n" +
                    "set path value \n";
            while (true) {
                try {
                    String command = scanner.nextLine();
                    String[] x = command.split(" ");
                    if ("add".equalsIgnoreCase(x[0]) && validateAdd(x)) {
                        String path = x[1];
                        String value = x[2];
                        if(!dataStore.exists(path)){
                            dataStore.addData(path, value);
                        }
                        System.out.println(format("New node on %s created", path));
                    } else if ("get".equals(x[0]) && validateGet(x)) {
                        String path = x[1];
                        if(!dataStore.exists(path)){
                            System.out.println(format("path %s does not exist", path));
                        }else {
                            byte[] value = dataStore.getData(path);
                            System.out.println(format("value at path %s is %s", path, new String(value)));
                        }
                    } else if("watch".equals(x[0]) && validateWatch(x)){
                       String path = x[1];
                        Watcher listener = event -> {
                            System.out.println(format("received event %s", event));
                            if(event.getPath().equals(path)){
                                try {
                                    System.out.println(format("value at path %s is %s", path, new String (dataStore.getData(path))));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        dataStore.registerWatcher(path, listener);
                    }else if("set".equals(x[0]) && validateSet(x)){
                        String path = x[1];
                        String newValue = x[2];
                        if(!dataStore.exists(path)){
                            System.out.println(format("path %s does not exist", path));
                        }else{
                            dataStore.update(path, newValue);
                        }
                    } else if ("quit".equals(x[0])) {
                        scanner.close();
                        break;
                    } else {
                        System.out.println(help);
                    }
                } catch (Exception e) {
                    System.out.println(help);
                }

            }
            scanner.close();
        }

    private static boolean validateSet(String[] x) {
        return x.length >= 3 && x[1].startsWith("/");
    }

    private static boolean validateWatch(String[] x) {
        return x.length == 2 && x[1].startsWith("/");
    }

    private static boolean validateAdd(String[] x) {
        return x.length >= 3 && x[1].startsWith("/");
    }

    private static boolean validateGet(String[] x) {
        return x.length == 2 && x[1].startsWith("/");
    }


}
