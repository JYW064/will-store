package com.jyw.controller;

import com.jyw.common.annotation.Token;
import com.jyw.entity.Category;
import com.jyw.entity.Product;
import com.jyw.entity.ProductPicture;
import com.jyw.service.impl.ProductDetailImpl;
import com.jyw.vo.ProductPictures;
import com.jyw.vo.ProductsVo;
import com.jyw.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("product")
public class ProductDetailController{
    @Autowired
    ProductDetailImpl productDetail;

    @PostMapping("/getDetails")
    public Result getDetails(@RequestBody Product product){
        List<Product> productList= productDetail.getDetails(product.getProductId());
        Result result = new Result();
        result.setCode("001");
        result.setResult(productList);
        return result;
    }

    @PostMapping("/getDetailsPictures")
    public Result getDetailsPictures(@RequestBody Product product){
        List<ProductPicture> pictureList= productDetail.getDetailPictures(product.getProductId());
        Result result = new Result();
        result.setCode("001");
        result.setResult(pictureList);
        return result;
    }

    @PostMapping("/insert")
    public void insert(@RequestBody ProductPictures productPictures){

        productDetail.insert(productPictures.getProductPicture());
    }
}
