package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
