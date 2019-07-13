package com.api.message;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;

/**
 * 实现redis消息队列
 * 消费者
 * @author zhaoxingwu
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageConsumer implements Runnable {
	
    public static final String MESSAGE_KEY = "message:queue";
    private volatile int count;

    public void consumerMessage() {
    	Jedis jedis = new Jedis("127.0.0.1", 6379);
        List<String> brpop = jedis.brpop(0, MESSAGE_KEY); //0是timeout,返回的是一个集合，第一个是消息的key，第二个是消息的内容
        System.out.println(brpop + ",count=" + count);
        count++;
    }

    @Override
    public void run() {
        while (true) {
            consumerMessage();
        }
    }

    public static void main(String[] args) {
    	MessageConsumer messageConsumer = new MessageConsumer();
        Thread t1 = new Thread(messageConsumer, "thread6");
        Thread t2 = new Thread(messageConsumer, "thread7");
        t1.start();
        t2.start();
	}
}