package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jiaxu on 8/9/17.
 */
public interface UserCategoryRepository extends JpaRepository<UserCategory, Integer> {

    List<UserCategory> getUserCategoriesByCategoryId(int categoryId);

}
