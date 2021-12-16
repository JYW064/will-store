package com.jyw.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyw.entity.Product;
import com.jyw.entity.ProductExample;
import com.jyw.mapper.ProductMapper;
import com.jyw.service.ProductService;
import com.jyw.vo.ProductsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public ProductsVo getAllProducts(int pageNum, int pageSize) {
        if(redisTemplate.hasKey("AllProductsPage"+pageNum)){
            log.info("缓存命中");
            return (ProductsVo)redisTemplate.opsForValue().get("AllProductsPage"+pageNum);
        }else {
            ProductsVo productsVo = new ProductsVo();
            PageHelper.startPage(pageNum, pageSize);
            ProductExample example = new ProductExample();
            List<Product> productList = productMapper.selectByExample(example);
            PageInfo<Product> pageInfo = new PageInfo<>(productList);
            productsVo.setProducts(productList);
            productsVo.setTotal(pageInfo.getTotal());
            redisTemplate.opsForValue().set("AllProductsPage"+pageNum,productsVo);
            redisTemplate.expire("AllProductsPage",1, TimeUnit.HOURS);
        }
        return (ProductsVo)redisTemplate.opsForValue().get("AllProductsPage"+pageNum);
    }

    @Override
    public ProductsVo getProductsByCategory(int pageNum, int pageSize,int categoryId) {
        if(redisTemplate.hasKey("ProductsByCategoryPage"+pageNum)){
            log.info("缓存命中");
            return (ProductsVo)redisTemplate.opsForValue().get("ProductsByCategoryPage"+pageNum);
        }else {
            ProductsVo productsVo = new ProductsVo();
            PageHelper.startPage(pageNum, pageSize);
            ProductExample example = new ProductExample();
            ProductExample.Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            List<Product> productList = productMapper.selectByExample(example);
            PageInfo<Product> pageInfo = new PageInfo<>(productList);
            productsVo.setProducts(productList);
            productsVo.setTotal(pageInfo.getTotal());
            redisTemplate.opsForValue().set("ProductsByCategoryPage"+pageNum,productsVo);
            redisTemplate.expire("ProductsByCategoryPage",1, TimeUnit.HOURS);
        }
        return (ProductsVo)redisTemplate.opsForValue().get("ProductsByCategoryPage"+pageNum);
    }


    @Override
    public List<Product> getPromoProducts(int categoryId) {
        if(redisTemplate.hasKey("promo"+categoryId)){
            log.info("缓存命中");
            return (List<Product>)redisTemplate.opsForValue().get("promo"+categoryId);
        }else{
            ProductExample example = new ProductExample();
            ProductExample.Criteria criteria=example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            criteria.andHotEqualTo(1);
            List<Product> productList = productMapper.selectByExample(example);
            if(productList.size()>6){
                productList = new ArrayList<>(productList.subList(0, 7));
            }
            redisTemplate.opsForValue().set("promo"+categoryId,productList);
            redisTemplate.expire("promo"+categoryId,1, TimeUnit.HOURS);
        }
        return (List<Product>)redisTemplate.opsForValue().get("promo"+categoryId);
    }

    @Override
    public List<Product> getHotProducts(List<Integer> categorList) {
        if(redisTemplate.hasKey("hot"+categorList)){
            log.info("缓存命中");
            return (List<Product>)redisTemplate.opsForValue().get("hot"+categorList);
        }else {
            List<Product> productList = new ArrayList<>();
            for (Integer category : categorList) {
                List<Product> temp = getPromoProducts(category);
                productList.addAll(temp);
            }
            if (productList.size() > 6) {
                productList = new ArrayList<>(productList.subList(0, 7));
            }
            redisTemplate.opsForValue().set("hot"+categorList,productList);
            redisTemplate.expire("hot"+categorList,1, TimeUnit.HOURS);
        }
        return (List<Product>)redisTemplate.opsForValue().get("hot"+categorList);
    }

    @Override
    public void insertProduct(List<Product> productList) {
        for(Product product:productList){
            productMapper.insertSelective(product);
        }
    }
}
