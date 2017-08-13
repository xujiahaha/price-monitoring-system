package demo;

import demo.domain.Product;
import demo.dto.DealInfo;
import demo.dto.ProductInfo;
import demo.service.ProductCacheService;
import demo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiaxu on 8/8/17.
 */
@Service
@Slf4j
public class ProductReceiver {

    private ProductService productService;
    private ProductCacheService productCacheService;
    private DealSender dealSender;

    @Autowired
    public ProductReceiver(ProductService productService, ProductCacheService productCacheService, DealSender dealSender) {
        this.productService = productService;
        this.productCacheService = productCacheService;
        this.dealSender = dealSender;
    }


    @RabbitListener(queues = {"priceMonitoring.products.appliances",
                            "priceMonitoring.products.baby-products",
                            "priceMonitoring.products.beauty",
                            "priceMonitoring.products.electronics"})
    public void processProduct(ProductInfo productInfo) {
        String productId = productInfo.getProductId();
        log.debug("received productInfo {}", productId);

        // check if this product has been crawled before
        boolean isExist = this.productCacheService.isProductExisted(productId);

        if(isExist) {  // Existed

            // check if there is a change of price
            double lastPrice = this.productCacheService.getCachedProductPrice(productId);
            double latestPrice = productInfo.getPrice();
            if(latestPrice == lastPrice) {
                return;
            } else {

                // update cache and database
                Product latestProduct = this.productService.updateProduct(productId, latestPrice, lastPrice);
                this.productCacheService.updateCachedProductPrice(productId, latestPrice);

                // if price reduced, send deal info to price reduced queue
                if(latestPrice < lastPrice) {
                    DealInfo dealInfo = new DealInfo(latestProduct);
                    this.dealSender.sendDealToPriceReducedQueue(String.valueOf(productInfo.getCategoryId()), dealInfo);
                }
            }
        } else {  // Not existed in database
            Product product = this.productService.saveProduct(productInfo);
            this.productCacheService.cacheProductPrice(productId, productInfo.getPrice());
        }
    }

}
