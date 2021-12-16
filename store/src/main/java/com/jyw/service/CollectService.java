package com.jyw.service;

import com.jyw.entity.Collect;
import com.jyw.entity.Product;

import java.util.List;

public interface CollectService {
    List<Collect> getAllCollects(int userId);
    void deleteCollect(int userId,int productId);
    void addCollect(int userId,int productId);
    Boolean isCollected(int userId,int productId);
}