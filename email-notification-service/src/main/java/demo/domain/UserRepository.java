package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jiaxu on 7/29/17.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User getById(int id);

    User getByEmail(String email);
}
