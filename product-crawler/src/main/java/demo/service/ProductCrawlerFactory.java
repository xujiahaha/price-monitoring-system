package demo.service;

import demo.model.CategorySeed;
import demo.task.ProductCrawler;

/**
 * Created by jiaxu on 8/12/17.
 */
public interface ProductCrawlerFactory {

    ProductCrawler createProductCrawler(CategorySeed categorySeed);

}
