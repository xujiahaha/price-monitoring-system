package demo.service;

import demo.model.ProductInfo;

/**
 * Created by jiaxu on 8/11/17.
 */
public interface ProductSendService {

    void sendProdToQueue(String key, ProductInfo productInfo);

}
