package demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * Created by jiaxu on 8/5/17.
 */
@Data
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

    /*
    * This is only for testing
    * */
    @JsonCreator
    public ProductInfo(@JsonProperty("productId") String productId,
                       @JsonProperty("categoryId") int categoryId,
                       @JsonProperty("title") String productTitle,
                       @JsonProperty("price") double price,
                       @JsonProperty("detailUrl") String detailUrl) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.title = productTitle;
        this.price = price;
        this.detailUrl = detailUrl;
    }
}
