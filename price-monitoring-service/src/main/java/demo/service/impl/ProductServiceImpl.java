package demo.service.impl;

import demo.dto.ProductInfo;
import demo.domain.Product;
import demo.domain.ProductRepository;
import demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiaxu on 8/4/17.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(ProductInfo productInfo) {
        return this.productRepository.save(new Product(productInfo));
    }

    @Override
    public Product getProductByProductId(String productId) {
        return this.productRepository.getByProductId(productId);
    }

    @Override
    public Product updateProduct(String productId, double price, double oldPrice) {
        Product product = getProductByProductId(productId);
        product.setOldPrice(oldPrice);
        product.setPrice(price);
        product.setDiscountPercent(getDiscountPercent(product.getOldPrice(), price));
        product.setLastUpdateTime(System.currentTimeMillis());
        return this.productRepository.save(product);
    }

    @Override
    public double getDiscountPercent(double oldPrice, double newPrice) {
        if(oldPrice == 0 || oldPrice <= newPrice) return 0;
        return Math.round((oldPrice - newPrice) / oldPrice * 100);
    }
}
