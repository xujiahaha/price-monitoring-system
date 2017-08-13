package demo.service.impl;

import demo.domain.UserCategory;
import demo.domain.UserCategoryRepository;
import demo.service.UserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiaxu on 8/9/17.
 */
@Service
public class UserCategoryServiceImpl implements UserCategoryService{

    private UserCategoryRepository userCategoryRepository;

    @Autowired
    public UserCategoryServiceImpl(UserCategoryRepository userCategoryRepository) {
        this.userCategoryRepository = userCategoryRepository;
    }

    @Override
    public List<UserCategory> getByCategoryId(int categoryId) {
        return this.userCategoryRepository.getByCategoryId(categoryId);
    }
}
