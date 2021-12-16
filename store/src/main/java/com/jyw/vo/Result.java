package com.jyw.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 前后端数据交互标准
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {
    private String msg;
    private String code;
    private T result;
}
