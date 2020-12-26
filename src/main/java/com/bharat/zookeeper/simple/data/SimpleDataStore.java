package com.bharat.zookeeper.simple.data;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleDataStore {

    private final CuratorFramework client;
    private final ExecutorService executorSerice;

    public SimpleDataStore(CuratorFramework client){
        this.executorSerice = Executors.newSingleThreadExecutor();
        this.client = client;
    }
    public void addData(String path, String value) throws Exception {
        client.create().forPath(path, value.getBytes());
    }

    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }

    public boolean exists(String path) throws Exception {
        return client.checkExists().forPath(path) != null;
    }

    public void registerWatcher(String path, Watcher listener) throws Exception {
        client.getData().usingWatcher(listener).forPath(path);
    }

    public void update(String path, String newValue) throws Exception {
        client.setData().forPath(path, newValue.getBytes());
    }
}
