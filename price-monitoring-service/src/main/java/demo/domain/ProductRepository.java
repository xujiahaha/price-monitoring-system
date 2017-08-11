package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by jiaxu on 8/1/17.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByProductId(@Param("productId") String productId);
}
