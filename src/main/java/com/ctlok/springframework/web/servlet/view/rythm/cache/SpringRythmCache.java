package com.ctlok.springframework.web.servlet.view.rythm.cache;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

import com.greenlaw110.rythm.cache.ICacheService;

/**
 * @author Lawrence Cheung
 *
 */
public class SpringRythmCache implements ICacheService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringRythmCache.class);
    
    private final Cache cache;
    
    private int defaultTtl;
    
    public SpringRythmCache(final Cache cache){
        this.cache = cache;
    }
    
    @Override
    public void put(String key, Serializable value, int ttl) {
        LOGGER.debug("Put cache with key: [{}] and TTL: [{}]", key, ttl);
        this.cache.put(key, new CacheWrapper(ttl, value));
    }

    @Override
    public void put(String key, Serializable value) {
        LOGGER.debug("Put cache with key: [{}] and TTL: [{}]", key, this.defaultTtl);
        this.cache.put(key, new CacheWrapper(this.defaultTtl, value));
    }

    @Override
    public Serializable remove(String key) {
        LOGGER.debug("Remove cache with key: [{}]", key);
        final Serializable value = this.get(key);
        this.cache.evict(key);
        return value;
    }

    @Override
    public Serializable get(String key) {
        LOGGER.debug("Get cache with key: [{}]", key);
        final ValueWrapper valueWrapper = this.cache.get(key);
        Serializable value = null;
        
        if (valueWrapper != null){
            
            final CacheWrapper cacheWrapper = (CacheWrapper) valueWrapper.get();
            
            if (cacheWrapper.isExpire()){
                this.cache.evict(key);
            }else{
                value = cacheWrapper.getValue();
            }
            
        }
        
        return value;
    }

    @Override
    public boolean contains(String key) {
        LOGGER.debug("Is contains cache with key: [{}]", key);
        return this.get(key) != null;
    }

    @Override
    public void setDefaultTTL(int ttl) {
        LOGGER.debug("Default TTL: [{}]", ttl);
        this.defaultTtl = ttl;
    }
    
    @Override
    public void clear() {
        LOGGER.debug("clear cache");
        this.cache.clear();
    }

    @Override
    public void startup() {
     // Nothing to do
    }

    @Override
    public void shutdown() {
        // Nothing to do
    }
    
    class CacheWrapper implements Serializable {
        
        private static final long serialVersionUID = 8422330991415116904L;
        
        private final long createTime;
        private final int ttl;
        private final Serializable value;
        
        CacheWrapper(final int ttl, final Serializable value){
            this.createTime = System.currentTimeMillis() / 1000;
            this.ttl = ttl;
            this.value = value;
        }

        public long getCreateTime() {
            return createTime;
        }

        public int getTtl() {
            return ttl;
        }

        public Serializable getValue() {
            return value;
        }
        
        public boolean isExpire(){
            final long currentTime = System.currentTimeMillis() / 1000;
            return (currentTime - this.createTime) > this.ttl;
        }
        
    }

}
