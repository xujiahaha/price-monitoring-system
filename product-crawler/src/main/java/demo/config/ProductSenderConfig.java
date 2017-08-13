package demo.config;

import org.springframework.amqp.core.*;
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

import java.util.*;

/**
 * Created by jiaxu on 8/5/17.
 */
@Configuration
public class ProductSenderConfig {

    static final String EXCHANGE_NAME = "priceMonitoring.products";
    static final String[] CATEGORIES = {"appliances", "baby-products", "beauty", "electronics"};
    static final String QUEUE_NAME_PREFIX = "priceMonitoring.products.";

    @Autowired
    private ConfigurableApplicationContext context;

    @Bean
    boolean queues() {
        for(String category : CATEGORIES) {
            this.context.getBeanFactory().registerSingleton(category, new Queue(QUEUE_NAME_PREFIX+category, true));
        }
        return true;
    }


    @Bean
    List<Binding> bindings() {
        List<Binding> bindings = new ArrayList<>();
        Map<String, Queue> queues = this.context.getBeansOfType(Queue.class);
        for(Map.Entry<String, Queue> entry: queues.entrySet()) {
            String routingKey = entry.getKey();
            Queue queue = entry.getValue();
            bindings.add(BindingBuilder.bind(queue).to(topicExchange()).with(routingKey));
        }
        return bindings;
    }


    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

//    @Bean
//    public MessageConverter jsonMessageConverter(){
//        return new JsonMessageConverter();
//    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setExchange(topicExchange().getName());
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
