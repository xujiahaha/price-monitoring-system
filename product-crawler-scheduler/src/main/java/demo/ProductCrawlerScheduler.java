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


    private int high = 1;
    private int med = 1;
    private int low = 1;


    @Scheduled(fixedRate = 10000)
    public void scheduleHighPriority(){
        log.info("Scheduling high priority category for {} time", high++);
        List<CategorySeed> categorySeeds = getCategorySeeds(1);
        for(CategorySeed categorySeed : categorySeeds) {
            distribute(categorySeed);
        }
    }

    @Scheduled(fixedRate = 20000)
    public void scheduleMedPriority(){
        log.info("Scheduling med priority category for {} time", med++);
        List<CategorySeed> categorySeeds = getCategorySeeds(2);
        for(CategorySeed categorySeed : categorySeeds) {
            distribute(categorySeed);
        }
    }

    @Scheduled(fixedRate = 30000)
    public void scheduleLowPriority(){
        log.info("Scheduling low priority category for {} time", low++);
        List<CategorySeed> categorySeeds = getCategorySeeds(3);
        for(CategorySeed categorySeed : categorySeeds) {
            distribute(categorySeed);
        }
    }

    private void distribute(CategorySeed categorySeed) {
        boolean success;
        do {
            if(index == CRAWLER_URL.length) {
                index = 0;
            }
            success = this.distributionService.sendCategorySeed(CRAWLER_URL[index++], categorySeed);
        } while (!success);
    }

    private List<CategorySeed> getCategorySeeds(int priority) {
        return this.categorySeedService.getCategoryByPriority(priority);
    }
}
