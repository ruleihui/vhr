package com.example.dao;

import com.example.entity.Employeeremove;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Employeeremove)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public interface EmployeeremoveDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Employeeremove queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Employeeremove> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param employeeremove 实例对象
     * @return 对象列表
     */
    List<Employeeremove> queryAll(Employeeremove employeeremove);

    /**
     * 新增数据
     *
     * @param employeeremove 实例对象
     * @return 影响行数
     */
    int insert(Employeeremove employeeremove);

    /**
     * 修改数据
     *
     * @param employeeremove 实例对象
     * @return 影响行数
     */
    int update(Employeeremove employeeremove);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}