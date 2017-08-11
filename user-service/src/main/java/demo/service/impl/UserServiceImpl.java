package demo.service.impl;

import demo.domain.Category;
import demo.domain.User;
import demo.domain.UserRepository;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void saveUser(User user) {
        User oldUser = this.userRepository.findByEmail(user.getEmail());
        if(oldUser == null) {
            this.userRepository.save(user);
        }
    }

    @Override
    public User updateUser(int id, User user) {
        User oldUser = getUserById(id);
        if(oldUser == null) return null;
        oldUser.setUsername(user.getUsername());
        oldUser.setEmail(user.getEmail());
        oldUser.setEmailNotification(user.isEmailNotification());
        oldUser.setLastUpdateTime(System.currentTimeMillis());
        return this.userRepository.save(oldUser);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Set<Category> subscribeCategories(int id, List<Category> categories) {
        User oldUser = getUserById(id);
        oldUser.setCategories(new HashSet<>(categories));
        oldUser.setLastUpdateTime(System.currentTimeMillis());
        this.userRepository.save(oldUser);
        return oldUser.getCategories();
    }

    @Override
    public Set<Category> getCategoriesByUserId(int id) {
        return this.userRepository.findById(id).getCategories();
    }
}
