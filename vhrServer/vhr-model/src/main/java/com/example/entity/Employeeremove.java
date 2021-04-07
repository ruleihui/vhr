package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (Employeeremove)实体类
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public class Employeeremove implements Serializable {
    private static final long serialVersionUID = 330463399312821322L;
    
    private Integer id;
    
    private Integer eid;
    /**
    * 调动后部门
    */
    private Integer afterdepid;
    /**
    * 调动后职位
    */
    private Integer afterjobid;
    /**
    * 调动日期
    */
    private Date removedate;
    /**
    * 调动原因
    */
    private String reason;
    
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

    public Integer getAfterdepid() {
        return afterdepid;
    }

    public void setAfterdepid(Integer afterdepid) {
        this.afterdepid = afterdepid;
    }

    public Integer getAfterjobid() {
        return afterjobid;
    }

    public void setAfterjobid(Integer afterjobid) {
        this.afterjobid = afterjobid;
    }

    public Date getRemovedate() {
        return removedate;
    }

    public void setRemovedate(Date removedate) {
        this.removedate = removedate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}