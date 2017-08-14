package demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "category")
public class Category {

    @Id
    private int id;
    private String title;

    public Category() {
    }

    public Category(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
