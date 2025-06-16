package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.example.entity.Account;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.mapper.CategoryMapper;
import com.example.mapper.IntroductionMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntroductionService {

    @Resource
    IntroductionMapper introductionMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    UserMapper userMapper;

    public void add(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        introduction.setUserId(currentUser.getId());

        introduction.setTime(DateUtil.now());
        introductionMapper.insert(introduction);
    }

    public void update(Introduction introduction) {
        introductionMapper.updateById(introduction);
    }

    public void deleteById(Integer id) {
        introductionMapper.deleteById(id);
    }

    public List<Introduction> selectAll(Introduction introduction) {
//        return introductionMapper.selectAll(introduction);
        List<Introduction> introductions = introductionMapper.selectAll(introduction);
        for (Introduction dbintroduction : introductions) {
            dbintroduction.setDescription(HtmlUtil.cleanHtmlTag(dbintroduction.getContent()));
        }
        return introductions;
    }

    public PageInfo<Introduction> selectPage(Integer pageNum, Integer pageSize, Introduction introduction) {
        //查询之前查看用户
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            introduction.setUserId(currentUser.getId());
        }

        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Introduction> list = introductionMapper.selectAll(introduction);

//        //list存储了旅游攻略的原始数据（只有categoryId）
//        for (Introduction dbIntroduction : list) {
//            //先拿到categoryId
//            Integer categoryId = dbIntroduction.getCategoryId();
//            //先拿到userId
//            Integer userId = dbIntroduction.getUserId();
//            //通过categoryId从category表里通过主键查询分类数据
//            Category category = categoryMapper.selectById(categoryId);
//            //通过userId从user表里通过主键查询分类数据
//            User user = userMapper.selectById(userId.toString());
//            if (ObjectUtil.isNotEmpty(category)) {
//                //把category表的title传给categoryTitle
//                dbIntroduction.setCategoryTitle(category.getTitle());
//            }
//            if (ObjectUtil.isNotEmpty(user)) {
//                //把user表的name传给userName
//                dbIntroduction.setUserName(user.getName());
//            }
//        }
        return PageInfo.of(list);
    }

    public Introduction selectById(Integer id) {
        Introduction dbIntroduction = introductionMapper.selectById(id);
        // 先拿到categoryId
        Integer categoryId = dbIntroduction.getCategoryId();
        //先拿到userId
        Integer userId = dbIntroduction.getUserId();
        //通过categoryId从category表里通过主键查询分类数据
        Category category = categoryMapper.selectById(categoryId);
        //通过userId从user表里通过主键查询分类数据
        User user = userMapper.selectById(userId.toString());
        if (ObjectUtil.isNotEmpty(category)) {
            //把category表的title传给categoryTitle
            dbIntroduction.setCategoryTitle(category.getTitle());
        }
        if (ObjectUtil.isNotEmpty(user)) {
            //把user表的name传给userName
            dbIntroduction.setUserName(user.getName());
            dbIntroduction.setUserAvatar(user.getAvatar());
        }
        return dbIntroduction;
    }
}
