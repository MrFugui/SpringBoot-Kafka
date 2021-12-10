package com.wangfugui.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MaSiyi
 * @version 1.0.0 2021/12/10
 * @since JDK 1.8.0
 */
@RestController
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /** 发送消息
     * @Param:
     * @return:
     * @Author: MaSiyi
     * @Date: 2021/12/10
     */
    @GetMapping("/kafka/normal/{topic}/{message}")
    public void sendMessage1(@PathVariable("topic") String topic, @PathVariable("message") String normalMessage) {
        kafkaTemplate.send(topic, normalMessage);
    }

    @GetMapping("/kafka/callbackOne/{message}")
    public void sendMessage2(@PathVariable("message") String callbackMessage) {
        kafkaTemplate.send("topictest3", callbackMessage).addCallback(success -> {
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
        }, failure -> {
            System.out.println("发送消息失败:" + failure.getMessage());
        });
    }
    @GetMapping("/kafka/callbackTwo/{message}")
    public void sendMessage3(@PathVariable("message") String callbackMessage) {
        kafkaTemplate.send("topictest3", callbackMessage).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送消息失败："+ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
            }
        });
    }

    @GetMapping("/kafka/transaction1")
    public void sendMessage4(){
        // 声明事务：后面报错消息不会发出去
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send("topictest4","test executeInTransaction");
            System.out.println("发送消息");
            throw new RuntimeException("fail");
        });
    }

    @GetMapping("/kafka/transaction2")
    public void sendMessage5(){
        // 不声明事务：后面报错但前面消息已经发送成功了
        kafkaTemplate.send("topictest4","test executeInTransaction");
        System.out.println("发送消息");
        throw new RuntimeException("fail");
    }
}
