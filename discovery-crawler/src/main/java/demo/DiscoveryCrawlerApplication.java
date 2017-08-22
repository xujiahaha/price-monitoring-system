package demo;

import demo.service.CategorySeedService;
import demo.task.CategoryCrawler;
import demo.task.CategoryCrawlerInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by jiaxu on 8/4/17.
 */
@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
public class DiscoveryCrawlerApplication implements CommandLineRunner{

    @Autowired
    private CategoryCrawlerInitializer categoryCrawlerInitializer;

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar discoveryCrawler.jar <proxyFilePath>");
            return;
        }
        SpringApplication.run(DiscoveryCrawlerApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        categoryCrawlerInitializer.init(strings[0]);
    }
}
