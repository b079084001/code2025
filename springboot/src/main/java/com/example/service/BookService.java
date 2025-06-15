package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Book;
import com.example.mapper.BookMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Resource
    BookMapper bookMapper;


    public void add(Book book) {
        bookMapper.insert(book);
    }

    public void update(Book book) {
        bookMapper.updateById(book);
    }

    public void deleteById(Integer id) {
        bookMapper.deleteById(id);
    }

    public List<Book> selectAll(Book book) {
        return bookMapper.selectAll(book);
    }

    public PageInfo<Book> selectPage(Integer pageNum, Integer pageSize, Book book) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Book> list = bookMapper.selectAll(book);
        return PageInfo.of(list);
    }
}
