package com.jyw.service;

import com.alipay.api.AlipayApiException;
import com.jyw.entity.AlipayBean;

public interface PayService {
    String aliPay(AlipayBean alipayBean) throws AlipayApiException;
}
