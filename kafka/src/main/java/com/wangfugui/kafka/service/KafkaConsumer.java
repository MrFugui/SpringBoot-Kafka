package com.wangfugui.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author MaSiyi
 * @version 1.0.0 2021/12/10
 * @since JDK 1.8.0
 */
@Service
public class KafkaConsumer {

    /** 点对点消费
     * @Param: [record]
     * @return: void
     * @Author: MaSiyi
     * @Date: 2021/12/10
     */
    @KafkaListener(topics = {"topictest1"})
    public void message1(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("点对点消费1："+record.topic()+"-"+record.partition()+"-"+record.value());
    }
    /** 点对点消费
     * @Param: [record]
     * @return: void
     * @Author: MaSiyi
     * @Date: 2021/12/10
     */
    @KafkaListener(topics = {"topictest1"})
    public void message(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("点对点消费2："+record.topic()+"-"+record.partition()+"-"+record.value());
    }
    /** 发布订阅模式
     * @Param: [record]
     * @return: void
     * @Author: MaSiyi
     * @Date: 2021/12/10
     */
    @KafkaListener(topics = {"topictest2"},groupId = "1")
    public void message2(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("发布订阅模式1："+record.topic()+"-"+record.partition()+"-"+record.value());
    }
    /** 发布订阅模式
     * @Param: [record]
     * @return: void
     * @Author: MaSiyi
     * @Date: 2021/12/10
     */
    @KafkaListener(topics = {"topictest2"},groupId = "2")
    public void message3(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("发布订阅模式2："+record.topic()+"-"+record.partition()+"-"+record.value());
    }
    /** 消息回调
     * @Param: [record]
     * @return: void
     * @Author: MaSiyi
     * @Date: 2021/12/10
     */
    @KafkaListener(topics = {"topictest3"})
    public void message4(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("回调方法："+record.topic()+"-"+record.partition()+"-"+record.value());
    }

    /** 事物
     * @Param: [record]
     * @return: void
     * @Author: MaSiyi
     * @Date: 2021/12/10
     */
    @KafkaListener(topics = {"topictest4"})
    public void message5(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("回调方法："+record.topic()+"-"+record.partition()+"-"+record.value());
    }



}
