package cn.financial.service;

import org.springframework.cache.Cache;

public interface RedisCacheService {
	
	public void removeAll(String cacheName);
	
	public void remove(String cacheName,String key);
	
	public void put(String cacheName,String key,Object value);
	
	public Object get(String cacheName, String key);

	public Cache get(String cacheName);
}
