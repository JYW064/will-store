package com.jyw.service;

import com.jyw.entity.Product;
import com.jyw.vo.ProductsVo;

import java.util.List;

public interface ProductService {
    ProductsVo getAllProducts(int pageNum, int pageSize);
    ProductsVo getProductsByCategory(int pageNum, int pageSize,int categoryId);
    List<Product> getPromoProducts(int categoryId);
    List<Product> getHotProducts(List<Integer> categorList);
    void insertProduct(List<Product> productList);
}
