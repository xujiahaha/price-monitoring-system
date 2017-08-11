package demo.service.impl;

import demo.service.ProductCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by jiaxu on 8/8/17.
 */
@Service
public class ProductCacheServiceImpl implements ProductCacheService {

    private StringRedisTemplate redisTemplate;

    @Autowired
    public ProductCacheServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean isProductExisted(String productId) {
        return this.redisTemplate.hasKey(productId);
    }

    @Override
    public double getCachedProductPrice(String productId) {
        return Double.parseDouble(this.redisTemplate.opsForValue().get(productId));
    }

    @Override
    public void cacheProductPrice(String productId,  double price) {
        this.redisTemplate.opsForValue().set(productId, String.valueOf(price));
    }

    @Override
    public void updateCachedProductPrice(String productId, double price) {
        this.redisTemplate.opsForValue().set(productId, String.valueOf(price));
    }

}
