package demo.service;

import java.util.List;

/**
 * Created by jiaxu on 8/11/17.
 */
public interface UserCategoryService {

    void save(int userId, List<Integer> categoryList);

    List<Integer> getUserSubscription(int userId);
}
