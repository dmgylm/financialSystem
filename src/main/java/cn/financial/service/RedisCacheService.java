package cn.financial.service;

import org.springframework.cache.Cache;

public interface RedisCacheService {
	
	/**
	 * 删除Cache
	 * @param cacheName
	 */
	public void removeAll(String cacheName);
	
	/**
	 * 根据Key删除单个缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 */
	public void remove(String cacheName,String key);
	
	/**
	 * 添加缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 * @param value
	 */
	public void put(String cacheName,String key,Object value);
	
	/**
	 * 根据Key获取缓存内容
	 * @param cacheName 缓存名称
	 * @param key 缓存Key
	 * @return
	 */
	public Object get(String cacheName, String key);

	/**
	 * 获取缓存对象
	 * @param cacheName
	 * @return
	 */
	public Cache get(String cacheName);
}
