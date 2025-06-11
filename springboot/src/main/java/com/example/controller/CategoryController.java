package com.example.controller;

import com.example.common.Result;
import com.example.entity.Category;
import com.example.service.CategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @PostMapping("/add")
    public Result add(@RequestBody Category category) {  //@RequestBody接收前端传来的json参数
        categoryService.add(category);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Category category) {
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@PathVariable接收前端传来的路径参数
        categoryService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Category category) {
        List<Category> categoryList = categoryService.selectAll(category);
        return Result.success(categoryList);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum, //@RequestParam接收前端传来的form表单参数
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Category category) {
        PageInfo<Category> lst = categoryService.selectPage(pageNum, pageSize, category);
        return Result.success(lst); //返回分页对象
    }
}
