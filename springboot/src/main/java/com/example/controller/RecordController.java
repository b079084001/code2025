package com.example.controller;

import com.example.common.Result;
import com.example.entity.Record;
import com.example.service.RecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    private RecordService recordService;

    @PostMapping("/add")
    public Result add(@RequestBody Record record) {  //@RequestBody接收前端传来的json参数
        recordService.add(record);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Record record) {
        recordService.update(record);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@PathVariable接收前端传来的路径参数
        recordService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Record record) {
        List<Record> recordList = recordService.selectAll(record);
        return Result.success(recordList);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum, //@RequestParam接收前端传来的form表单参数
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Record record) {
        PageInfo<Record> lst = recordService.selectPage(pageNum, pageSize, record);
        return Result.success(lst); //返回分页对象
    }
}
