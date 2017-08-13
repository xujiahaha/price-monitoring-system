package demo.controller;

import demo.model.CategorySeed;
import demo.service.ProductCrawlerFactory;
import demo.task.ProductCrawler;
import demo.task.ProductCrawlerTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by jiaxu on 8/5/17.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api")
public class ProductCrawlerController {

    @Autowired
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    private ProductCrawlerFactory productCrawlerFactory;

    private Map<Long, ProductCrawlerTaskInfo> taskFutures = new HashMap<>();

    @RequestMapping(value = "/crawler", method = RequestMethod.POST)
    public void crawler(@RequestBody CategorySeed categorySeed) {
        ProductCrawler productCrawler = this.productCrawlerFactory.createProductCrawler(categorySeed);
        Future<?> future = taskExecutor.submit(productCrawler);
        log.info("added {} crawler to task executor on {}", categorySeed.getCategoryTitle(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        ProductCrawlerTaskInfo instance = new ProductCrawlerTaskInfo(productCrawler.getId(), categorySeed.getCategoryId(), categorySeed.getCategoryTitle(), future);
        taskFutures.put(instance.getInstanceId(), instance);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Collection<ProductCrawlerTaskInfo> status() {
        return taskFutures.values();
    }

}
