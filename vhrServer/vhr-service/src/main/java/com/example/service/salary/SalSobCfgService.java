package com.example.service.salary;

import com.example.dao.SalSobCfgDao;
import org.springframework.stereotype.Service;

@Service
public class SalSobCfgService {
    SalSobCfgDao dao;

    public SalSobCfgService(SalSobCfgDao dao) {
        this.dao = dao;
    }

    public int update(Integer employeeId, Integer salaryId) {
        return dao.update(employeeId, salaryId);
    }

    public int add(Integer employeeId, Integer salaryId) {
       return dao.add(employeeId,salaryId);
    }
}
