package demo.controller;

import demo.domain.Category;
import demo.domain.User;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by jiaxu on 7/29/17.
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ------------ create a user ----------------------------------------------------------

    @RequestMapping(method = RequestMethod.POST)
    public void saveUser(@RequestBody User user) {
        this.userService.saveUser(user);
    }

    @RequestMapping(value = "/{id}/subscriptions", method = RequestMethod.POST)
    @ResponseBody
    public Set<Category> subscribeCategories(@PathVariable("id") int id,
                                   @RequestBody List<Category> categories) {
        return this.userService.subscribeCategories(id, categories);
    }

    /** ----------- PUT -----------------------*/

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public User updateUser(@PathVariable("id") int id,
                           @RequestBody User user) {
        return this.userService.updateUser(id, user);
    }

    /** ----------- GET -----------------------*/

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAll() {
        return this.userService.getAllUsers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserById(@PathVariable("id") int id) {
        return this.userService.getUserById(id);
    }

    @RequestMapping(value = "/{id}/subscriptions", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getCategoriesByUserId(@PathVariable("id") int id) {
        if(this.userService.getUserById(id) == null) return ResponseEntity.badRequest().body("User does not exist.");
        return ResponseEntity.ok(this.userService.getCategoriesByUserId(id));
    }

}
