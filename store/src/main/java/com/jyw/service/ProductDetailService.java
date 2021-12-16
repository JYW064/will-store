package com.jyw.service;

import com.jyw.entity.Product;
import com.jyw.entity.ProductPicture;

import java.util.List;

public interface ProductDetailService {
    List<Product> getDetails(int productId);
    List<ProductPicture> getDetailPictures(int productId);
    void insert(List<ProductPicture> pictureList);
}
