package com.jyw.vo;

import com.jyw.entity.Product;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductsVo implements Serializable {
    List<Product> products;
    long total;
}
