package com.api;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.cache.RedisClient;
import com.api.utils.AccquireUtils;
import com.google.common.base.Stopwatch;

/**
 * redis实现限流
 * 使用lua脚本
 * @author zhaoxingwu
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LimitLuaTest {

	@Autowired
	RedisClient redisClient;
	
	@Test
	public void limiter() throws InterruptedException {
		Stopwatch watch = Stopwatch.createStarted();
		for (int i = 0; i < 100; i++) {
			if (i == 30) {
				Thread.sleep(1500);
			}
			System.out.println(AccquireUtils.accquire(redisClient));
		}
		long time = watch.elapsed(TimeUnit.SECONDS);
		System.out.println(time);
	}
}
