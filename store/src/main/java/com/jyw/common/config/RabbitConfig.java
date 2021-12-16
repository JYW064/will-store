package com.jyw.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitConfig {

    /**
     * 普通队列交换机
     * @return
     */
    @Bean
    DirectExchange orderDirect(){
        return new DirectExchange("mall.order.direct.exchange",true,false );
    }
    /**
     * 订单延迟队列交换机
     */
    @Bean
    DirectExchange orderDLDirect() {
        return new DirectExchange("mall.order.cancel.dlExchange",true,false);
    }
    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue orderDLQueue(){
        return new Queue("mall.order.cancel.queue", true, false, false);
    }
    /**
     * 普通队列
     */
    @Bean
    public Queue orderQueue() {
        return QueueBuilder
                .durable("mall.order.direct.queue")
                .withArgument("x-dead-letter-exchange", "mall.order.cancel.dlExchange")//到期后转发的交换机
                .withArgument("x-dead-letter-routing-key", "mall.order.cancel")//到期后转发的路由键
                .build();
    }
    /**
     * 绑定普通交换机和队列
     * @param orderDirect
     * @param orderQueue
     * @return
     */
    @Bean
    public Binding orderBinding(DirectExchange orderDirect, Queue orderQueue){
        return BindingBuilder
                .bind(orderQueue)
                .to(orderDirect)
                .with("mall.order.direct");
    }
    /**
     * 将订单死信队列绑定到交换机
     */
    @Bean
    Binding orderTtlBinding(DirectExchange orderDLDirect,Queue orderDLQueue){
        return BindingBuilder
                .bind(orderDLQueue)
                .to(orderDLDirect)
                .with("mall.order.cancel");
    }

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
            System.out.println("ConfirmCallback:     "+"确认情况："+ack);
            System.out.println("ConfirmCallback:     "+"原因："+cause);
            log.debug("wdwdwd");
        });

        rabbitTemplate.setReturnsCallback((ReturnedMessage returnedMessage) -> {
                System.out.println("ReturnCallback:     "+"消息："+returnedMessage.getMessage());
                System.out.println("ReturnCallback:     "+"回应码："+returnedMessage.getReplyCode());
                System.out.println("ReturnCallback:     "+"回应信息："+returnedMessage.getReplyText());
                System.out.println("ReturnCallback:     "+"交换机："+returnedMessage.getExchange());
                System.out.println("ReturnCallback:     "+"路由键："+returnedMessage.getRoutingKey());
        });
        return rabbitTemplate;
    }
}