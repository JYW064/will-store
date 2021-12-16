package com.jyw.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    private Integer id;

    private Integer categoryId;

    private String categoryName;
}