package com.jyw.controller;

import com.jyw.entity.Carousel;
import com.jyw.entity.Category;
import com.jyw.entity.Product;
import com.jyw.service.impl.CarouselServiceImpl;
import com.jyw.service.impl.ProductServiceImpl;
import com.jyw.vo.ProductsVo;
import com.jyw.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    ProductServiceImpl productService;

    @Autowired
    CarouselServiceImpl carouselService;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/getPromoProducts")
    public Result getPromoProducts(@RequestBody Category category){
        List<Product> productList= productService.getPromoProducts(category.getCategoryId());
        Result result = new Result();
        result.setCode("001");
        result.setResult(productList);
        return result;
    }

    @PostMapping("/getHotProducts")
    public Result getHotProducts(@RequestBody List<Integer> categoryIdList){

        List<Product> productList = productService.getHotProducts(categoryIdList);
        Result result = new Result();
        result.setCode("001");
        result.setResult(productList);
        return result;
    }

    @PostMapping("/getCarousel")
    public Result getCarousel(){
        List<Carousel> carouselList = carouselService.getCarousel();
        Result result = new Result();
        result.setCode("001");
        result.setResult(carouselList);
        return result;
    }

    @PostMapping("/insert")
    public void insertProducts(@RequestBody List<Product> productList){
        productService.insertProduct(productList);
    }
}
