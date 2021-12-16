package com.jyw.service.impl;

import com.jyw.entity.*;
import com.jyw.mapper.CollectMapper;
import com.jyw.mapper.ProductMapper;
import com.jyw.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    CollectMapper collectMapper;
    @Autowired
    ProductMapper productMapper;

    @Override
    public List<Collect> getAllCollects(int userId) {
        CollectExample example = new CollectExample();
        CollectExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Collect> collectList = collectMapper.selectByExample(example);
        return collectList;
    }

    @Override
    public void deleteCollect(int userId, int productId) {
        CollectExample example = new CollectExample();
        CollectExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andProductIdEqualTo(productId);
        collectMapper.deleteByExample(example);
    }


    @Override
    public void addCollect(int userId, int productId) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(productId);
        Product product = productMapper.selectByExample(example).get(0);
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setProductId(product.getProductId());
        collect.setProductName(product.getProductName());
        collect.setCategoryId(product.getCategoryId());
        collect.setTitle(product.getTitle());
        collect.setInfo(product.getInfo());
        collect.setPrice(product.getPrice());
        collect.setImg(product.getImg());
        collect.setSellingPrice(product.getSellingPrice());
        collect.setSales(product.getSales());
        collect.setMaxNum(product.getMaxNum());
        collectMapper.insertSelective(collect);
    }

    @Override
    public Boolean isCollected(int userId, int productId) {
        CollectExample example = new CollectExample();
        CollectExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andProductIdEqualTo(productId);
        return collectMapper.selectByExample(example).isEmpty() ? false : true;
    }
}
