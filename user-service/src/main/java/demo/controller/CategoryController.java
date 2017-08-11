package demo.controller;

import demo.domain.Category;
import demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jiaxu on 7/29/17.
 */
@RestController
@RequestMapping(value = "categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Category saveCategory(@RequestBody Category category) {
        return this.categoryService.save(category);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Category> getAll() {
        return this.categoryService.getAllCategories();
    }
}
