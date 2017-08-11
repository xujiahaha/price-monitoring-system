package demo.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

/**
 * Created by jiaxu on 8/8/17.
 */
@Configuration
public class ProductReceiverConfig {

    static final String[] CATEGORIES = {"appliances", "baby-products", "beauty", "electronics"};
    static final String QUEUE_NAME_PREFIX = "priceMonitoring.products.";

    @Autowired
    private ConfigurableApplicationContext context;

    @Bean
    boolean createQueues() {
        for(String category : CATEGORIES) {
            this.context.getBeanFactory().registerSingleton(category, new Queue(QUEUE_NAME_PREFIX+category, true));
        }
        return true;
    }

}
