package demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jiaxu on 8/4/17.
 */
@Entity
@Data
@Table(name = "category_seed")
public class CategorySeed {

    @Id
    private int categoryId;
    private String categoryTitle;
    private String searchAlias;
    private int userCount;
    private int priority;
    private String productListUrl;

    public CategorySeed() {
    }

}
