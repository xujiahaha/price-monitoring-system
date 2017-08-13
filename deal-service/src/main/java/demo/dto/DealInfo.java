package demo.dto;

import demo.domain.Product;
import lombok.Data;

/**
 * Created by jiaxu on 8/8/17.
 */
@Data
public class DealInfo {

    private String title;
    private int categoryId;
    private String brand;
    private String detailUrl;
    private String thumbnail;
    private double oldPrice;
    private double curPrice;
    private double discountPercent;

    public DealInfo() {
    }

    public DealInfo(Product product) {
        this.title = product.getTitle();
        this.categoryId = product.getCategoryId();
        this.brand = product.getBrand();
        this.detailUrl = product.getDetailUrl();
        this.thumbnail = product.getThumbnail();
        this.oldPrice = product.getOldPrice();
        this.curPrice = product.getPrice();
        this.discountPercent = product.getDiscountPercent();
    }

}
