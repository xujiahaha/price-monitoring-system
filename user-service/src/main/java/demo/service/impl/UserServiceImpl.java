package demo.service.impl;

import demo.domain.User;
import demo.domain.UserRepository;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Created by jiaxu on 7/29/17.
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(String email) {
        User oldUser = this.userRepository.getByEmail(email);
        if(oldUser == null) {
            User user = new User(email);
            return this.userRepository.save(user);
        }
        return oldUser;
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return this.userRepository.getById(id);
    }
}
