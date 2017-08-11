package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface UserCategoryRepository extends JpaRepository<UserCategory, Integer> {

    @Query(value = "select category_id, count(user_id) as user_count from user_category group by category_id", nativeQuery = true)
    List<Object[]> getCategoryUserCount();

}
