package demo.service.impl;

import demo.DealInfo;
import demo.config.RabbitmqConfig;
import demo.service.EmailNotificationService;
import demo.service.UserCategoryService;
import demo.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * Created by jiaxu on 8/11/17.
 */
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final String FROM = "xujiahaha@hotmail.com";
    private static final String HOST = "localhost";

    private List<DealInfo> appliances;
    private List<DealInfo> babyProducts;
    private List<DealInfo> beauty;
    private List<DealInfo> electronics;

    @Autowired
    private UserCategoryService userCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendEmailNotification() {

    }

    @Scheduled(cron = "*/10 * * * *")
    @RabbitListener(queues = "priceMonitoring.deals.all")



    private void sendEmail(String to) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("This is the Subject Line!");
            message.setText("This is actual message");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private String createEmailContent(List<DealInfo> dealInfoList) {
        StringBuilder content = new StringBuilder();
        for(DealInfo dealInfo : dealInfoList) {
            content.append(dealInfo.toString());
            content.append("\n");
        }
        return content.toString();
    }


}
