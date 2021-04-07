package com.example.service.employee;

import com.example.dao.EmployeeDao;
import com.example.entity.Employee;
import com.example.entity.MailSendLog;
import com.example.entity.help.MailConstants;
import com.example.entity.help.RespPageBean;
import com.example.service.MailSendLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmployeeService {
    EmployeeDao dao;
    RabbitTemplate template;
    MailSendLogService mailSendLogService;

    public EmployeeService(MailSendLogService service, EmployeeDao dao, RabbitTemplate template) {
        this.dao = dao;
        this.template = template;
        this.mailSendLogService = service;
    }

    public RespPageBean getEmployeeByPage(Integer page, Integer size, Employee employee) {
        Integer offset = null;
        if (page != null) offset = (page - 1) * size;
        List<Employee> employees = dao.getAllEmployeeInfo(offset, size, employee);
        int total = dao.getTotal(employee);
        return new RespPageBean(total, employees);
    }

    public RespPageBean getEmployeeSalaryByPage(Integer page, Integer size) {
        Integer offset = null;
        if (page != null) offset = (page - 1) * size;
        List<Employee> employees = dao.getEmployeeSalaryByPage(offset, size);
        int total = dao.getTotal(null);
        return new RespPageBean(total, employees);
    }


    public int add(Employee employee) {
        //新增
        if (dao.insert(employee) == 1) {
            Employee args = new Employee();
            args.setId(employee.getId());
            //查询添加成功的员工的全部信息
            List<Employee> singleAllEmployeeInfo = dao.getAllEmployeeInfo(null, null, args);
            //生成消息唯一id
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setCreateTime(new Date());
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailSendLog.setEmpId(args.getId());
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + MailConstants.MSG_TIMEOUT));
            //新增邮件发送记录
            mailSendLogService.insert(mailSendLog);

            template.convertAndSend(
                    MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    singleAllEmployeeInfo.get(0),
                    new CorrelationData(msgId));
/*重复消费测试            template.convertAndSend(
                    MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    singleAllEmployeeInfo.get(0),
                    new CorrelationData(msgId));*/
            return 1;
        } else {
            return 0;
        }
    }

    public String getWorkId() {
        return dao.getWorkId();
    }

    public int delete(Integer id) {
        return dao.deleteById(id);
    }

    public int insertAll(List<Employee> list) {
        return dao.insertAll(list);
    }

    public int update(Employee employee) {
        return dao.update(employee);
    }

}
