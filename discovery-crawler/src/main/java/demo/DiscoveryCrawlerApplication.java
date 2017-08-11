package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by jiaxu on 8/4/17.
 */
@SpringBootApplication
public class DiscoveryCrawlerApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryCrawlerApplication.class, args);
    }

    @Autowired
    private CategoryCrawler categoryCrawler;


    @Override
    public void run(String... strings) throws Exception {
        categoryCrawler.init();
        categoryCrawler.crawling();
    }
}
