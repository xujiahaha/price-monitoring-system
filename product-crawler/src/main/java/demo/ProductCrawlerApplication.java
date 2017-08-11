package demo;

import demo.task.ProductCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by jiaxu on 8/1/17.
 */
@SpringBootApplication
@EnableScheduling
public class ProductCrawlerApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(ProductCrawlerApplication.class);
    }

    @Autowired
    private ProductCrawler productCrawler;

    @Override
    public void run(String... strings) throws Exception {
        productCrawler.init();
//        productCrawler.crawling(9);
    }



}
