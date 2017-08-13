package demo.service.impl;

import demo.domain.CategorySeed;
import demo.domain.CategorySeedRepository;
import demo.service.CategorySeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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

    @Override
    public List<CategorySeed> getCategoryByPriority(int priority) {
        return this.categorySeedRepository.findByPriority(priority);
    }

}
