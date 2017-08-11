package demo.service;

import demo.domain.Category;

import java.util.List;

/**
 * Created by jiaxu on 7/29/17.
 */
public interface CategoryService {

    Category save(Category category);

    List<Category> getAllCategories();
}
