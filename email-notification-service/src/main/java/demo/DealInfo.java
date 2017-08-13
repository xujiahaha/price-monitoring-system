package demo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by jiaxu on 8/8/17.
 */
@Data
@AllArgsConstructor
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

}
