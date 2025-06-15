package com.example.controller;

import com.example.common.Result;
import com.example.entity.Apply;
import com.example.service.ApplyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Resource
    private ApplyService applyService;

    @PostMapping("/add")
    public Result add(@RequestBody Apply apply) {  //@RequestBody接收前端传来的json参数
        applyService.add(apply);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Apply apply) {
        applyService.update(apply);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@PathVariable接收前端传来的路径参数
        applyService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Apply apply) {
        List<Apply> applyList = applyService.selectAll(apply);
        return Result.success(applyList);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum, //@RequestParam接收前端传来的form表单参数
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Apply apply) {
        PageInfo<Apply> lst = applyService.selectPage(pageNum, pageSize, apply);
        return Result.success(lst); //返回分页对象
    }
}
