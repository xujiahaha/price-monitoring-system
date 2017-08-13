package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jiaxu on 8/11/17.
 */
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

    List<UserCategory> getByUserId(int userId);

}
