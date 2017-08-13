package demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jiaxu on 8/11/17.
 */
@Configuration
public class RabbitmqConfig {

    static final String QUEUE_NAME= "priceMonitoring.deals.all";

    @Autowired
    private ConfigurableApplicationContext context;

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

}
