package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (MailSendLog)实体类
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public class MailSendLog implements Serializable {
    private static final long serialVersionUID = 441447815984230026L;
    
    private String msgId;
    
    private Integer empId;
    /**
    * 0发送中，1发送成功，2发送失败
    */
    private int status;
    
    private String routeKey;
    
    private String exchange;
    /**
    * 重试次数
    */
    private int count;
    /**
    * 第一次重试时间
    */
    private Date tryTime;
    
    private Date createTime;
    
    private Date updateTime;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getTryTime() {
        return tryTime;
    }

    public void setTryTime(Date tryTime) {
        this.tryTime = tryTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}