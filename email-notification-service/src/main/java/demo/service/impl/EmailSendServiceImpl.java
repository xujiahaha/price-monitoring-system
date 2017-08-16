package demo.service.impl;

import demo.domain.User;
import demo.domain.UserCategory;
import demo.model.DealInfo;
import demo.service.CategoryService;
import demo.service.EmailSendService;
import demo.service.UserCategoryService;
import demo.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by jiaxu on 8/14/17.
 */
@Service
@Slf4j
public class EmailSendServiceImpl implements EmailSendService {

    private static String SUBJECT_TEMPLATE = "Save big on ";
    private static String GREETING_TEMPLATE = ", check these deals before they are gone!";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCategoryService userCategoryService;

    private List<SendEmailTaskInstance> futureTask = new ArrayList<>();
    private long counter = 1;

    @Override
    public void sendToSubscribers(int categoryId, List<DealInfo> dealInfoList) {
        List<User> subscribers = getSubscribers(categoryId);
        String subject = createSubject(categoryId);
        String content = createContent(dealInfoList);
        SendEmailTask task = new SendEmailTask(subscribers, subject, content);
        Future<?> future = taskExecutor.submit(task);
        log.info("Scheduled a email sender for category {}", categoryId);
        SendEmailTaskInstance instance = new SendEmailTaskInstance(counter++, categoryId, task, future);
        futureTask.add(instance);
    }

    @Override
    public List<SendEmailTaskInstance> getFutureTask() {
        return this.futureTask;
    }

    private String createSubject(int categoryId) {
        String categoryTitle = this.categoryService.getCategoryTitleById(categoryId);
        return SUBJECT_TEMPLATE + categoryTitle;
    }

    private String createContent(List<DealInfo> dealInfoList) {
        StringBuilder content = new StringBuilder();
        content.append(GREETING_TEMPLATE);
        content.append("\n").append("\n");
        for(DealInfo dealInfo : dealInfoList) {
            content.append("  Title: ").append(dealInfo.getTitle()).append("\n");
            content.append("  Link: ").append(dealInfo.getDetailUrl()).append("\n");
            content.append("  Thumbnail: ").append(dealInfo.getThumbnail()).append("\n");
            content.append("  Price: ").append(dealInfo.getCurPrice()).append("\n");
            content.append("  Old price: ").append(dealInfo.getOldPrice()).append("\n");
            content.append("  You save: ").append(dealInfo.getDiscountPercent()).append("%").append("\n");
            content.append("\n");
        }
        return content.toString();
    }

    private List<User> getSubscribers(int categoryId) {
        List<User> users = new ArrayList<>();
        List<UserCategory> userCategoryList = this.userCategoryService.getByCategoryId(categoryId);
        for(UserCategory userCategory : userCategoryList) {
            users.add(this.userService.getById(userCategory.getUserId()));
        }
        return users;
    }

    @Data
    private class SendEmailTask implements Runnable {

        private List<User> recipients;
        private String subject;
        private String content;

        public SendEmailTask(List<User> recipients, String subject, String content) {
            this.recipients = recipients;
            this.subject = subject;
            this.content = content;
        }

        @Override
        public void run() {
            for(User user: recipients) {
                String text = user.getUsername() + content;
                MimeMessage message = javaMailSender.createMimeMessage();
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(user.getEmail());
                    helper.setReplyTo(user.getEmail());
                    helper.setSubject(subject);
                    helper.setText(text);
                    log.info("Sending email to {}", user.getEmail());
                } catch (MessagingException e) {
                    e.printStackTrace();
                    log.error("Fail to send email to {}", user.getEmail());
                }

                javaMailSender.send(message);
            }

        }
    }

    @Data
    public class SendEmailTaskInstance {
        private long id;
        private int categoryId;
        private SendEmailTask sendEmailTask;
        private Future<?> future;

        public SendEmailTaskInstance(long id, int categoryId, SendEmailTask sendEmailTask, Future<?> future) {
            this.id = id;
            this.categoryId = categoryId;
            this.sendEmailTask = sendEmailTask;
            this.future = future;
        }
    }
}
