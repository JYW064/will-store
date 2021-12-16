package com.jyw.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Integer userId;

    private String username;

    private String password;

    private String tel;

    private String date;

    private Integer status;

    private String privateKey;

    private String token;

}