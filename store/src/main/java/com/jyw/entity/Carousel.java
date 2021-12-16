package com.jyw.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Carousel implements Serializable {
    private Integer id;

    private Integer carouselId;

    private String img;

    private String describes;
}