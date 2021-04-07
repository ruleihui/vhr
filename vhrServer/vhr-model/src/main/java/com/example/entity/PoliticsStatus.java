package com.example.entity;

import java.io.Serializable;

/**
 * (Politicsstatus)实体类
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public class PoliticsStatus implements Serializable {
    private static final long serialVersionUID = 941536449329095707L;
    
    private Integer id;
    
    private String name;


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

}