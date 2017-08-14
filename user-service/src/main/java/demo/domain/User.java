package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

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
    private long creationTime;
    private long lastNotifyTime;

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("email") String email) {
        this.email = email;
        this.username = getUsernameFromEmail(email);
        this.creationTime = System.currentTimeMillis();
    }

    private String getUsernameFromEmail(String email) {
        int index = email.indexOf('@');
        return email.substring(0, index);
    }

}
