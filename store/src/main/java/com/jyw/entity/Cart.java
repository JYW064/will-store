package com.jyw.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Cart implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private String productName;

    private Integer price;

    private String img;

    private Integer num;

    private Integer maxNum;

    private Integer status;
}