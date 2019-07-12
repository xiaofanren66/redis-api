package com.api.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

/**
 * 封装jedis客户端工具类
 * 
 * @author zhaoxingwu
 *
 */
@Component
public class RedisClient {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JedisPool jedisPool;

	/**
	 * 释放连接
	 * 
	 * @param jedis
	 */
	private void release(Jedis jedis) {
		if (jedis != null)
			jedis.close();
	}

	/**
	 * 计数
	 * 
	 * @param key
	 * @return
	 */
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.incr(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 添加指定值
	 * 
	 * @param key
	 * @param count
	 * @return
	 */
	public Long incrBy(String key, Integer count) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.incrBy(key, count);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 检查给定 key 是否存在
	 *
	 * @param key 键
	 * @return 是否存在
	 */
	public boolean exist(String key) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.exists(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 设置此键的过期时间
	 *
	 * @param key     键
	 * @param seconds 过期时间（秒）
	 * @return 是否成功。api中设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis
	 *         中你尝试更新 key 的过期时间)返回 0 。
	 */
	public boolean expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		try {
			return 1 == jedis.expire(key, seconds);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 设置此键的过期时间点
	 *
	 * @param key      键
	 * @param unixTime 时间点
	 * @return 是否成功。api中设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis
	 *         中你尝试更新 key 的过期时间)返回 0 。
	 */
	public boolean expireAt(String key, long unixTime) {
		Jedis jedis = jedisPool.getResource();
		try {
			return 1 == jedis.expireAt(key, unixTime);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 更改键的名称
	 *
	 * @param key1 原键名
	 * @param key2 更新后的名
	 * @return 是否成功。api中改名成功时提示 OK ，失败时候返回一个错误。 当 OLD_KEY_NAME 和 NEW_KEY_NAME 相同，或者
	 *         OLD_KEY_NAME 不存在时，返回一个错误。 当 NEW_KEY_NAME 已经存在时， RENAME 命令将覆盖旧值。
	 */
	public boolean rename(String key1, String key2) {
		return false;
	}

	/**
	 * 删除键
	 *
	 * @param key 键
	 * @return 被删除的数量
	 */
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.del(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 删除键
	 *
	 * @param key 键
	 * @return 被删除的数量
	 */
	public Long del(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.del(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 根据键获取值
	 *
	 * @param key 键
	 * @return 值
	 */
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.get(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 根据键获取值
	 *
	 * @param key 键
	 * @return 值
	 */
	public byte[] get(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.get(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 设置键值
	 *
	 * @param key   键
	 * @param value 值
	 * @return 在 Redis 2.6.12 以前版本， SET 命令总是返回 OK 。从 Redis 2.6.12 版本开始， SET
	 *         在设置操作成功完成时，才返回 OK 。
	 */
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.set(key, value);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 将值value关联到key，并将key的生存时间设为seconds(以秒为单位)。SETEX是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成。
	 *
	 * @param key     键
	 * @param seconds 超过指定秒数自动过期
	 * @param value   值。如果在键中设置了值则返回OK。如果值未设置则返回 Null。
	 * @return 设置成功时返回 OK 。
	 */
	public String setex(String key, int seconds, String value) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.setex(key, seconds, value);
		} finally {
			release(jedis);
		}
	}

	public String setex(String key, int seconds, byte[] value) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.setex(key.getBytes(), seconds, value);
		} finally {
			release(jedis);
		}
	}

	public String getSet(String key, String value) {
		String pre = "";
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (jedis != null) {
				pre = jedis.getSet(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(jedis);
		}
		return pre;
	}

	/**
	 * 将 key 的值设为 value，当且仅当 key 不存在。若给定的 key 已经存在，则不做任何动作。SET if Not exists
	 * 
	 * @param key   键
	 * @param value 值
	 * @return 设置成功返回1，失败0: 1 if the key was set 0 if the key was not set
	 */
	public Long setnx(String key, String value) {
		Long ok = 0L;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (jedis != null) {
				ok = jedis.setnx(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(jedis);
		}
		return ok;
	}

	/**
	 * 原子性的设置value和过期时间
	 * 
	 * @param key   key
	 * @param value value nxxx NX|XX, NX -- Only set the key if it does not already
	 *              exist. XX -- Only set the key if it already exist. expx EX|PX,
	 *              expire time units: EX = seconds; PX = milliseconds time, expire
	 *              time in the units of <code>expx</code>
	 * @return Status code reply
	 */
	public boolean setNxExp(String key, String value, int expireSeconds) {
		logger.info("redis setNxExp key={} ,value={}, expireSeconds:{}", key, value, expireSeconds);
		try (Jedis jedis = jedisPool.getResource()) {
			String result = jedis.set(key, value, "NX", "EX", expireSeconds);
			return "OK".equalsIgnoreCase(result);
		} catch (Exception e) {
			logger.error("redis setNxExp Error", e);
		}
		return false;
	}

	/**
	 * 原子性的设置value和过期时间
	 * 
	 * @param key      key
	 * @param value    value
	 * @param NX_OR_XX NX|XX<br/>
	 *                 NX -- 仅当key不存在时，设置<br/>
	 *                 XX -- 仅当key存在时，设置
	 * @param EX_OR_PX EX|PX<br/>
	 *                 过期时间单位: EX = 秒（seconds）; PX = 毫秒(milliseconds)
	 * @param time     时间
	 * @return Status code reply
	 */
	public String set(String key, String value, String NX_OR_XX, String EX_OR_PX, long time) {
		String ok = "";
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (jedis != null) {
				ok = jedis.set(key, value, NX_OR_XX, EX_OR_PX, time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(jedis);
		}
		return ok;
	}

	/**
	 * 返回值的长度
	 *
	 * @param key 键
	 * @return 值的长度
	 */
	public Long strlen(String key) {
		return null;
	}

	/**
	 * 设置散列字段的字符串值 值不能为null，可以为空字符串
	 *
	 * @param key   键
	 * @param field 列
	 * @param value 值
	 * @return 是否成功.如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。
	 */
	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.hset(key, field, value);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 获取存储在指定键的哈希字段的值。
	 *
	 * @param key   键
	 * @param field 列
	 * @return 值。如果没找到，返回 NULL
	 */
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.hget(key, field);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 为多个哈希字段分别设置它们的值
	 *
	 * @param key   键
	 * @param tuple 多个哈希键值
	 */
	public String hmset(String key, Map<String, String> tuple) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.hmset(key, tuple);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 删除一个哈希字段
	 *
	 * @param key   键
	 * @param field 列
	 * @return 是否删除成功（1成功，0失败）
	 */
	public Long hdel(String key, String field) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.hdel(key, field);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 删除一个或多个哈希字段
	 *
	 * @param key    键
	 * @param fields 列数组
	 * @return 是否删除成功（1成功，0失败）
	 */
	public Long hdel(String key, String[] fields) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.hdel(key, fields);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 获取散列中的字段数量
	 *
	 * @param key 键
	 * @return 字段数量
	 */
	public Long hlen(String key) {
		return null;
	}

	/**
	 * 查看哈希表的指定字段是否存在。
	 *
	 * @param key   键
	 * @param field 字段
	 * @return 是否存在
	 */
	public boolean hexists(String key, String field) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.hexists(key, field);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 获取指定键的哈希中所有字段
	 *
	 * @param key 键
	 * @return 返回该键对应的哈希中的所有字段
	 */
	public Set<String> hkeys(String key) {
		return null;
	}

	/**
	 * 获取指定键的哈希中的所有值
	 *
	 * @param key 键
	 * @return 返回该键对应的哈希中的所有值
	 */
	public List<String> hvals(String key) {
		return null;
	}

	/**
	 * 获取存储在指定键的哈希中的所有字段和值
	 *
	 * @param key 键
	 * @return 返回该键对应的哈希列表
	 */
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.hgetAll(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 获取所有给定字段的值
	 *
	 * @param key    键
	 * @param fields 一个或多个给定字段的值
	 * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样。
	 */
	public List<String> hmget(String key, String[] fields) {
		return null;
	}

	/**
	 * 迭代哈希表中的键值对。
	 *
	 * @param key   键
	 * @param count 游标
	 * @return 数组列表
	 */
	public ScanResult<Map.Entry<String, String>> hscan(String key, int count) {
		return null;
	}

	/**
	 * 迭代哈希表中的键值对
	 *
	 * @param key     键
	 * @param pattern 匹配字符串
	 * @return 数组列表
	 */
	public ScanResult<Map.Entry<String, String>> hscan(String key, String pattern) {
		return null;
	}

	/**
	 * 用于将一个或多个值插入到列表的尾部(最右边)。如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。当列表存在但不是列表类型时，返回一个错误。
	 *
	 * @param key   键
	 * @param items 列表值数组
	 * @return 执行 RPUSH 操作后，列表的长度
	 */
	public Long rpush(String key, String[] items) {
		return null;
	}

	/**
	 * 将所有指定的值插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key 对应的值不是一个
	 * list 的话，那么会返回一个错误。
	 *
	 * @param key   键
	 * @param items 指定值
	 * @return 插入的数量
	 */
	public Long lpush(String key, String[] items) {
		Jedis jedis = jedisPool.getResource();

		try {
			return jedis.lpush(key, items);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 获取列表长度
	 *
	 * @param key 键
	 * @return 列表长度
	 */
	public Long llen(String key) {
		return null;
	}

	/**
	 * 获取列表指定范围内的元素
	 *
	 * @param key   键
	 * @param start 其中 0 表示列表的第一个元素
	 * @param end   结束的索引位置。如果为-1则表示列表最后一个元素；-2表示列表的倒数第二个元素，以此类推
	 * @return 范围内的元素集合
	 */
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.lrange(key, start, end);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 *
	 * @param key   键
	 * @param start 其中 0 表示列表的第一个元素
	 * @param end   结束的索引位置。如果为-1则表示列表最后一个元素；-2表示列表的倒数第二个元素，以此类推
	 */
	public void ltrim(String key, long start, long end) {
	}

	/**
	 * 通过索引获取列表中的元素
	 *
	 * @param key   键
	 * @param index 列表索引
	 * @return 索引对应的值
	 */
	public String lindex(String key, long index) {
		return null;
	}

	/**
	 * 通过索引设置列表元素的值
	 *
	 * @param key   键
	 * @param index 列表索引
	 * @param item  要设置的值
	 * @return 操作成功返回 ok ，否则返回错误信息。
	 */
	public String lset(String key, long index, String item) {
		return null;
	}

	/**
	 * 移除指定索引的列表元素
	 *
	 * @param key   键
	 * @param index 列表索引
	 * @param item  要设置的值
	 * @return 被移除元素的数量。 列表不存在时返回 0 。
	 */
	public Long lrem(String key, long index, String item) {
		return null;
	}

	/**
	 * 移出并获取列表的第一个元素
	 *
	 * @param key 键
	 * @return 列表的第一个元素。 当列表 key 不存在时，返回 nil
	 */
	public String lpop(String key) {
		return null;
	}

	/**
	 * 移除并获取列表最后一个元素
	 * 
	 * @param key 键
	 * @return 列表的最后一个元素。 当列表不存在时，返回 nil 。
	 */
	public String rpop(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.rpop(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 列表的元素前或者后插入元素。 当指定元素不存在于列表中时，不执行任何操作。 当列表不存在时，被视为空列表，不执行任何操作。 如果 key
	 * 不是列表类型，返回一个错误。
	 *
	 * @param key           键
	 * @param position      位置，前 or 后
	 * @param existingValue 已存在的值
	 * @param newValue      新值
	 * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到指定元素 ，返回 -1 。 如果 key 不存在或为空列表，返回 0 。
	 */
	public Long linsert(String key, BinaryClient.LIST_POSITION position, String existingValue, String newValue) {
		return null;
	}

	/**
	 * 将一个或多个值插入到已存在的列表头部，列表不存在时操作无效。
	 *
	 * @param key    键
	 * @param values 一个或多个值
	 * @return 命令执行之后，列表的长度。
	 */
	public Long lpushx(String key, String[] values) {
		return null;
	}

	/**
	 * 将一个或多个值插入到已存在的列表尾部(最右边)。如果列表不存在，操作无效。
	 *
	 * @param key    键
	 * @param values 一个或多个值
	 * @return 操作后，列表的长度。
	 */
	public Long rpushx(String key, String[] values) {
		return null;
	}

	/*
	 * 集合（Set）操作
	 */
	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 假如 key 不存在，则创建一个只包含
	 * member 元素作成员的集合。 当 key 不是集合类型时，返回一个错误。
	 *
	 * @param key     键
	 * @param members 待插入的成员
	 * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 */
	public Long sadd(String key, String... members) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.sadd(key, members);
		}
	}

	/**
	 * 返回集合中的所有成员
	 * 
	 * @param key 键
	 * @return 集合中的所有成员。
	 */
	public Set<String> smembers(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.smembers(key);
		} finally {
			release(jedis);
		}
	}

	/**
	 * 移除集合中的一个或多个成员元素，不存在的成员元素会被忽略
	 *
	 * @param key     键
	 * @param members 被移除的成员元素
	 * @return 被成功移除的元素的数量，不包括被忽略的元素
	 */
	public Long srem(String key, String[] members) {
		return null;
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 *
	 * @param key 键
	 * @return 被移除的随机元素。当集合不存在或是空集时，返回 nil 。
	 */
	public String spop(String key) {
		return null;
	}

	/**
	 * 获取集合的成员数
	 *
	 * @param key 键
	 * @return 集合的数量。 当集合 key 不存在时，返回 0 。
	 */
	public Long scard(String key) {
		return null;
	}

	/**
	 * 判断 member 元素是否是集合 key 的成员
	 *
	 * @param key   键
	 * @param value 值
	 * @return 如果成员元素是集合的成员，返回 1 。 如果成员元素不是集合的成员，或 key 不存在，返回 0 。
	 */
	public Boolean sismember(String key, String value) {
		return null;
	}

	/**
	 * 返回集合中一个或多个随机数
	 *
	 * @param key 键
	 * @return 返回一个元素
	 */
	public String srandmember(String key) {
		return null;
	}

	/**
	 * @param key   键
	 * @param count 从 Redis 2.6 版本开始， Srandmember 命令接受可选的 count 参数 如果 count
	 *              为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。 如果 count
	 *              大于等于集合基数，那么返回整个集合。 如果 count
	 *              为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
	 * @return 如果集合为空，返回 nil 。 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组。
	 */
	public List<String> srandmember(String key, int count) {
		return null;
	}

	/**
	 * 命令用于迭代集合键中的元素。
	 *
	 * @param key     键
	 * @param pattern 匹配值
	 * @return 数组列表
	 */
	public ScanResult<String> sscan(String key, String pattern) {
		return null;
	}

	/**
	 * 迭代集合键中的元素
	 *
	 * @param key   键
	 * @param count 游标
	 * @return 少量的元素
	 */
	public ScanResult<String> sscan(String key, int count) {
		return null;
	}

	/*
	 * 有序集合(sorted set)操作
	 */

	public Long zadd(String paramString1, double paramDouble, String paramString2) {
		return null;
	}

	public Long zadd(String key, Map<String, Double> paramMap) {
		return null;
	}

	public Set<String> zrange(String key, long paramLong1, long paramLong2) {
		return null;
	}

	public Long zrem(String key, String[] paramArrayOfString) {
		return null;
	}

	public Double zincrby(String paramString1, double paramDouble, String paramString2) {
		return null;
	}

	public Long zrank(String paramString1, String paramString2) {
		return null;
	}

	public Long zrevrank(String paramString1, String paramString2) {
		return null;
	}

	public Set<String> zrevrange(String key, long paramLong1, long paramLong2) {
		return null;
	}

	public Set<Tuple> zrangeWithScores(String key, long paramLong1, long paramLong2) {
		return null;
	}

	public Set<Tuple> zrevrangeWithScores(String key, long paramLong1, long paramLong2) {
		return null;
	}

	public Long zcard(String key) {
		return null;
	}

	public Double zscore(String paramString1, String paramString2) {
		return null;
	}

	public List<String> sort(String key) {
		return null;
	}

	public List<String> sort(String key, SortingParams paramSortingParams) {
		return null;
	}

	public Long zcount(String key, double paramDouble1, double paramDouble2) {
		return null;
	}

	public Long zcount(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public Set<String> zrangeByScore(String key, double paramDouble1, double paramDouble2) {
		return null;
	}

	public Set<String> zrangeByScore(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public Set<String> zrevrangeByScore(String key, double paramDouble1, double paramDouble2) {
		return null;
	}

	public Set<String> zrangeByScore(String key, double paramDouble1, double paramDouble2, int paramInt1,
			int paramInt2) {
		return null;
	}

	public Set<String> zrevrangeByScore(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public Set<String> zrangeByScore(String paramString1, String paramString2, String paramString3, int paramInt1,
			int paramInt2) {
		return null;
	}

	public Set<String> zrevrangeByScore(String key, double paramDouble1, double paramDouble2, int paramInt1,
			int paramInt2) {
		return null;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double paramDouble1, double paramDouble2) {
		return null;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double paramDouble1, double paramDouble2) {
		return null;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double paramDouble1, double paramDouble2, int paramInt1,
			int paramInt2) {
		return null;
	}

	public Set<String> zrevrangeByScore(String paramString1, String paramString2, String paramString3, int paramInt1,
			int paramInt2) {
		return null;
	}

	public Set<Tuple> zrangeByScoreWithScores(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public Set<Tuple> zrangeByScoreWithScores(String paramString1, String paramString2, String paramString3,
			int paramInt1, int paramInt2) {
		return null;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double paramDouble1, double paramDouble2, int paramInt1,
			int paramInt2) {
		return null;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String paramString1, String paramString2, String paramString3,
			int paramInt1, int paramInt2) {
		return null;
	}

	public Long zremrangeByRank(String key, long paramLong1, long paramLong2) {
		return null;
	}

	public Long zremrangeByScore(String key, double paramDouble1, double paramDouble2) {
		return null;
	}

	public Long zremrangeByScore(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public Long zlexcount(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public Set<String> zrangeByLex(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public Set<String> zrangeByLex(String paramString1, String paramString2, String paramString3, int paramInt1,
			int paramInt2) {
		return null;
	}

	public Long zremrangeByLex(String paramString1, String paramString2, String paramString3) {
		return null;
	}

	public ScanResult<Tuple> zscan(String key, int count) {
		return null;
	}

	public ScanResult<Tuple> zscan(String paramString1, String pattern) {
		return null;
	}

	/*
	 * 其他操作
	 */

	public List<String> blpop(String key) {
		return null;
	}

	public List<String> blpop(int paramInt, String key) {
		return null;
	}

	public List<String> brpop(String key) {
		return null;
	}

	public List<String> brpop(int paramInt, String key) {
		return null;
	}

	public Long bitcount(String key) {
		return null;
	}

	public Long bitcount(String key, long paramLong1, long paramLong2) {
		return null;
	}

	public Long pfadd(String key, String[] paramArrayOfString) {
		return null;
	}

	public long pfcount(String key) {
		return 0;
	}
}
