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


/**
 * Created by jiaxu on 8/9/17.
 */
@Configuration
public class DealSenderConfig {

    static final String EXCHANGE_NAME = "priceMonitoring.deals";
    static final String OUTPUT_QUEUE_NAME = "priceMonitoring.deals.all";


    @Autowired
    private ConfigurableApplicationContext context;


    @Bean
    boolean createQueue() {
        this.context.getBeanFactory().registerSingleton("price.reduced", new Queue(OUTPUT_QUEUE_NAME, true));
        return true;
    }


    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding1() {
        return BindingBuilder.bind(this.context.getBeansOfType(Queue.class).get("price.reduced")).to(directExchange()).with("price.reduced");
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
        rabbitTemplate.setExchange(directExchange().getName());
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
