package com.jyw.service.impl;

import com.alipay.api.AlipayApiException;
import com.jyw.common.annotation.Retry;
import com.jyw.common.exception.RetryException;
import com.jyw.entity.*;
import com.jyw.mapper.CartMapper;
import com.jyw.mapper.OrdersMapper;
import com.jyw.mapper.ProductMapper;
import com.jyw.service.OrderService;
import com.jyw.utils.AlipayUtil;
import com.jyw.utils.DateUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @Override
    public String addOrder(List<Cart> cartList) {
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        for (Cart cart : cartList) {
            Orders orders = new Orders();
            orders.setUserId(cart.getUserId());
            orders.setOrderId(uuid);
            orders.setProductId(cart.getProductId());
            orders.setProductName(cart.getProductName());
            orders.setPrice(cart.getPrice());
            orders.setImg(cart.getImg());
            orders.setNum(cart.getNum());
            String date = DateUtil.ptfDate();
            orders.setOrderTime(date);
            ordersMapper.insertSelective(orders);
        }
        return uuid;
    }

    @Override
    public List<List<Orders>> getOrder(int userId) {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Orders> orders = ordersMapper.selectByExample(example);

        Map<String, List<Orders>> map = new LinkedHashMap();
        for (Orders order : orders) {
            if (!map.containsKey(order.getOrderId())) {
                map.put(order.getOrderId(), new ArrayList<>());
                map.get(order.getOrderId()).add(order);
            } else {
                map.get(order.getOrderId()).add(order);
            }
        }
        return new ArrayList<>(map.values());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Retry
    @Override
    public String checkOrder(String orderId) throws Exception {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<Orders> ordersList = ordersMapper.selectByExample(example);

        int total = 0;
        String orderName = ordersList.get(0).getProductName();
        for (Orders orders : ordersList) {
            int num = orders.getNum();
            total += orders.getPrice() * num;

            ProductExample example1 = new ProductExample();
            ProductExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andProductIdEqualTo(orders.getProductId());
            Product product = productMapper.selectByExample(example1).get(0);
            int productId = product.getProductId();
            int version = product.getVersion();
            int sales = product.getSales();
            //Thread.sleep(15000);
            int flag = productMapper.updateSalesByVersion(productId, version, num + sales);
            if (flag == 0) {
                throw new RetryException("更新失败，数据已被修改，正在重试。。。");
            }
        }


        String successResponse = aliPay(new AlipayBean()
                .setOut_trade_no(orderId)
                .setTotal_amount(new StringBuffer().append(total))
                .setSubject(orderName));


        rabbitTemplate.convertAndSend("mall.order.direct.exchange", "mall.order.direct", orderId, message -> {
            //将订单号发送到延迟队列，延迟时间为3分钟
            message.getMessageProperties().setExpiration("180000");
            return message;
        });
        return successResponse;
    }

    @Override
    public void updateOrderStatus(String orderId) {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        Orders orders = new Orders();
        orders.setPayStatus(1);
        ordersMapper.updateByExampleSelective(orders, example);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateOrderStatusTo3(String orderId) throws Exception {
        //String orderId = ordersList.get(0).getOrderId();
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<Orders> ordersList = ordersMapper.selectByExample(example);
        int orderStatus = ordersList.get(0).getPayStatus();
        if (orderStatus == 0) {
            log.info("订单号：{}超时取消。。。", orderId);
            Orders order = new Orders();
            order.setPayStatus(3);
            ordersMapper.updateByExampleSelective(order, example);
        }
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    @Retry
    public void salesRollBack(String orderId) throws Exception {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<Orders> ordersList = ordersMapper.selectByExample(example);

        for (Orders orders : ordersList) {
            int num = orders.getNum();

            ProductExample example1 = new ProductExample();
            ProductExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andProductIdEqualTo(orders.getProductId());
            Product product = productMapper.selectByExample(example1).get(0);
            int productId = product.getProductId();
            int version = product.getVersion();
            int sales = product.getSales();
            //Thread.sleep(10000);
            int flag = productMapper.updateSalesByVersion(productId, version, sales - num);
            if (flag == 0) {
                log.info("更新失败");
                throw new RetryException("更新失败，数据已被修改，正在重试。。。");
            }
        }
    }

    public String aliPay(AlipayBean alipayBean) throws AlipayApiException {
        return AlipayUtil.connect(alipayBean);
    }
}