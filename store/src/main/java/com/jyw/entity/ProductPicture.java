package com.jyw.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductPicture implements Serializable {
    private Integer id;

    private Integer productId;

    private String img;

    private String info;
}