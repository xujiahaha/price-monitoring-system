package demo.service.impl;

import demo.domain.Category;
import demo.domain.CategoryRepository;
import demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiaxu on 8/4/17.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public String getCategoryTitleById(int categoryId) {
        Category category = this.categoryRepository.getById(categoryId);
        return category.getTitle();
    }
}
