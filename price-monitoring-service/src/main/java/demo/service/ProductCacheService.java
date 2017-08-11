package demo.service;

import demo.domain.Product;

/**
 * Created by jiaxu on 8/8/17.
 */
public interface ProductCacheService {

    boolean isProductExisted(String productId);

    double getCachedProductPrice(String productId);

    /*
    * key = detailUrl, value = productId + "#" + price
    * eg. "124#12.99"
    **/
    void cacheProductPrice(String productId, double price);

    void updateCachedProductPrice(String productId, double price);
}
