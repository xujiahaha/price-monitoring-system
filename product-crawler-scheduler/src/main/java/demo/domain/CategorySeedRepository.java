package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface CategorySeedRepository extends JpaRepository<CategorySeed, Integer> {

    CategorySeed findByCategoryId(@Param("id") int id);

    List<CategorySeed> findByPriority(int priority);
}
