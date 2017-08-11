package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by jiaxu on 7/27/17.
 */
@Entity
@Getter
@Setter
@Table(name = "user", indexes = {@Index(name = "email_index",  columnList="email", unique = true)})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    @NonNull
    private String email;
    private boolean emailNotification;
    private long creationTime;
    private long lastUpdateTime;
    private long lastNotifyTime;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_category",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories;

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("emailNotification") Boolean emailNotification) {
        this.email = email;
        this.username = getUsernameFromEmail(email);
        this.emailNotification = emailNotification;
        this.creationTime = System.currentTimeMillis();
        this.lastUpdateTime = System.currentTimeMillis();
    }

    private String getUsernameFromEmail(String email) {
        int index = email.indexOf('@');
        return email.substring(0, index);
    }

}
