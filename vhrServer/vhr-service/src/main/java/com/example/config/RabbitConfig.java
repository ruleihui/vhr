package com.example.config;

import com.example.entity.MailSendLog;
import com.example.entity.help.MailConstants;
import com.example.service.MailSendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
public class RabbitConfig {
    private final static Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
    CachingConnectionFactory cachingConnectionFactory;
    static MailSendLogService mailSendLogService;

    public RabbitConfig(CachingConnectionFactory cachingConnectionFactory, MailSendLogService mailSendLogService) {
        this.cachingConnectionFactory = cachingConnectionFactory;
        this.mailSendLogService = mailSendLogService;
    }

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setConfirmCallback(RabbitConfig::confirm);
        rabbitTemplate.setReturnsCallback(RabbitConfig::returnedMessage);
        return rabbitTemplate;
    }
    @Bean
    Queue mailQueue(){
        //true if we are declaring a durable queue (the queue will survive a server restart)
        return new Queue(MailConstants.MAIL_QUEUE_NAME,true);
    }
    @Bean
    DirectExchange directExchange(){
        //true if the server should delete the exchange when it is no longer in use
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME,true,false);
    }

    @Bean
    Binding mailBinding(){
        return BindingBuilder.bind(mailQueue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }




    //投递失败
    //确认消息是否到达broker
    static void confirm(@Nullable CorrelationData data, boolean ack, @Nullable String cause) {
        String messageId = data.getId();
        if (ack) {
            //消息发送成功
            logger.info(messageId + "消息发送成功");
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(messageId);
            mailSendLog.setStatus(1);
            //修改数据库内容,消息投递成功
            mailSendLogService.update(mailSendLog);
        } else {
            logger.error(messageId + "消息发送失败");
        }
    }

    //从交换机往队列时失败,触发这个回调
    //exchange->queue时失败
    static  void returnedMessage(ReturnedMessage returned) {
            logger.error("消息返回失败");
    }


}
