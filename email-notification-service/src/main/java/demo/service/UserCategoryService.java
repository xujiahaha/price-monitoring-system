package demo.service;

import demo.domain.UserCategory;

import java.util.List;

/**
 * Created by jiaxu on 8/9/17.
 */
public interface UserCategoryService{

    List<UserCategory> getByCategoryId(int categoryId);

}
