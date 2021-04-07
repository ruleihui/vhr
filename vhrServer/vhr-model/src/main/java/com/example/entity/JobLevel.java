package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (Joblevel)实体类
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public class JobLevel implements Serializable {
    private static final long serialVersionUID = -41883111149066437L;
    private static final String validValue = "正高级,副高级,中级,初级,员级";
    private Integer id;
    /**
     * 职称名称
     */
    private String name;

    private String titleLevel;

    private Date createDate;

    private Boolean enabled;

    /**
     * 判断职称是否有效
     *
     * @param titleLevel
     * @return
     */
    public static boolean titleLevelValid(String titleLevel) {
        return validValue.contains(titleLevel);
    }

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

    public String getTitleLevel() {
        return titleLevel;
    }

    public void setTitleLevel(String titleLevel) {
        this.titleLevel = titleLevel;
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