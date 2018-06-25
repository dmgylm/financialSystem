package cn.financial.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheUtils {
	
	@Autowired
	private RedisCacheManager cacheRedisCacheManager;
	
	public void removeAll(String cacheName){
		Cache cache = cacheRedisCacheManager.getCache(cacheName);
		cache.clear();
	}
	
	public void remove(String cacheName,String key){
		Cache cache = cacheRedisCacheManager.getCache(cacheName);
		cache.evict(key);
	}
	
	public void put(String cacheName,String key,Object value){
		Cache cache = cacheRedisCacheManager.getCache(cacheName);
		cache.put(key, value);
	}
	
	public Object get(String cacheName, String key) {
		Cache cache = cacheRedisCacheManager.getCache(cacheName);
		RedisCacheElement element = (RedisCacheElement) cache.get(key);
		return element == null ? null : element.get();
	}

	public Cache get(String cacheName) {
		Cache cache = cacheRedisCacheManager.getCache(cacheName);
		return cache;
	}
}
