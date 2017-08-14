package demo;

import demo.config.DealSenderConfig;
import demo.dto.DealInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiaxu on 8/8/17.
 */
@Service
@Slf4j
public class DealSender {

    private DealSenderConfig dealSenderConfig;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public DealSender(DealSenderConfig dealSenderConfig) {
        this.dealSenderConfig = dealSenderConfig;
        this.rabbitTemplate = dealSenderConfig.rabbitTemplate();
    }

    public void sendDealToPriceReducedQueue(DealInfo dealInfo) {
        log.info("sending deals to price reduced queue...");
        this.rabbitTemplate.convertAndSend("price.reduced", dealInfo);
    }
}
