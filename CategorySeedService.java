package demo.service;

import demo.domain.CategorySeed;

import java.util.List;

/**
 * Created by jiaxu on 8/4/17.
 */
public interface CategorySeedService {

    CategorySeed getSeedByCategoryId(int id);

    List<CategorySeed> getAllCategorySeeds();

}
