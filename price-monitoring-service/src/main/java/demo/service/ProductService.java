package demo.service;

import demo.model.ProductInfo;
import demo.domain.Product;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface ProductService {

    Product saveProduct(ProductInfo productInfo);

    Product updateProduct(ProductInfo productInfo);

    Product getProductByProductId(String productId);

    double getDiscountPercent(double oldPrice, double newPrice);
}
