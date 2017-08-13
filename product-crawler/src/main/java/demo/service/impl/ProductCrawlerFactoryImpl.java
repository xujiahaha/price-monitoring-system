package demo.service.impl;

import demo.model.CategorySeed;
import demo.service.ProductCrawlerFactory;
import demo.service.ProductCrawlerInitService;
import demo.service.ProductSendService;
import demo.task.ProductCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jiaxu on 8/12/17.
 */
@Service
public class ProductCrawlerFactoryImpl implements ProductCrawlerFactory{

    private ProductSendService productSendService;
    private ProductCrawlerInitService crawlerInitService;

    private Random random = new Random();

    private long crawlerId = 1;

    @Autowired
    public ProductCrawlerFactoryImpl(ProductSendService productSendService, ProductCrawlerInitService crawlerInitService) {
        this.productSendService = productSendService;
        this.crawlerInitService = crawlerInitService;
    }

    @Override
    public ProductCrawler createProductCrawler(CategorySeed categorySeed) {
        ProductCrawler productCrawler = new ProductCrawler(categorySeed);
        productCrawler.setId(crawlerId++);
        productCrawler.setProxyList(this.crawlerInitService.getProxyList());
        productCrawler.setProductSendService(productSendService);
        productCrawler.setProxyIndex(new AtomicInteger(random.nextInt(this.crawlerInitService.getNumOfProxy())));
        return productCrawler;
    }
}
