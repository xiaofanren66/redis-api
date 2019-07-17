package com.api.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static java.lang.Thread.sleep;

/**
 * 基于redis的分布式锁 参考博客：http://blog.csdn.net/ugg/article/details/41894947
 * @author zhaoxingwu
 *
 */
@Component
public final class RedisLock {

	public static final String LOCK_PREFIX = "lock";
	private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);
	@Resource
	private RedisClient redisClient;

	/**
	 * 获得锁
	 * 
	 * @param key
	 * @param lockTime
	 * @param tryTime
	 * @return
	 */
	public boolean lock(String key, int lockTime, int tryTime) {
		boolean lockStatus = false;
		key = String.format("%s_%s", LOCK_PREFIX, key);

		// 重试一定次数，还是拿不到，就放弃
		try {
			long status;
			long tryFailTime = System.currentTimeMillis() + tryTime;
			do {
				status = redisClient.setnx(key, Long.toString(System.currentTimeMillis() + lockTime));
				if (status == 1) {
					lockStatus = true;
				} else {
					// 获取锁失败，查看锁是否超时
					String time = redisClient.get(key);
					// 在加锁和检查之间，锁被删除了，尝试重新加锁
					if (time == null) {
						continue;
					}
					// 锁的超时时间戳小于当前时间，证明锁已经超时
					if (Long.parseLong(time) < System.currentTimeMillis()) {
						String oldTime = redisClient.getSet(key, Long.toString(System.currentTimeMillis() + lockTime));
						if (oldTime == null || oldTime.equals(time)) {
							lockStatus = true;
							break;
						}
					}
					try {
						sleep(tryTime / 10);
					} catch (Exception e) {
						logger.error("lock key:{} sleep failed!", key);
					}
				}
			} while (status == 0 && tryFailTime > System.currentTimeMillis());

			if (lockStatus) {
				redisClient.expire(key, lockTime / 1000);
				logger.info("lock key:{} success!", key);
			}
		} catch (Exception e) {
			logger.error("redis lock key:{} failed! reids cache exception: ", key, e);
		}

		return lockStatus;
	}

	/**
	 * 解锁
	 * 
	 * @param key
	 */
	public void releaseLock(String key) {
		key = String.format("%s_%s", LOCK_PREFIX, key);
		try {
			long deletedCount = redisClient.del(key);
			if (deletedCount == 1) {
				logger.info("release lock key:{} success!", key);
			} else {
				logger.info("release lock key:{} failed!", key);
			}
		} catch (Exception e) {
			logger.error("redis release key:{} failed! reids cache exception: ", key, e);
		}
	}
}
