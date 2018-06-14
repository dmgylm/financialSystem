package cn.financial.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class RedisUtils {
	@Autowired
	public JedisConnectionFactory jedisConnectionFactory;
	
	public String getData(String key){
		return jedisConnectionFactory.getShardInfo().createResource().get(key);
	}
	
	public void insert(String key, String value){
		jedisConnectionFactory.getShardInfo().createResource().getSet(key, value);
	}
	
	public void delete(String key){
		jedisConnectionFactory.getShardInfo().createResource().del(key);
	}

}
