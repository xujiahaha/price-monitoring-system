package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by jiaxu on 8/1/17.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PriceMonitoringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceMonitoringServiceApplication.class, args);
    }

}
