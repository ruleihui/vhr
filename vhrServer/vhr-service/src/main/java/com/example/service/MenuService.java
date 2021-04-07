package com.example.service;

import com.example.dao.MenuDao;
import com.example.entity.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    MenuDao menuDao;
    static List<Menu> allMenusWithRole = null;

    public MenuService(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public List<Menu> getMenusByHrId(Integer hrId) {
        return menuDao.getMenusByHrId(hrId);
    }

    //实际生产中对于不太变化的数据通过注解添加缓存功能
    //@Cacheable,或者定义一个全局的map
    public List<Menu> getAllMenusWithRole() {
        if (allMenusWithRole == null) allMenusWithRole = menuDao.getAllMenusWithRole();
        return allMenusWithRole;
    }

    public List<Menu> queryAll(Menu menu) {
        return menuDao.queryAll(menu);
    }

    public List<Menu> queryMenuAndChild() {
        return menuDao.queryMenuAndChild();
    }

    public List<Integer> getMenusByRoleId(Integer rid) {
        return menuDao.getMenusByRoleId(rid);
    }

    @Transactional
    public Integer updateMenuByRoleId(Integer id, List<Integer> ids) {
        int num;
        num = menuDao.deleteMenusById(id);
        if (ids.size() != 0)
            num = menuDao.insertRelevanceByRoleId(id, ids);
        //更新后刷新缓存
        allMenusWithRole=null;
        return num;
    }
}
