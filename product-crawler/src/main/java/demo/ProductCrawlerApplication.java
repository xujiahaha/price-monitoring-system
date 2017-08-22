package demo;

import demo.service.ProductCrawlerInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by jiaxu on 8/1/17.
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
@EnableDiscoveryClient
public class ProductCrawlerApplication implements CommandLineRunner{

    @Autowired
    private ProductCrawlerInitService crawlerInitService;

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar productCrawler.jar <proxyFilePath>");
            return;
        }
        SpringApplication.run(ProductCrawlerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("proxy file path: " + args[0]);
        crawlerInitService.init(args[0]);
    }

    @Bean
    public AsyncTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("product-crawler-");
        executor.initialize();
        return executor;
    }

}
