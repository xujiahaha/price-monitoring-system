package demo.domain;

import demo.model.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by jiaxu on 8/1/17.
 */
@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
public class Product {

    @Id
    private String productId;
    private int categoryId; // for now, don't consider the case that one product belongs to multiple categories.
    private String title;
    private String brand;
    @Column(length = 1024)
    private String detailUrl;
    @Column(length = 1024)
    private String thumbnail;
    private double oldPrice;
    private double price;
    private double discountPercent;
    private long creationTime;
    private long lastUpdateTime;

    public Product() {
    }

    public Product(ProductInfo productInfo) {
        this.productId = productInfo.getProductId();
        this.categoryId = productInfo.getCategoryId();
        this.title = productInfo.getTitle();
        this.brand = productInfo.getBrand();
        this.detailUrl = productInfo.getDetailUrl();
        this.thumbnail = productInfo.getThumbnail();
        this.price = productInfo.getPrice();
        this.creationTime = System.currentTimeMillis();
        this.lastUpdateTime = System.currentTimeMillis();
    }

}
