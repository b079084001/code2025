package com.example.controller;

import com.example.common.Result;
import com.example.entity.Book;
import com.example.service.BookService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookService bookService;

    @PostMapping("/add")
    public Result add(@RequestBody Book book) {  //@RequestBody接收前端传来的json参数
        bookService.add(book);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Book book) {
        bookService.update(book);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@PathVariable接收前端传来的路径参数
        bookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Book book) {
        List<Book> bookList = bookService.selectAll(book);
        return Result.success(bookList);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum, //@RequestParam接收前端传来的form表单参数
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Book book) {
        PageInfo<Book> lst = bookService.selectPage(pageNum, pageSize, book);
        return Result.success(lst); //返回分页对象
    }
}
