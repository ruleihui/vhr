package com.example.service.system.basic;

import com.example.dao.system.basic.JobLevelDao;
import com.example.entity.JobLevel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobLevelService {

    JobLevelDao dao;

    public JobLevelService(JobLevelDao dao) {
        this.dao = dao;
    }

    public List<JobLevel> getAll() {
        return dao.queryAll(null);
    }


    public int addJobLevel(JobLevel jobLevel) {
        jobLevel.setEnabled(true);
        jobLevel.setCreateDate(new Date());
        return dao.insert(jobLevel);
    }

    public int updateById(JobLevel jobLevel) {
        return dao.update(jobLevel);
    }

    public int deleteById(Integer id) {
        return dao.deleteById(id);
    }

    public int deleteByIds(Integer[] ids) {
        return dao.deleteByIds(ids);
    }
}
