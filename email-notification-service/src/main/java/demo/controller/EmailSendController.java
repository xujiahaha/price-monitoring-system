package demo.controller;

import demo.service.EmailSendService;
import demo.service.impl.EmailSendServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jiaxu on 8/14/17.
 */
@RestController
@RequestMapping(value = "/api")
public class EmailSendController {

    @Autowired
    private EmailSendService emailSendService;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public List<EmailSendServiceImpl.SendEmailTaskInstance> status() {
        return this.emailSendService.getFutureTask();
    }
}
