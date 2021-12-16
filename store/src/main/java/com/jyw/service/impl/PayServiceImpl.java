package com.jyw.service.impl;

import com.alipay.api.AlipayApiException;
import com.jyw.entity.AlipayBean;
import com.jyw.service.PayService;
import com.jyw.utils.AlipayUtil;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {
    @Override
    public String aliPay(AlipayBean alipayBean) throws AlipayApiException {

        return AlipayUtil.connect(alipayBean);
    }
}

