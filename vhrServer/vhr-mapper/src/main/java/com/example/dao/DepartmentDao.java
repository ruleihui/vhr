package com.example.dao;

import com.example.entity.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (Department)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-22 17:19:38
 */
public interface DepartmentDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Department queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Department> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param department 实例对象
     * @return 对象列表
     */
    List<Department> queryAll(Department department);

    /**
     * 新增数据
     *
     * @param department 实例对象
     * @return 影响行数
     */
    int insert(Department department);

    /**
     * 修改数据
     *
     * @param department 实例对象
     * @return 影响行数
     */
    int update(Department department);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    List<Department> getAllDepartmentByParentId(Integer pid);

    /**
     * 存储过程调用除非最后语句存在select则返回结果,否则是直接修改参数的值
     * 也可以通过定义resultMap返回值
     * com.example.dao.DepartmentDao#addDepartment(com.example.entity.Department)
     *
     * @param department
     */
    void addDepartment(Department department);

    void deleteDepartment(Map map);
}