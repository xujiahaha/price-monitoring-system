package demo.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
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

    public CategorySeed(Category category) {
        this.categoryId = category.getId();
        this.categoryTitle = category.getTitle();
    }
}
