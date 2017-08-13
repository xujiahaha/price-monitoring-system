package demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaxu on 8/9/17.
 */
@Configuration
public class DealSenderConfig {

    static final String EXCHANGE_NAME = "priceMonitoring.deals";
//    static final String[] CATEGORIES = {"appliances", "baby-products", "beauty", "computers", "electronics", "office-products"};
    static final String[] CATEGORIES = {"5", "9", "10", "25"};
    static final String QUEUE_NAME_PREFIX = "priceMonitoring.deals.";


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


    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setExchange(topicExchange().getName());
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
