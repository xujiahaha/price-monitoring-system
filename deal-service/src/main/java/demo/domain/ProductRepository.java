package demo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jiaxu on 8/1/17.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product getProductByProductId(String productId);

    Page<Product> findByDiscountPercentGreaterThan(double minDiscount, Pageable pageable);

    Page<Product> findByCategoryIdAndDiscountPercentGreaterThan(int categoryId, double minDiscount, Pageable pageable);

    @Query(value = "SELECT * FROM product WHERE title REGEXP ?1 AND discount_percent > 0 ORDER BY ?2", nativeQuery = true)
    List<Product> findByTitleRegex(String regex, String sortBy);
}
