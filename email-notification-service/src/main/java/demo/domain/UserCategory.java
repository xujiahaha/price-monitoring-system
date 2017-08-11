package demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by jiaxu on 8/9/17.
 */
@Entity
@Data
@Table(name = "user_category", indexes = {@Index(name = "category_id_index",  columnList="categoryId", unique = true)})
public class UserCategory {

    @Id
    private int userId;
    private int categoryId;

    public UserCategory() {
    }

    public UserCategory(int userId, int categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }
}
