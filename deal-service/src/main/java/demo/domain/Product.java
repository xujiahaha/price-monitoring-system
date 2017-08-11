package demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by jiaxu on 8/1/17.
 */
@Entity
@Table(name = "product", indexes = {@Index(name = "product_id_index",  columnList="productId", unique = true)})
@Data
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

}
