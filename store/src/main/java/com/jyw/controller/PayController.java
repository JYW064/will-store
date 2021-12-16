package com.jyw.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jyw.common.annotation.Token;
import com.jyw.common.config.PropertiesConfig;
import com.jyw.entity.AlipayBean;
import com.jyw.entity.Cart;
import com.jyw.entity.Orders;
import com.jyw.service.PayService;
import com.jyw.service.impl.OrderServiceImpl;
import com.jyw.service.impl.PayServiceImpl;
import com.jyw.utils.DateUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController()
@RequestMapping("pay")
public class PayController {
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    PayServiceImpl payService;

    @PostMapping("/callBack")
    public String alipayNotify(HttpServletRequest request) throws Exception {

        // 获取支付宝的请求信息
        Map<String, String> map = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        if (requestParams.isEmpty()) {
            return "failure";
        }
        // 将 Map<String,String[]> 转为 Map<String,String>
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            map.put(name, valueStr);
        }
        // 验签
        boolean signVerified = AlipaySignature.rsaCheckV1(map, PropertiesConfig.getKey("alipay_public_key"), PropertiesConfig.getKey("charset"),
                PropertiesConfig.getKey("sign_type"));
        // 验签通过
        if (signVerified) {
            System.out.println(map);
            //若支付成功
            if (map.get("trade_status").equals("TRADE_SUCCESS")) {
                //修改订单状态
                orderService.updateOrderStatus(map.get("out_trade_no"));
            }
            return "success"; //支付成功后进行操作
        }
        return "failure";
    }


    @Token
    @GetMapping("/alipay")
    public String Alipay(@Param("orderId") String orderId,
                         @Param("orderName") String orderName,
                         @Param("total") int total) throws AlipayApiException {

        return payService.aliPay(new AlipayBean()
                .setOut_trade_no(orderId)
                .setTotal_amount(new StringBuffer().append(total))
                .setSubject(orderName));
    }
}