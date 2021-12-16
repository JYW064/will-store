package com.jyw.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Orders implements Serializable {
    private Integer id;

    private Integer userId;

    private String orderId;

    private Integer productId;

    private String productName;

    private Integer price;

    private String img;

    private Integer num;

    private String orderTime;

    private Integer payStatus;
}