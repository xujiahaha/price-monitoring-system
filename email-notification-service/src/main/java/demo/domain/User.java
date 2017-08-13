package demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by jiaxu on 7/27/17.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
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

    public User() {
    }

    private String getUsernameFromEmail(String email) {
        int index = email.indexOf('@');
        return email.substring(0, index);
    }

}
