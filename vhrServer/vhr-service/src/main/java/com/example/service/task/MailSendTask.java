package com.example.service.task;

import com.example.dao.EmployeeDao;
import com.example.entity.Employee;
import com.example.entity.MailSendLog;
import com.example.entity.help.MailConstants;
import com.example.entity.help.RespPageBean;
import com.example.service.MailSendLogService;
import com.example.service.employee.EmployeeService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MailSendTask {
    MailSendLogService mailSendLogService;

    RabbitTemplate rabbitTemplate;

    EmployeeDao employeeDao;

    public MailSendTask(MailSendLogService mailSendLogService, RabbitTemplate rabbitTemplate, EmployeeDao employeeDao) {
        this.mailSendLogService = mailSendLogService;
        this.rabbitTemplate = rabbitTemplate;
        this.employeeDao = employeeDao;
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void mailResendTask() {
        MailSendLog mailSendLogArgs = new MailSendLog();
        mailSendLogArgs.setStatus(0);
        List<MailSendLog> logs = mailSendLogService.queryAllByStatusAndTryTimeLtSysdate(mailSendLogArgs);
        logs.forEach(mailSendLog -> {
            if (mailSendLog.getCount() >= 3) mailSendLog.setStatus(2);//设置消息发送失败
            else {
                mailSendLog.setCount(mailSendLog.getCount() + 1);
                mailSendLog.setUpdateTime(new Date());
                Employee args = new Employee();
                args.setId(mailSendLog.getEmpId());
                List<Employee> employees = employeeDao.getAllEmployeeInfo(null, null, args);
                rabbitTemplate.convertAndSend(
                        MailConstants.MAIL_EXCHANGE_NAME,
                        MailConstants.MAIL_ROUTING_KEY_NAME,
                        employees.get(0),
                        new CorrelationData(mailSendLog.getMsgId())
                );
            }
            mailSendLogService.update(mailSendLog);

        });
    }


}
