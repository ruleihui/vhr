package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (Employeetrain)实体类
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public class Employeetrain implements Serializable {
    private static final long serialVersionUID = 716697849353341869L;
    
    private Integer id;
    /**
    * 员工编号
    */
    private Integer eid;
    /**
    * 培训日期
    */
    private Date traindate;
    /**
    * 培训内容
    */
    private String traincontent;
    /**
    * 备注
    */
    private String remark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Date getTraindate() {
        return traindate;
    }

    public void setTraindate(Date traindate) {
        this.traindate = traindate;
    }

    public String getTraincontent() {
        return traincontent;
    }

    public void setTraincontent(String traincontent) {
        this.traincontent = traincontent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}