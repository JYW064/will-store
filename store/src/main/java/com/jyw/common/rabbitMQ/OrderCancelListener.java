package com.jyw.common.rabbitMQ;

import com.jyw.entity.Orders;
import com.jyw.service.impl.OrderServiceImpl;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RabbitListener(queues = "mall.order.cancel.queue")//监听的队列名称
public class OrderCancelListener {
    @Autowired
    OrderServiceImpl orderService;


    @RabbitHandler
    public void cancelOrder(String orderId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {

        channel.basicAck(tag, false);
//        try {
//
//        } catch (Exception e) {
//        }

        orderService.updateOrderStatusTo3(orderId);
        orderService.salesRollBack(orderId);

    }
}
