package com.api;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.cache.RedisClient;

/**
 * redis实现好友关系
 * @author zhaoxingwu
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendsTest {

	@Autowired
	RedisClient redisClient;
	
	@Test
	public void friends() {
		
		redisClient.sadd("A:follow", "C", "D");
		redisClient.sadd("B:follow", "A", "D");
		
		System.out.println(redisClient.sismember("A:follow", "A"));
		System.out.println(redisClient.sismember("A:follow", "B"));
		System.out.println(redisClient.sismember("A:follow", "C"));
		System.out.println(redisClient.sismember("A:follow", "D"));
		System.out.println(redisClient.sismember("B:follow", "A"));
		System.out.println(redisClient.sismember("B:follow", "B"));
		System.out.println(redisClient.sismember("B:follow", "C"));
		System.out.println(redisClient.sismember("B:follow", "D"));
		System.out.println();
		
		// 取交集
		Set<String> interResults = redisClient.sinter("A:follow", "B:follow");
		for (String string : interResults) {
			System.out.println(string);
		}
		System.out.println();
		
		// 取交集
		Set<String> sunionResults = redisClient.sunion("A:follow", "B:follow");
		for (String string : sunionResults) {
			System.out.println(string);
		}
		System.out.println();
		
		// 取差集
		Set<String> sdiffResults = redisClient.sdiff("A:follow", "B:follow");
		for (String string : sdiffResults) {
			System.out.println(string);
		}
	}
}
