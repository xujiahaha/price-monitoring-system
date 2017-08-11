package demo.service.impl;

import demo.domain.CategorySeed;
import demo.domain.CategorySeedRepository;
import demo.domain.UserCategoryRepository;
import demo.service.CategorySeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiaxu on 8/4/17.
 */
@Service
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
    public void updateCategoryUserCount() {

    }
}
