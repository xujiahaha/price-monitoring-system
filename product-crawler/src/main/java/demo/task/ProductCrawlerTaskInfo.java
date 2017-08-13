package demo.task;

import demo.model.CategorySeed;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.Future;

/**
 * Created by jiaxu on 8/12/17.
 */
@Data
@AllArgsConstructor
public class ProductCrawlerTaskInfo {

    private long instanceId;
    private int categoryId;
    private String categoryTitle;
    private Future<?> productCrawlerTask;

    @Override
    public String toString() {
        return "ProductCrawlerTaskInfo{" +
                "instanceId=" + instanceId +
                ", categoryId=" + categoryId +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", productCrawlerTask=" + productCrawlerTask +
                '}';
    }
}
