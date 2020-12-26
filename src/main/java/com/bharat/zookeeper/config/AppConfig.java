package com.bharat.zookeeper.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;

public class AppConfig {

    private Properties properties;

    public void loadProperties() throws IOException {
       properties = new Properties();
       properties.load(ClassLoader.getSystemResourceAsStream("application.properties"));
    }
    private String ZookeeperConnectString = "zookeeper.server.url";
    private String ZookeeperClientConnectionTimeout = "zookeeper.client.connection.timeout";
    private String ZookeeperClientSessionTimeout = "zookeeper.client.session.timeout";

    String getZkConnectString() {
        return properties.getProperty(ZookeeperConnectString, "127.0.0.1:2181");
    }

    int getZkClientSessionTimeOut() {
        return Integer.parseInt(properties.getProperty(ZookeeperClientConnectionTimeout, "2000"));
    }

    int getZkClientConnectionTimout() {
        return Integer.parseInt(properties.getProperty(ZookeeperClientSessionTimeout, "2000"));
    }
}
