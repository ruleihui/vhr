package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (Position)实体类
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public class Position implements Serializable {
    private static final long serialVersionUID = -59858284936238759L;
    
    private Integer id;
    /**
    * 职位
    */
    private String name;
    
    private Date createDate;
    
    private Boolean enabled;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}