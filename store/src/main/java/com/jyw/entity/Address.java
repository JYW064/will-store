package com.jyw.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer addressId;

    private String name;

    private String phone;

    private String address;
}