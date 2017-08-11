package demo.controller;

import demo.task.ProductCrawler;
import demo.ProductSender;
import demo.service.CategorySeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiaxu on 8/5/17.
 */
@RestController
public class ProductCrawlerController {

    @Autowired
    private ProductSender productSender;

    @Autowired
    private CategorySeedService categorySeedService;

//    @RequestMapping(value = "/start", method = RequestMethod.POST)
//    public boolean start(@RequestBody ProductInfo productInfo) {
//        CategorySeed seed = this.categorySeedService.getSeedByCategoryId(productInfo.getCategoryId());
//        System.out.println(seed.getCategoryTitle());
//        String key = seed.getSearchAlias();
//        productSender.sendProdToQueue(key ,productInfo);
//        return true;
//    }

    @Autowired
    private ProductCrawler productCrawler;

    @RequestMapping(value = "/start/{categoryId}", method = RequestMethod.POST)
    public void start(@PathVariable("categoryId") int categoryId) {
        this.productCrawler.crawling(categoryId);
    }
}
