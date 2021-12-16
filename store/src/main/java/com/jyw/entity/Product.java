package com.jyw.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {
    private Integer id;

    private Integer productId;

    private String productName;

    private Integer categoryId;

    private String title;

    private String info;

    private Integer price;

    private String img;

    private Integer sellingPrice;

    private Integer sales;

    private Integer maxNum;

    private Integer version;

    private Integer hot;
}