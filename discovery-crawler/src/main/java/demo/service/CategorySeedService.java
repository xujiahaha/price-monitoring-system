package demo.service;

import demo.domain.CategorySeed;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface CategorySeedService {

    void saveCategorySeed(CategorySeed categorySeed);

    void updateCategoryPriority();
}
