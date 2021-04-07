package com.example.dao;

import com.example.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Employee)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public interface EmployeeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Employee queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Employee> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);
    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param size 查询条数
     * @param employee
     * @return 对象列表
     */
    List<Employee> getAllEmployeeInfo(@Param("offset") Integer offset, @Param("size") Integer size,
                                      @Param("employee") Employee employee);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param employee 实例对象
     * @return 对象列表
     */
    List<Employee> queryAll(Employee employee);

    /**
     * 新增数据
     *
     * @param employee 实例对象
     * @return 影响行数
     */
    int insert(Employee employee);

    /**
     * 修改数据
     *
     * @param employee 实例对象
     * @return 影响行数
     */
    int update(Employee employee);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int getTotal(Employee employee);

    String getWorkId();

    int insertAll(List<Employee> list);

    List<Employee> getEmployeeSalaryByPage(@Param("offset") Integer offset, @Param("size") Integer size);
}