package com.jyw.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderVo<T> implements Serializable {
    private String msg;
    private String code;
    private T result;
    private String successResponse;
}
