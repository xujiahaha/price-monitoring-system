package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface CategorySeedRepository extends JpaRepository<CategorySeed, Integer> {

    CategorySeed getByCategoryId(int categoryId);

}
