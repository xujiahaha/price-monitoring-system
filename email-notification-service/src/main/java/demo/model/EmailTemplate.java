package demo.model;

import lombok.Data;

import java.util.List;

/**
 * Created by jiaxu on 8/13/17.
 */
@Data
public class EmailTemplate {

    private int categoryId;
    private String subject;
    private String content;

    public EmailTemplate() {

    }

    public EmailTemplate(int categoryId) {
        this.categoryId = categoryId;
    }


}
