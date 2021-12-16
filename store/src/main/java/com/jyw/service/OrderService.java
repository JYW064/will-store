package com.jyw.service;

import com.alipay.api.AlipayApiException;
import com.jyw.entity.AlipayBean;
import com.jyw.entity.Cart;
import com.jyw.entity.Orders;

import java.util.List;

public interface OrderService {
    String addOrder(List<Cart> cart);
    List<List<Orders>> getOrder(int userId);
    String checkOrder(String orderId) throws Exception;
    void updateOrderStatus(String orderId);
    void salesRollBack(String ordersList) throws Exception;
}
