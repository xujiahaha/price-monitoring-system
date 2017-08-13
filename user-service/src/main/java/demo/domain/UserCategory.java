package demo.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by jiaxu on 8/11/17.
 */
@Entity
@Data
@Table(name = "user_category")
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int userId;
    private int categoryId;

    public UserCategory() {
    }

    public UserCategory(int userId, int categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }
}
