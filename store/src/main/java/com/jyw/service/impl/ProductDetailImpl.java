package com.jyw.service.impl;

import com.jyw.entity.Product;
import com.jyw.entity.ProductExample;
import com.jyw.entity.ProductPicture;
import com.jyw.entity.ProductPictureExample;
import com.jyw.mapper.ProductMapper;
import com.jyw.mapper.ProductPictureMapper;
import com.jyw.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailImpl implements ProductDetailService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductPictureMapper productPictureMapper;

    @Override
    public List<Product> getDetails(int productId) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(productId);
        List<Product> productList = productMapper.selectByExample(example);
        return productList;
    }

    @Override
    public List<ProductPicture> getDetailPictures(int productId) {
        ProductPictureExample example = new ProductPictureExample();
        ProductPictureExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(productId);
        List<ProductPicture> pictureList = productPictureMapper.selectByExample(example);
        return pictureList;
    }

    @Override
    public void insert(List<ProductPicture> pictureList) {
        for (ProductPicture picture : pictureList) {
            picture.setId(null);
            productPictureMapper.insertSelective(picture);
        }
    }
}
