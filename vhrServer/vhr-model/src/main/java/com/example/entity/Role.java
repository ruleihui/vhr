package com.example.entity;

import java.io.Serializable;
import java.util.List;

/**
 * (Role)实体类
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public class Role implements Serializable {
    private static final long serialVersionUID = 168074215230528685L;

    private Integer id;

    private String name;
    /**
     * 角色名称
     */
    private String nameZh;

    private List<Menu> menus;

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
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

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
}