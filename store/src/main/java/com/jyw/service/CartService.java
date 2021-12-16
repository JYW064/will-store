package com.jyw.service;

import com.jyw.entity.Cart;
import com.jyw.entity.Product;
import com.jyw.vo.Result;

import java.util.List;

public interface CartService {
    List<Cart> getCartByUserId(int userId);

    int isExist(int userId, int productId);

    Result insertCart(int userId, int productId);

    void updateCart(int userId, int productId, int num);

    void deleteCart(int userId, int productId);
}
