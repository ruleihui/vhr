package com.example.service.system.basic;

import com.example.dao.RoleDao;
import com.example.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public List<Role> queryAll(Role role) {
        return roleDao.queryAll(role);
    }


    public Integer add(Role role) {
        return roleDao.insert(role);
    }
    @Transactional
    public int delete(Integer id) {
        roleDao.deleteById(id);
        return roleDao.deleteMenuRoleByRoleId(id);
    }
}
