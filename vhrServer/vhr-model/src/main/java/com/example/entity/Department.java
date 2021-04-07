package com.example.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (Department)实体类
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public class Department implements Serializable {
    private static final long serialVersionUID = 731705643641764249L;

    private Integer id;
    /**
     * 部门名称
     */
    private String name;

    private Integer parentId;

    private String depPath;

    private Boolean enabled;

    private Boolean isParent;
    private List<Department> children =new ArrayList<>();

    private Integer affectRow;

    public Integer getAffectRow() {
        return affectRow;
    }

    public void setAffectRow(Integer affectRow) {
        this.affectRow = affectRow;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getDepPath() {
        return depPath;
    }

    public void setDepPath(String depPath) {
        this.depPath = depPath;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", depPath='" + depPath + '\'' +
                ", enabled=" + enabled +
                ", isParent=" + isParent +
                ", children=" + children +
                '}';
    }
}