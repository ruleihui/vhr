package com.example.service.salary;

import com.example.dao.SalaryDao;
import com.example.entity.Salary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class SalaryService {
    SalaryDao dao;

    public SalaryService(SalaryDao dao) {
        this.dao = dao;
    }

    public List<Salary> getAll() {
        return dao.queryAll(null);
    }

    public int add(Salary salary) {
        salary.setCreateDate(new Date());
        return dao.insert(salary);
    }

    public int delete(Integer id) {
        return dao.deleteById(id);
    }

    public int update(Salary salary) {
        return dao.update(salary);
    }

    public int deleteMulti(Integer[] ids) {
        return dao.deleteMulti(ids);
    }
}
