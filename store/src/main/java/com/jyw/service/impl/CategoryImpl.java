package com.jyw.service.impl;

import com.jyw.entity.CartExample;
import com.jyw.entity.Category;
import com.jyw.entity.CategoryExample;
import com.jyw.mapper.CategoryMapper;
import com.jyw.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategory() {
        CategoryExample example = new CategoryExample();
        List<Category> categoryList = categoryMapper.selectByExample(example);
        return categoryList;
    }
}
