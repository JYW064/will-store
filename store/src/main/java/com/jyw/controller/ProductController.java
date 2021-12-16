package com.jyw.controller;

import com.jyw.entity.Category;
import com.jyw.entity.Product;
import com.jyw.service.impl.CategoryImpl;
import com.jyw.service.impl.ProductServiceImpl;
import com.jyw.vo.ProductsVo;
import com.jyw.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    ProductServiceImpl productService;

    @Autowired
    CategoryImpl category;

    @GetMapping("/getAllProducts")
    public Result getAllProducts(@RequestParam("currentPage") int currentPage,
                                 @RequestParam("pageSize") int pageSize){
        ProductsVo productsVo = productService.getAllProducts(currentPage,pageSize);
        Result result = new Result();
        result.setCode("001");
        result.setResult(productsVo);
        return result;
    }

    @GetMapping("/getProductsByCategory")
    public Result getProductsByCategory(@RequestParam("currentPage") int currentPage,
                                        @RequestParam("pageSize") int pageSize,
                                        @RequestParam("categoryId") int categoryId){
        ProductsVo productsVo = productService.getProductsByCategory(currentPage,pageSize,categoryId);
        Result result = new Result();
        result.setCode("001");
        result.setResult(productsVo);
        return result;
    }

    @PostMapping("/getCategory")
    public Result getCategory(){
        Result result = new Result();
        List<Category> categoryList = category.getCategory();
        result.setCode("001");
        result.setResult(categoryList);
        return result;
    }

}
