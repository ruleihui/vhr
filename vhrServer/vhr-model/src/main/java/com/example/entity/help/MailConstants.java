package com.example.entity.help;

import java.util.concurrent.TimeUnit;

public class MailConstants {
    public static final Integer DELIVERING = 0;//消息投递中
    public static final Integer SUCCESS = 1;//消息投递成功
    public static final Integer FAILURE = 0;//消息投递失败
    public static final Integer MAX_TRY_COUNT = 3;//最大重试次数
    public static final Long MSG_TIMEOUT = TimeUnit.MINUTES.toMillis(1);//消息超时时间,单位分钟
    public static final String MAIL_QUEUE_NAME = "example.mail.queue";//消息超时时间,单位秒
    public static final String MAIL_EXCHANGE_NAME = "example.mail.exchange";//消息超时时间,单位秒
    public static final String MAIL_ROUTING_KEY_NAME = "example.mail.routing_key";//消息超时时间,单位秒


}
