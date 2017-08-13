package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by jiaxu on 7/29/17.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User getById(@Param("id") int id);

    User getByEmail(@Param("email") String email);

}
