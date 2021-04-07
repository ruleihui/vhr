package com.example.service.system.basic;

import com.example.dao.DepartmentDao;
import com.example.entity.Department;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DepartmentService {
    DepartmentDao dao;

    public DepartmentService(DepartmentDao dao) {
        this.dao = dao;
    }

    public Department getAll() {
        List<Department> departments = dao.queryAll(null);
        Stream<Department> stream = departments.stream();
        for (Department department : departments) {
            List<Department> children = department.getChildren();
            stream.filter(it -> department.getId().equals(it.getParentId()))
                    .forEach(it -> children.add(it));
            stream=departments.stream();
        }
        return stream.filter(it -> it.getParentId() == -1).findFirst().get();
    }

    public static void main(String[] args) {
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9).stream();
        stream.filter(it -> {
            System.err.println(it);
            return it % 2 == 0;
        }).findFirst().ifPresent(System.out::println);
    }

    public List<Department> getAllDepartmentByParentId(Integer pid) {
        return dao.getAllDepartmentByParentId(pid);
    }

    public void addDepartment(Department department) {
        department.setEnabled(true);
        dao.addDepartment(department);
    }

    public void deleteDepartment(Map map) {
        dao.deleteDepartment(map);
    }
}
