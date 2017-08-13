package demo.service;

import demo.domain.CategorySeed;

import java.util.List;


public interface CategorySeedService {

    CategorySeed getSeedByCategoryId(int id);

    List<CategorySeed> getAllCategorySeeds();

    List<CategorySeed> getCategoryByPriority(int priority);

}
