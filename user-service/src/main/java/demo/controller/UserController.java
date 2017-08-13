package demo.controller;

import demo.domain.User;
import demo.model.UserContent;
import demo.service.UserCategoryService;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiaxu on 7/29/17.
 */
@RestController
public class UserController {

    private UserService userService;
    private UserCategoryService userCategoryService;

    @Autowired
    public UserController(UserService userService, UserCategoryService userCategoryService) {
        this.userService = userService;
        this.userCategoryService = userCategoryService;
    }

    @RequestMapping(value = "/subscriptions", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String subscribe(@RequestBody UserContent userContent) {
        User user = this.userService.save(userContent.getEmail());
        this.userCategoryService.save(user.getId(), userContent.getSubscriptions());
        System.out.println("Successfully created user:");
        System.out.println("userId: " + user.getId());
        System.out.println("username: " + user.getUsername());
        System.out.println("email: " + user.getEmail());
        System.out.println("subscriptions: " + userContent.getSubscriptions().toString());
        return "success";
    }
}
