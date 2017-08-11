package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by jiaxu on 7/27/17.
 */
@Entity
@Getter
@Setter
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue
    private int id;
    private String title;
    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<User> users;

    public Category() {
    }

    @JsonCreator
    public Category(@JsonProperty("title") String title) {
        this.title = title;
    }
}
