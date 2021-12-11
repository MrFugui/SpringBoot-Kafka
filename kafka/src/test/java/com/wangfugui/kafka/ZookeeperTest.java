package com.wangfugui.kafka;


import com.wangfugui.kafka.service.CustomWatcher;
import com.wangfugui.kafka.util.ZkUtil;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author jieshao
 * @date 2021/11/11 12:04
 */
@SpringBootTest(classes = KafkaApplication.class)
public class ZookeeperTest {
    @Autowired
    private ZkUtil zkUtil;

    /**
     * 新增节点
     */
    @Test
    public void testCreateNode() {
        boolean result = zkUtil.createPerNode("/demo", "zookeeper01");
        System.out.println(result == true ? "create success" : "create fail");
    }

    /**
     * 修改节点
     */
    @Test
    public void testUpdateNode() {
        boolean result = zkUtil.updateNode("/demo", "zookeeper02");
        System.out.println(result == true ? "update success" : "update fail");
    }

    /**
     * 获取节点是否存在
     * 自定义监听
     */
    @Test
    public void exists() {
        Stat stat = zkUtil.exists("/demo", new CustomWatcher());
        System.out.println(stat == null ? "not exist" : "exist");
    }

    /**
     * 获取节点数据
     * 自定义监听
     */
    @Test
    public void getData() throws InterruptedException {
        String data = zkUtil.getData("/demo", new CustomWatcher());
        System.out.println(data);
        zkUtil.updateNode("/demo", "zookeeper03");
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 删除节点
     */
    @Test
    public void testDeleteNode() {
        boolean result = zkUtil.deleteNode("/demo");
        System.out.println(result == true ? "delete success" : "delete fail");
    }
}