package demo.service.impl;

import demo.domain.Category;
import demo.domain.CategoryRepositroy;
import demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiaxu on 7/29/17.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepositroy categoryRepositroy;

    @Autowired
    public CategoryServiceImpl(CategoryRepositroy categoryRepositroy) {
        this.categoryRepositroy = categoryRepositroy;
    }

    @Override
    public Category save(Category category) {
        return this.categoryRepositroy.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepositroy.findAll();
    }

}
