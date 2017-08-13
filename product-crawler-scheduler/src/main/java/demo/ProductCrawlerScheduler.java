package demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import demo.domain.CategorySeed;
import demo.service.CategorySeedDistributionService;
import demo.service.CategorySeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jiaxu on 8/12/17.
 */
@Component
@Slf4j
public class ProductCrawlerScheduler {

    private CategorySeedService categorySeedService;
    private CategorySeedDistributionService distributionService;

    @Autowired
    public ProductCrawlerScheduler(CategorySeedService categorySeedService, CategorySeedDistributionService distributionService) {
        this.categorySeedService = categorySeedService;
        this.distributionService = distributionService;
    }

    private static final String[] CRAWLER_URL = {"http://localhost:8001/api/crawler",
            "http://localhost:8002/api/crawler","http://localhost:8003/api/crawler"};
    private int index = 0;


    @Scheduled(fixedRate = 10000)
    public void schedule(){
        List<CategorySeed> categorySeeds = getCategorySeeds(3);
        for(CategorySeed categorySeed : categorySeeds) {
            boolean success;
            do {
                if(index == CRAWLER_URL.length) {
                    index = 0;
                }
                success = this.distributionService.sendCategorySeed(CRAWLER_URL[index++], categorySeed);
            } while (!success);
        }
    }

    private List<CategorySeed> getCategorySeeds(int priority) {
        return this.categorySeedService.getCategoryByPriority(priority);
    }
}
