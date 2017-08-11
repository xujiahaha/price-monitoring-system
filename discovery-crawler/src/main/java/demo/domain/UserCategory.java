package demo.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jiaxu on 8/4/17.
 */
@Entity
@Data
@Table(name = "user_category")
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
public class UserCategory {

    @Id
    private int userId;
    private int categoryId;

}

