package com.example.service;

import com.example.dao.MailSendLogDao;
import com.example.entity.MailSendLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailSendLogService {

    MailSendLogDao dao;

    public MailSendLogService(MailSendLogDao dao) {
        this.dao = dao;
    }

    public Integer update(MailSendLog mailSendLog) {
        return dao.update(mailSendLog);
    }

    public Integer insert(MailSendLog mailSendLog) {
        return dao.insert(mailSendLog);
    }
    //queryAllByStatusAndTryTimeLtSysdate
    public List<MailSendLog> queryAllByStatusAndTryTimeLtSysdate(MailSendLog mailSendLog) {
        return dao.queryAllByStatusAndTryTimeLtSysdate(mailSendLog);
    }
}
