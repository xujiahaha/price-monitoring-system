package demo.service.impl;

import demo.domain.UserCategory;
import demo.domain.UserCategoryRepository;
import demo.service.UserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaxu on 8/11/17.
 */
@Service
public class UserCategoryServiceImpl implements UserCategoryService{

    private UserCategoryRepository userCategoryRepository;

    @Autowired
    public UserCategoryServiceImpl(UserCategoryRepository userCategoryRepository) {
        this.userCategoryRepository = userCategoryRepository;
    }

    @Override
    public void save(int userId, List<Integer> subscriptions) {
        for(Integer categoryId : subscriptions) {
            UserCategory userCategory = new UserCategory(userId, categoryId);
            this.userCategoryRepository.save(userCategory);
        }
    }

    @Override
    public List<Integer> getUserSubscription(int userId) {
        List<Integer> subscription = new ArrayList<>();
        List<UserCategory> userCategoryList = this.userCategoryRepository.getByUserId(userId);
        for(UserCategory userCategory : userCategoryList) {
            subscription.add(userCategory.getCategoryId());
        }
        return subscription;
    }
}
