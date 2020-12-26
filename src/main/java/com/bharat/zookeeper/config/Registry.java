package com.bharat.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

public class Registry {
    private AppConfig config;

    public Registry(AppConfig config) {
        this.config = config;
    }

    private CuratorFramework client;

    public synchronized CuratorFramework getClient() {
        if(client == null){
            RetryPolicy a  = new RetryNTimes( 5,2000);
            CuratorFramework client = CuratorFrameworkFactory.newClient(config.getZkConnectString(), config.getZkClientSessionTimeOut(), config.getZkClientConnectionTimout(), a);
            client.start();
            this.client = client;
        }
        return client;
    }
}
