package cn.financial.util.shiro;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;


public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher{
    @Autowired
    @Qualifier("apiRedisTemplate")
    private RedisTemplate redis;

    //private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        //passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }
    
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        /*AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        System.out.println("*******************"+retryCount);
        if(retryCount.incrementAndGet() > 3) {
            //if retry count > 3 throw
            throw new ExcessiveAttemptsException();
        }*/
        /*int count = 0;
        Object obj=redis.opsForValue().get(username);//获取错误次数
        Object locking=redis.opsForValue().get("financialSystem"+"_cache_"+username+"_status");//获取是否锁定
        if(locking!=null){
            //抛出用户锁定异常
            throw new ExcessiveAttemptsException();
        }
        if(obj==null){
            redis.opsForValue().set(username, 1, 24, TimeUnit.HOURS);//设置错误次数的缓存key，时间24小时，默认次数第一次
        }else{
            count=(int)obj+1;//错误次数加1 
            redis.opsForValue().set(username, count, 24, TimeUnit.HOURS);//错误次数加1
        }*/
        boolean matches = super.doCredentialsMatch(token, info);//密码验证
        System.out.println(matches+"*****************************************");
        /*if(matches) {
            //clear retry count
            //passwordRetryCache.remove(username);
            redis.delete(username);//登陆成功清除key
        }else{//判断是否登录成功
            if(count>=3){//判断登陆失败次数
                redis.opsForValue().set("financialSystem"+"_cache_"+username+"_status", "N");//锁定账户
                throw new ExcessiveAttemptsException();
            }
        }*/
        return matches;
    }
}
