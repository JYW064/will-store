package com.jyw.service.impl;

import com.jyw.entity.Carousel;
import com.jyw.entity.CarouselExample;
import com.jyw.entity.Product;
import com.jyw.entity.ProductExample;
import com.jyw.mapper.CarouselMapper;
import com.jyw.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    CarouselMapper carouselMapper;

    @Override
    public List<Carousel> getCarousel() {
        CarouselExample example = new CarouselExample();
        List<Carousel> carouselList = carouselMapper.selectByExample(example);
        return carouselList;
    }
}
