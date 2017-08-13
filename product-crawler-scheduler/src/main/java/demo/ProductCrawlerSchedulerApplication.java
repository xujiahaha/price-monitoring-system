package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jiaxu on 8/12/17.
 */
@SpringBootApplication
@EnableScheduling
@EnableCircuitBreaker
public class ProductCrawlerSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCrawlerSchedulerApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
