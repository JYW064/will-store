package com.jyw.controller;

import com.jyw.common.annotation.Token;
import com.jyw.entity.Cart;
import com.jyw.entity.Orders;
import com.jyw.entity.User;
import com.jyw.service.impl.OrderServiceImpl;
import com.jyw.vo.OrderVo;
import com.jyw.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    OrderServiceImpl orderService;

    @Token
    @PostMapping("/addOrder")
    public OrderVo addOrder(@RequestBody List<Cart> cartList){
        OrderVo result = new OrderVo();
        result.setCode("001");
        result.setMsg("生成订单！");
        String orderId = orderService.addOrder(cartList);
        result.setResult(orderId);
        try {
            result.setSuccessResponse(orderService.checkOrder(orderId));
        }catch (Exception e){
            log.error(e.getMessage());
            result.setMsg(e.getMessage());
            result.setCode("004");
        }
        return result;
    }

    @Token
    @PostMapping("/getOrder")
    public Result getOrder(@RequestBody User user){
        Result result = new Result();
        List<List<Orders>> orderLists = orderService.getOrder(user.getUserId());
        result.setCode("001");
        result.setResult(orderLists);
        return result;
    }
}
