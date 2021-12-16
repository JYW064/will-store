package com.jyw.controller;

import com.jyw.common.annotation.Token;
import com.jyw.entity.Cart;
import com.jyw.entity.User;
import com.jyw.service.impl.CartServiceImpl;
import com.jyw.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    CartServiceImpl cartService;

    @Token
    @PostMapping("/getCart")
    public Result getCart(@RequestBody User user) {
        List<Cart> cartList = cartService.getCartByUserId(user.getUserId());
        Result result = new Result();
        result.setCode("001");
        result.setResult(cartList);
        return result;
    }

    @Token
    @GetMapping("/insertCart")
    public Result insertCart(@RequestParam("userId") int userId,
                             @RequestParam("productId") int productId) {
        return cartService.insertCart(userId, productId);
    }

    @Token
    @GetMapping("/updateCart")
    public Result updateCart(@RequestParam("userId") int userId,
                             @RequestParam("productId") int productId,
                             @RequestParam("num") int num) {
        Result result = new Result();
        cartService.updateCart(userId, productId, num);
        result.setCode("001");
        result.setMsg("更新购物车成功！");
        return result;
    }

    @Token
    @GetMapping("/deleteCart")
    public Result deleteCart(@RequestParam("userId") int userId,
                             @RequestParam("productId") int productId) {
        Result result = new Result();
        cartService.deleteCart(userId, productId);
        result.setCode("001");
        result.setMsg("删除购物车成功！");
        return result;
    }
}
