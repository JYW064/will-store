package com.jyw.vo;

import com.jyw.entity.Product;
import com.jyw.entity.ProductPicture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductPictures implements Serializable {
    String code;
    List<ProductPicture> productPicture;
}
