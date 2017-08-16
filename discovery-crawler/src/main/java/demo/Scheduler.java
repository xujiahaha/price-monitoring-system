package demo;

import demo.service.CategorySeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by jiaxu on 8/11/17.
 */
@Component
@Slf4j
public class Scheduler {

    private CategorySeedService categorySeedService;

    @Autowired
    public Scheduler(CategorySeedService categorySeedService) {
        this.categorySeedService = categorySeedService;
    }

//    @Scheduled(cron = "0 6 * * *") // update priority every day at 6am
    @Scheduled(fixedRate = 10000)  // for demo purpose
    public void updateCategoryPriority(){
        this.categorySeedService.updateCategoryPriority();
    }

}
