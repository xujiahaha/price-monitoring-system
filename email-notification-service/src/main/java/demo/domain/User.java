package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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

    public User(String username, String email, long creationTime, long lastNotifyTime) {
        this.username = username;
        this.email = email;
        this.creationTime = creationTime;
        this.lastNotifyTime = lastNotifyTime;
    }

    private String getUsernameFromEmail(String email) {
        int index = email.indexOf('@');
        return email.substring(0, index);
    }

}
