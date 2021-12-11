package com.wangfugui.kafka.util;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jieshao
 * @date 2021/11/11 11:22
 */
@Component
public class ZkUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZkUtil.class);

    @Autowired
    private ZooKeeper zkClient;

    /**
     * 创建持久化节点(客户端断开连接后，节点数据持久化在磁盘上，不会被删除)
     *
     * @param path 路径
     * @param data 数据
     * @return
     */
    public boolean createPerNode(String path, String data) {
        try {
            /**
             * 参数1：要创建节点的路径
             * 参数2：节点数据
             * 参数3：节点权限
             * 参数4：节点类型
             */
            zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;
        } catch (Exception e) {
            logger.error("创建持久化节点异常，路径: {}, 数据: {}, 异常: {}", path, data, e);
            return false;
        }
    }

    /**
     * 创建临时节点(客户端断开连接后，节点将被删除)
     *
     * @param path 路径
     * @param data 数据
     * @return
     */
    public boolean createTmpNode(String path, String data) {
        try {
            /**
             * 参数1：要创建节点的路径
             * 参数2：节点数据
             * 参数3：节点权限
             * 参数4：节点类型
             */
            zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            return true;
        } catch (Exception e) {
            logger.error("创建临时节点异常，路径: {}, 数据: {}, 异常: {}", path, data, e);
            return false;
        }
    }

    /**
     * 创建自定义节点
     *
     * @param path       路径
     * @param data       数据
     * @param acl        节点权限
     * @param createMode 节点类型
     * @return
     */
    public boolean createNode(String path, String data, List<ACL> acl, CreateMode createMode) {
        try {
            /**
             * 参数1：要创建节点的路径
             * 参数2：节点数据
             * 参数3：节点权限
             * 参数4：节点类型
             */
            zkClient.create(path, data.getBytes(), acl, createMode);
            return true;
        } catch (Exception e) {
            logger.error("创建节点异常，路径: {}, 数据: {}, 异常: {}", path, data, e);
            return false;
        }
    }

    /**
     * 修改节点
     *
     * @param path 路径
     * @param data 数据
     * @return
     */
    public boolean updateNode(String path, String data) {
        try {
            /**
             * version：指定要更新数据的版本，如果 version 和真实的版本不同，更新操作将失败，指定 version 为 -1 则忽略版本检查
             * zk 的数据版本是从 0 开始计数的，如果客户端传入的是 -1，则表示 zk 服务器需要基于最新的数据进行更新。如果对 zk 的数据节点的更新操作没有原子性要求则可以使用 -1
             */
            zkClient.setData(path, data.getBytes(), -1);
            return true;
        } catch (Exception e) {
            logger.error("修改节点异常，路径: {}, 数据: {}, 异常: {}", path, data, e);
            return false;
        }
    }

    /**
     * 删除节点
     *
     * @param path 路径
     * @return
     */
    public boolean deleteNode(String path) {
        try {
            /**
             * version：指定要更新数据的版本，如果 version 和真实的版本不同，更新操作将失败，指定 version 为 -1 则忽略版本检查
             */
            zkClient.delete(path, -1);
            return true;
        } catch (Exception e) {
            logger.error("删除节点异常，路径: {}, 异常: {}", path, e);
            return false;
        }
    }

    /**
     * 判断指定节点是否存在
     *
     * @param path      路径
     * @param needWatch 指定是否复用zookeeper中默认的Watcher
     * @return
     */
    public Stat exists(String path, boolean needWatch) {
        try {
            return zkClient.exists(path, needWatch);
        } catch (Exception e) {
            logger.error("判断指定节点是否存在异常，路径: {}, 异常: {}", path, e);
            return null;
        }
    }

    /**
     * 检测节点是否存在并设置监听事件(三种监听类型：创建、删除、更新)
     *
     * @param path    路径
     * @param watcher 传入指定的监听类
     * @return
     */
    public Stat exists(String path, Watcher watcher) {
        try {
            return zkClient.exists(path, watcher);
        } catch (Exception e) {
            logger.error("判断指定节点是否存在异常，路径: {}, 异常: {}", path, e);
            return null;
        }
    }

    /**
     * 获取当前节点的子节点(不包含孙子节点)
     *
     * @param path 父节点路径
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> list = zkClient.getChildren(path, false);
        return list;
    }

    /**
     * 获取指定节点的值
     *
     * @param path    路径
     * @param watcher 监听
     * @return
     */
    public String getData(String path, Watcher watcher) {
        try {
            Stat stat = new Stat();
            byte[] bytes = zkClient.getData(path, watcher, stat);
            return new String(bytes);
        } catch (Exception e) {
            logger.error("获取指定节点的值异常，路径: {}, 异常: {}", path, e);
            return null;
        }
    }
}