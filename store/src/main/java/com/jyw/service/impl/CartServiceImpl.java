package com.jyw.service.impl;

import com.jyw.entity.*;
import com.jyw.mapper.CartMapper;
import com.jyw.mapper.ProductMapper;
import com.jyw.service.CartService;
import com.jyw.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    @Override
    public List<Cart> getCartByUserId(int userId) {
        CartExample example = new CartExample();
        CartExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Cart> cartList = cartMapper.selectByExample(example);
        return cartList;
    }

    @Override
    public int isExist(int userId, int productId) {
        CartExample example = new CartExample();
        CartExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andProductIdEqualTo(productId);
        List<Cart> cartList = cartMapper.selectByExample(example);
        return cartList.isEmpty() ? 0 : cartList.get(0).getNum();
    }

    @Override
    public Result insertCart(int userId, int productId) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(productId);
        Product product = productMapper.selectByExample(example).get(0);

        int num = product.getMaxNum() - product.getSales();
        //要返回代码和对象，所以用了result
        Result result = new Result();
        if (num > 0) {
            int isExist = isExist(userId, productId);
            if (isExist == 0) {
                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setProductId(product.getProductId());
                cart.setProductName(product.getProductName());
                cart.setPrice(product.getPrice());
                cart.setImg(product.getImg());
                cart.setNum(1);
                cart.setMaxNum(product.getMaxNum());
                cartMapper.insertSelective(cart);
                result.setCode("001");
                result.setMsg("加入购物车成功！");
                result.setResult(cart);
                return result;
            } else {
                updateCart(userId, productId, isExist + 1);
                result.setCode("002");
                result.setMsg("该商品已存在，数量+1！");
                return result;
            }
        } else {
            result.setCode("003");
            result.setMsg("该商品已售罄！");
            return result;
        }
    }

    @Override
    public void updateCart(int userId, int productId, int num) {
        CartExample example = new CartExample();
        CartExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andProductIdEqualTo(productId);
        Cart cart = new Cart();
        cart.setNum(num);
        cartMapper.updateByExampleSelective(cart, example);
    }

    @Override
    public void deleteCart(int userId, int productId) {
        CartExample example = new CartExample();
        CartExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andProductIdEqualTo(productId);
        cartMapper.deleteByExample(example);
    }
}

