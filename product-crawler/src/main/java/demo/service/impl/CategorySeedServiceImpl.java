package demo.service.impl;

import demo.domain.CategorySeed;
import demo.domain.CategorySeedRepository;
import demo.service.CategorySeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiaxu on 8/4/17.
 */
@Service
public class CategorySeedServiceImpl implements CategorySeedService {

    private CategorySeedRepository categorySeedRepository;


    @Autowired
    public CategorySeedServiceImpl(CategorySeedRepository categorySeedRepository) {
        this.categorySeedRepository = categorySeedRepository;
    }

    @Override
    public CategorySeed getSeedByCategoryId(int id) {
        return this.categorySeedRepository.findByCategoryId(id);
    }

    @Override
    public List<CategorySeed> getAllCategorySeeds() {
        return this.categorySeedRepository.findAll();
    }
}
