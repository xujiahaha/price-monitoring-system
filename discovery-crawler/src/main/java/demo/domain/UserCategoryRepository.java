package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface UserCategoryRepository extends JpaRepository<UserCategory, Integer> {

    @Query(value = "SELECT category_id, COUNT(user_id) AS user_count FROM user_category GROUP BY category_id ORDER BY user_count DESC", nativeQuery = true)
    List<Object[]> getCategoryUserCount();

}
