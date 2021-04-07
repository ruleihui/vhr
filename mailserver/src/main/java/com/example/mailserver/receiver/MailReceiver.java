package com.example.mailserver.receiver;

import com.example.entity.Employee;
import com.example.entity.help.MailConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.PublisherCallbackChannel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Component
public class MailReceiver {

    static final Logger log = LoggerFactory.getLogger(MailReceiver.class);

    JavaMailSender sender;
    MailProperties properties;
    TemplateEngine engine;
    StringRedisTemplate template;

    public MailReceiver(JavaMailSender sender, MailProperties properties,
                        TemplateEngine engine, StringRedisTemplate template) {
        this.sender = sender;
        this.properties = properties;
        this.engine = engine;
        this.template = template;
    }

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    //org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.invokeHandlerAndProcessResult
    //方法调用成功后,会自动确认,也可以接受channel参数进行手动确认
    //消费失败的消息(方法执行抛出异常后)会返回队列中,重复消费
    public void handler(Message<Employee> message, Channel channel) throws IOException {
        //message处理
        Employee employee = message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //delivery_tag交付标签,从0开始的自增计数统计
        Long tag = ((Long) headers.get(AmqpHeaders.DELIVERY_TAG));
        try {
            //Header used to locate a pending confirm to which to attach a returned message.
            //接口并不需要显示定义为public static final String test
            //new CorrelationData(msgId)
            //只需直接定义String test,因为interface的default就是public static final
            String msgId = ((String) headers.get(PublisherCallbackChannel.RETURNED_MESSAGE_CORRELATION_KEY));
            if (template.opsForHash().entries("mail_log").containsKey(msgId)) {
                //redis中包含该key,说明消息已被消费
                log.info("消息已经被消费");
                channel.basicAck(tag, false);//确认消息已消费
                return;
            }
            //接受消息,发送邮件
            MimeMessage mail = sender.createMimeMessage();
            //帮助构建Message
            MimeMessageHelper helper = new MimeMessageHelper(mail);
            //模板上下文加载
            Context context = new Context();
            context.setVariable("position", employee.getPosition().getName());
            context.setVariable("department", employee.getDepartment().getName());
            context.setVariable("name", employee.getName());
            context.setVariable("jobLevel", employee.getJobLevel().getName());
            //根据上下文处理模板生成html格式字符串
            String process = engine.process("mail", context);
            helper.setSubject("入职欢迎");
            helper.setFrom(properties.getUsername());
            helper.setTo(employee.getEmail());
            //设置游览器头ContentType为"text/html"
            helper.setText(process, true);
            helper.setSentDate(new Date());
            //最终发送邮件
            sender.send(mail);
            template.opsForHash().put("mail_log", msgId, "test");
            channel.basicAck(tag, false);//确认消息已消费
            log.info("邮件发送成功:姓名_" + employee.getName() + ",邮箱_" + employee.getEmail());
        } catch (MessagingException | IOException e) {
            //tag作为交付标签,用于找到消息,
            //multiple表示是否开启批处理,为true将处理deliveryTag<=tag的所有信息
            //requeue为true表示消息返回队列,false则表示丢弃,死信
            channel.basicNack(tag, false, true);
            log.error("邮件发送失败:" + employee.getEmail() + ",错误:" + e.getMessage());
        }
    }
}
