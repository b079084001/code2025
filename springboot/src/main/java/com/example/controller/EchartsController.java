package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.service.CategoryService;
import com.example.service.IntroductionService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Resource
    CategoryService categoryService;
    @Resource
    IntroductionService introductionService;
    @Resource
    UserService userService;

    @GetMapping("/pie")
    public Result pie() {
        List<Map<String, Object>> list = new ArrayList<>();
        //查询出所有分类
        List<Category> categories = categoryService.selectAll(new Category());
        //查询出所有帖子信息
        List<Introduction> introductions = introductionService.selectAll(new Introduction());
        for (Category category : categories) {
//            long count = introductions.stream().filter(x -> ObjectUtil.isNotEmpty(x.getCategoryId()) && x.getCategoryId().equals(category.getId())).count();
            long count = introductions.stream().filter(x -> category.getId().equals(x.getCategoryId())).count();
            Map<String, Object> map = new HashMap<>();
            map.put("name", category.getTitle());
            map.put("value", count);
            list.add(map);
        }
        return Result.success(list);
    }

//    @GetMapping("/bar")
//    public Result bar() {
//        Map<String, Object> ResultMap = new HashMap<>();
//        List<String> xList = new ArrayList<>();
//        List<Long> yList = new ArrayList<>();
//
//        //查询所有的用户
//        List<User> users = userService.selectAll(new User());
//        //查询出所有帖子信息
//        List<Introduction> introductions = introductionService.selectAll(new Introduction());
//        for (User user : users) {
//            long count = introductions.stream().filter(x -> user.getId().equals(x.getUserId())).count();
//            xList.add(user.getName());
//            yList.add(count);
//        }
//
//        ResultMap.put("xAxis", xList);
//        ResultMap.put("yAxis", yList);
//        return Result.success(ResultMap);
//    }

    @GetMapping("/bar")
    public Result bar() {
        Map<String, Object> ResultMap = new HashMap<>();
        List<String> xList = new ArrayList<>();
        List<Long> yList = new ArrayList<>();

        Map<String, Long> map = new HashMap<>();

        //查询所有的用户
        List<User> users = userService.selectAll(new User());
        //查询出所有帖子信息
        List<Introduction> introductions = introductionService.selectAll(new Introduction());
        for (User user : users) {
            long count = introductions.stream().filter(x -> user.getId().equals(x.getUserId())).count();
            map.put(user.getName(), count);
        }
        //对map进行排序，按照value来倒序
        LinkedHashMap<String, Long> collectMap = map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (String key : collectMap.keySet()) {
            xList.add(key);
            yList.add(collectMap.get(key));
        }

        //Top几的问题，截断
        if (xList.size() > 3 && yList.size() > 3) {
            xList = xList.subList(0, 3);
            yList = yList.subList(0, 3);
        }

        ResultMap.put("xAxis", xList);
        ResultMap.put("yAxis", yList);
        return Result.success(ResultMap);
    }


    @GetMapping("/line")
    public Result line() {
        Map<String, Object> ResultMap = new HashMap<>();
        List<Long> yList = new ArrayList<>();

        //获取最近多少天的年月日
        Date today = new Date();
        DateTime start = DateUtil.offsetDay(today, -7);
        List<String> xList = DateUtil.rangeToList(start, today, DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).toList();

        //查询出所有帖子信息
        List<Introduction> introductions = introductionService.selectAll(new Introduction());
        for (String day : xList) {
            long count = introductions.stream().filter(x -> ObjectUtil.isNotEmpty(x.getTime()) && x.getTime().contains(day)).count();
            yList.add(count);
        }

        ResultMap.put("xAxis", xList);
        ResultMap.put("yAxis", yList);
        return Result.success(ResultMap);
    }
}
