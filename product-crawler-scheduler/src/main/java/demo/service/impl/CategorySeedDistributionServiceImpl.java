package demo.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import demo.domain.CategorySeed;
import demo.service.CategorySeedDistributionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jiaxu on 8/12/17.
 */
@Slf4j
@Service
public class CategorySeedDistributionServiceImpl implements CategorySeedDistributionService{

    private RestTemplate restTemplate;

    @Autowired
    public CategorySeedDistributionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "sendRequestToCrawlerFallback")
    @Override
    public boolean sendCategorySeed(String crawlerLocation, CategorySeed categorySeed) {
        log.info("sending {} request to {}", categorySeed.getCategoryTitle(), crawlerLocation);
        this.restTemplate.postForLocation(crawlerLocation, categorySeed);
        return true;
    }

    public boolean sendRequestToCrawlerFallback(String crawlerLocation, CategorySeed categorySeed) {
        log.error("Hystrix Fallback Method. Unable to send {} request to {}", categorySeed.getCategoryTitle(), crawlerLocation);
        return false;
    }
}
