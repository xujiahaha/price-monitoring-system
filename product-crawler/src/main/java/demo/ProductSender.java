package demo;

import demo.model.ProductInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiaxu on 8/5/17.
 */
@Service
public class ProductSender {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ProductSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendProdToQueue(String key, ProductInfo productInfo) {
        System.out.println("routing key: " + key);
        this.rabbitTemplate.convertAndSend(key, productInfo);
    }

}
