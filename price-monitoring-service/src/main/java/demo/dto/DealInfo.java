package demo.dto;

import demo.domain.Product;
import lombok.Data;

/**
 * Created by jiaxu on 8/8/17.
 */
@Data
public class DealInfo {

    private String categoryId;
    private String title;
    private String brand;
    private String detailUrl;
    private String thumbnail;
    private double oldPrice;
    private double curPrice;
    private double discountPercent;

    public DealInfo() {
    }

    public DealInfo(Product product) {
        this.categoryId = product.getProductId();
        this.title = product.getTitle();
        this.brand = product.getBrand();
        this.detailUrl = product.getDetailUrl();
        this.thumbnail = product.getThumbnail();
        this.oldPrice = product.getOldPrice();
        this.curPrice = product.getPrice();
        this.discountPercent = product.getDiscountPercent();
    }

}
