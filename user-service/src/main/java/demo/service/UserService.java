package demo.service;

import demo.domain.User;

import java.util.List;

/**
 * Created by jiaxu on 7/29/17.
 */
public interface UserService {

    User save(String email);

    List<User> getAllUsers();

    User getUserById(int id);

}
