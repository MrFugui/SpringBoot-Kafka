package com.wangfugui.kafka.service;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jieshao
 * @date 2021/11/11 12:00
 */
public class CustomWatcher implements Watcher {
    private static final Logger logger = LoggerFactory.getLogger(CustomWatcher.class);

    @Override
    public void process(WatchedEvent event) {
        logger.info("监听事件的状态: {}", event.getState());
        logger.info("监听事件的路径: {}", event.getPath());
        logger.info("监听事件的类型: {}", event.getType());
    }
}