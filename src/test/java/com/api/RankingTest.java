package com.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.cache.RedisClient;

/**
 * redis实现排行榜功能
 * ZSet 实现
 * @author zhaoxingwu
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RankingTest {

	@Autowired
	RedisClient redisCLient;
	
	private final static String rankingKey = "rankingBase";
	/**
	 * 随机插入五个成绩
	 */
//	@Test
	public void zadd() {
		Map<String, Double> paramMap = new HashMap<String, Double>();
		paramMap.put("jack", Double.valueOf(RandomUtils.nextInt(1, 100) + ""));
		paramMap.put("tom", Double.valueOf(RandomUtils.nextInt(1, 100) + ""));
		paramMap.put("jim", Double.valueOf(RandomUtils.nextInt(1, 100) + ""));
		paramMap.put("lily", Double.valueOf(RandomUtils.nextInt(1, 100) + ""));
		paramMap.put("lucky", Double.valueOf(RandomUtils.nextInt(1, 100) + ""));
		redisCLient.zadd(rankingKey, paramMap);
	}
	
	/**
	 * 获取排行榜某人的分数
	 */
//	@Test
	public void zscore() {
		Double result = redisCLient.zscore(rankingKey, "jack");
		System.out.println(result);
	}
	
	/**
	 * 获取排行榜顺序 从大到小
	 */
//	@Test
	public void zrevrange() {
		// 第一个参数是key，第二个第三个参数说明，获取第几位到第几位，-1表示显示所有
		Set<String> result = redisCLient.zrevrange(rankingKey, 0, -1);
		for (String string : result) {
			System.out.println(string);
		}
	}
	
	/**
	 * 获取某人在排行榜的位置
	 */
//	@Test
	public void zrevrank() {
		Long result = redisCLient.zrevrank(rankingKey, "lucky");
		System.out.println(result);
	}
	
	/**
	 * 增加某人分数
	 */
//	@Test
	public void zincrby() {
		Double result = redisCLient.zincrby(rankingKey, Double.valueOf(RandomUtils.nextInt(1, 100) + ""), "tom");
		System.out.println(result);
	}
	
	/**
	 * 删除排行榜某人成绩
	 */
	@Test
	public void zrem() {
		Long result = redisCLient.zrem(rankingKey, new String[] {"tom"});
		System.out.println(result);
	}

}
