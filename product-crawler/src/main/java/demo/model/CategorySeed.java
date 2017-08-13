package demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jiaxu on 8/4/17.
 */
@Data
public class CategorySeed {

    private int categoryId;
    private String categoryTitle;
    private String searchAlias;
    private int userCount;
    private int priority;
    private String productListUrl;

    public CategorySeed() {
    }

    @JsonCreator
    public CategorySeed(@JsonProperty("categoryId") int categoryId,
                        @JsonProperty("categoryTitle")String categoryTitle,
                        @JsonProperty("searchAlias")String searchAlias,
                        @JsonProperty("userCount")int userCount,
                        @JsonProperty("priority")int priority,
                        @JsonProperty("productListUrl")String productListUrl) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.searchAlias = searchAlias;
        this.userCount = userCount;
        this.priority = priority;
        this.productListUrl = productListUrl;
    }
}
