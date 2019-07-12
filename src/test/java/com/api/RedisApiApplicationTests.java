package com.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.cache.RedisClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApiApplicationTests {

	@Autowired
	RedisClient redisCLient;
	
	@Test
	public void contextLoads() {
		redisCLient.set("hello", "123");
	}

}
