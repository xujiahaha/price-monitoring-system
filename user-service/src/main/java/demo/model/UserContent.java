package demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by jiaxu on 8/11/17.
 */
@Data
public class UserContent {

    private String email;
    private List<Integer> subscriptions;

    public UserContent () {}

    @JsonCreator
    public UserContent(@JsonProperty("email") String email,
                       @JsonProperty("subscriptions") List<Integer> subscriptions) {
        this.email = email;
        this.subscriptions = subscriptions;
    }
}
