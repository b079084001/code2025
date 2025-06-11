package com.example.mapper;

import com.example.entity.Category;
import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper {
    List<Category> selectAll(Category category);

    void insert(Category category);

    void updateById(Category category);

    @Delete("delete from `category` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `category` where id = #{id}")
    Category selectById(Integer categoryId);
}
