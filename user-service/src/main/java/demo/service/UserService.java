package demo.service;

import demo.domain.Category;
import demo.domain.User;

import java.util.List;
import java.util.Set;

/**
 * Created by jiaxu on 7/29/17.
 */
public interface UserService {

    void saveUser(User user);

    User updateUser(int id, User user);

    List<User> getAllUsers();

    User getUserById(int id);

    Set<Category> subscribeCategories(int id, List<Category> categories);

    Set<Category> getCategoriesByUserId(int id);

}
