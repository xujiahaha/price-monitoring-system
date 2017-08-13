package demo.service;

import demo.dto.ProductInfo;
import demo.domain.Product;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface ProductService {

    Product saveProduct(ProductInfo productInfo);

    Product updateProduct(String productId, double price, double oldPrice);

    Product getProductByProductId(String productId);

    double getDiscountPercent(double oldPrice, double newPrice);
}
