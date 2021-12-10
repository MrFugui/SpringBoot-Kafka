
## 啦啦啦啦啦，富贵同学又开始开坑了，出了个免费的专栏，主要给大家从0基础开始用springBoot集成第三方的插件或者功能，如果这篇专栏能帮到你，一定不要忘了点一个赞哦！！欢迎大家收藏分享
![在这里插入图片描述](https://img-blog.csdnimg.cn/385dc942abfc4a019d845055328814c1.png#pic_center)
## 第一步，导入jar包

```java
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
```
## 第二步，服务器上启动kafka
如果不知道怎么安装，启动请查看博主的文章
[https://blog.csdn.net/csdnerM/article/details/121851493](https://blog.csdn.net/csdnerM/article/details/121851493)
配置文件连接kafka

```java
spring.kafka.bootstrap-servers=ip:端口
spring.kafka.consumer.group-id=consumer-group

```

## 点对点消费

编写生产类

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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

}

```
编写消费者

```java
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

    /** 消费监听
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

}

```
测试
![在这里插入图片描述](https://img-blog.csdnimg.cn/fc6534bf8ab04d0a9b20fd47ebeaad6a.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/9b91211d71bb4ba1a421d81ed51d4623.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_20,color_FFFFFF,t_70,g_se,x_16)

如果有两个方法

```java
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
```
则只会消费一个
![在这里插入图片描述](https://img-blog.csdnimg.cn/61c94f6e8d1f41b8a60df6cece35d9bf.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_20,color_FFFFFF,t_70,g_se,x_16)

## 发布订阅模式

生产者是同一个，消费者如下

```java
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

```
测试
![在这里插入图片描述](https://img-blog.csdnimg.cn/f33811f381884b39b8427b63f6dc2cc1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_19,color_FFFFFF,t_70,g_se,x_16)
![在这里插入图片描述](https://img-blog.csdnimg.cn/504bcb22a42542e69cf9654bcabfe8cc.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_20,color_FFFFFF,t_70,g_se,x_16)
## 方法回调
生产者

```java
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

```
消费者

```java
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
```
测试
![在这里插入图片描述](https://img-blog.csdnimg.cn/9a707cae86f148298683355ed2ba1170.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/140a87ea171649869c202e976a244268.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_20,color_FFFFFF,t_70,g_se,x_16)
## 事物提交
### 有异常不发送

```java
    @GetMapping("/kafka/transaction1")
    public void sendMessage4(){
        // 声明事务：后面报错消息不会发出去
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send("topictest4","test executeInTransaction");
            throw new RuntimeException("fail");
        });
    }

```
接收者

```java
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

```
测试
![在这里插入图片描述](https://img-blog.csdnimg.cn/ef64d028db6b4cc7b4011d20b9007b97.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_19,color_FFFFFF,t_70,g_se,x_16)

没有发送
![在这里插入图片描述](https://img-blog.csdnimg.cn/2f8ce7640a8d4f1e94f1d10ed8b79492.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_20,color_FFFFFF,t_70,g_se,x_16)

### 有异常发送

```java
    @GetMapping("/kafka/transaction2")
    public void sendMessage5(){
        // 不声明事务：后面报错但前面消息已经发送成功了
        kafkaTemplate.send("topictest4","test executeInTransaction");
        System.out.println("发送消息");
        throw new RuntimeException("fail");
    }
```
测试
![在这里插入图片描述](https://img-blog.csdnimg.cn/ac2516cc66f242579d488d4b3de217cd.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_20,color_FFFFFF,t_70,g_se,x_16)
已发送
![在这里插入图片描述](https://img-blog.csdnimg.cn/63e804cda35240469b6b3afbcc44744b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_20,color_FFFFFF,t_70,g_se,x_16)

好了，就是这么的简单，完整代码请移至[SpringBoot+Kafka ](https://gitee.com/WangFuGui-Ma/spring-boot-kafka)查看
![在这里插入图片描述](https://img-blog.csdnimg.cn/a866736dfb41420f8d8a8484d1e9abb7.jpg?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5o6J5aS05Y-R55qE546L5a-M6LS1,size_10,color_FFFFFF,t_70,g_se,x_16#pic_center)