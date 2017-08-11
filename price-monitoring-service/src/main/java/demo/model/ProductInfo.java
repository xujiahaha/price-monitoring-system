package demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jiaxu on 8/5/17.
 */
@Getter
@Setter
public class ProductInfo {

    private String productId;
    private int categoryId; // for now, don't consider the case that one product belongs to multiple categories.
    private String title;
    private String brand;
    private String detailUrl;
    private String thumbnail;
    private double price;

    public ProductInfo() {
    }

    public ProductInfo(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", brand='" + brand + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", price=" + price +
                '}';
    }
}
