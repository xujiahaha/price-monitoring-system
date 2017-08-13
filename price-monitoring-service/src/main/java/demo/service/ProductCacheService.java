package demo.service;

import demo.domain.Product;

/**
 * Created by jiaxu on 8/8/17.
 */
public interface ProductCacheService {

    boolean isProductExisted(String productId);

    double getCachedProductPrice(String productId);

    void cacheProductPrice(String productId, double price);

    void updateCachedProductPrice(String productId, double price);
}
