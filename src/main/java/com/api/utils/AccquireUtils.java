package com.api.utils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.api.cache.RedisClient;

public class AccquireUtils {

	private final static String limiter = "10";
	
	public static boolean accquire(RedisClient redisClient) {
		Long result = 0l;
		try {
			ClassPathResource classPathResource = new ClassPathResource("limit.lua");
			InputStream inputStream = classPathResource.getInputStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer, "UTF-8");
			String luaScript = writer.toString();
	        String key = "ip:" + System.currentTimeMillis()/1000; // 当前秒
	        List<String> keys = new ArrayList<String>();
	        keys.add(key);
	        List<String> args = new ArrayList<String>();
	        args.add(limiter);
	        result = (Long)(redisClient.eval(luaScript, keys, args)); // 执行lua脚本，传入参数
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result == 1;
    }
}
