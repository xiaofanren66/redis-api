package com.api.message;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;

/**
 * 实现redis消息队列
 * 生产者
 * @author zhaoxingwu
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageProducer extends Thread {
	
	private static final String MESSAGE_KEY = "message:queue";
    private volatile int count;

    
    public void putMessage(String message) {
    	Jedis jedis = new Jedis("127.0.0.1", 6379);
        Long size = jedis.lpush(MESSAGE_KEY, new String[] {message});
        System.out.println(Thread.currentThread().getName() + " put message,size=" + size + ",count=" + count);
        count++;
    }

    @Override
    public synchronized void run() {
        for (int i = 0; i < 5; i++) {
            putMessage("message" + count);
        }
    }
    
    public static void main(String[] args) {
    	MessageProducer messageProducer = new MessageProducer();
        Thread t1 = new Thread(messageProducer, "thread1");
        Thread t2 = new Thread(messageProducer, "thread2");
        Thread t3 = new Thread(messageProducer, "thread3");
        Thread t4 = new Thread(messageProducer, "thread4");
        Thread t5 = new Thread(messageProducer, "thread5");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
	}
}