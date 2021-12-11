package com.wangfugui.kafka.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

/**
 * @author jieshao
 * @date 2021/11/11 11:13
 */
@Configuration
public class ZookeeperConfig {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

    @Value("${zookeeper.address}")
    private String connectString;

    @Value("${zookeeper.timeout}")
    private int sessionTimeout;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Bean(name = "zkClient")
    public ZooKeeper zkClient() {
        ZooKeeper zooKeeper = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    // 如果收到了服务端的响应事件，说明连接成功
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            logger.info("初始化ZooKeeper连接状态: {}", zooKeeper.getState());
        } catch (Exception e) {
            logger.error("初始化Zookeeper连接状态异常: {}", e.getMessage());
        }

        return zooKeeper;
    }
}