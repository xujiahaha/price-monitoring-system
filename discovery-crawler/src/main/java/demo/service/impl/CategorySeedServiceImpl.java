package demo.service.impl;

import demo.domain.CategorySeed;
import demo.domain.CategorySeedRepository;
import demo.domain.UserCategoryRepository;
import demo.service.CategorySeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by jiaxu on 8/4/17.
 */
@Service
@Slf4j
public class CategorySeedServiceImpl implements CategorySeedService {

    private CategorySeedRepository categorySeedRepository;

    private UserCategoryRepository userCategoryRepository;

    @Autowired
    public CategorySeedServiceImpl(CategorySeedRepository categorySeedRepository, UserCategoryRepository userCategoryRepository) {
        this.categorySeedRepository = categorySeedRepository;
        this.userCategoryRepository = userCategoryRepository;
    }


    @Override
    public void saveCategorySeed(CategorySeed categorySeed) {
        this.categorySeedRepository.save(categorySeed);
    }

    @Override
    public void updateCategoryPriority() {
        List<Object[]> objects = this.userCategoryRepository.getCategoryUserCount();
        int total = objects.size();
        for(int i = 0; i < total; i++) {
            Integer categoryId = Integer.parseInt(objects.get(i)[0].toString());
            Integer userCount = Integer.parseInt(objects.get(i)[1].toString());
            updateUserCountAndPriority(categoryId, userCount);
            System.out.println("categoryId: " + categoryId + ", userCount: " + userCount);
        }
        log.info("Category priority has been updated successfully on {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }

    private void updateUserCountAndPriority(int categoryId, int userCount) {
        CategorySeed categorySeed = this.categorySeedRepository.getByCategoryId(categoryId);
        categorySeed.setUserCount(userCount);
        /*
        * TODO: dynamically update userCount threshold for prioritize
        * */
        if(userCount > 100) {
            categorySeed.setPriority(1);
        }else if (userCount > 50 && userCount <= 100) {
            categorySeed.setPriority(2);
        }else {
            categorySeed.setPriority(3);
        }
        this.categorySeedRepository.save(categorySeed);
    }
}
