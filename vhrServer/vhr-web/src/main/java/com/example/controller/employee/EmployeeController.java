package com.example.controller.employee;

import com.example.dao.DepartmentDao;
import com.example.dao.NationDao;
import com.example.dao.PoliticsStatusDao;
import com.example.dao.system.basic.JobLevelDao;
import com.example.dao.system.basic.PositionDao;
import com.example.entity.*;
import com.example.entity.help.RespBean;
import com.example.entity.help.RespPageBean;
import com.example.service.employee.EmployeeService;
import com.example.utils.ExcelOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    EmployeeService service;

    NationDao nationDao;

    PoliticsStatusDao politicsStatusDao;

    DepartmentDao departmentDao;

    JobLevelDao jobLevelDao;

    PositionDao positionDao;

    public EmployeeController(EmployeeService service, NationDao nationDao, PoliticsStatusDao politicsStatusDao, DepartmentDao departmentDao, JobLevelDao jobLevelDao, PositionDao positionDao) {
        this.service = service;
        this.nationDao = nationDao;
        this.politicsStatusDao = politicsStatusDao;
        this.departmentDao = departmentDao;
        this.jobLevelDao = jobLevelDao;
        this.positionDao = positionDao;
    }

    @GetMapping
    public RespBean getAllEmp(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer size,
                              Employee employee) {
        return RespBean.ok("查询成功", service.getEmployeeByPage(page, size, employee));
    }


    @PostMapping
    public RespBean add(@RequestBody Employee employee) {
        if (service.add(employee) == 1) return RespBean.ok("添加成功", 1);
        return RespBean.error("添加失败");
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable Integer id) {
        if (service.delete(id) == 1) return RespBean.ok("删除成功", 1);
        return RespBean.error("删除失败");
    }


    @GetMapping("/nations")
    public RespBean getNations() {
        return RespBean.ok("查询Nations成功", nationDao.queryAll(null));
    }

    @GetMapping("/politics")
    public RespBean getPoliticsStatus() {
        return RespBean.ok("查询Politics成功", politicsStatusDao.queryAll(null));
    }

    @GetMapping("/departments")
    public RespBean getDepartments() {
        return RespBean.ok("查询Departments成功", departmentDao.queryAll(null));
    }

    @GetMapping("/jobLevels")
    public RespBean getJobLevels() {
        return RespBean.ok("查询JobLevels成功", jobLevelDao.queryAll(null));
    }

    @GetMapping("/positions")
    public RespBean getPositions() {
        return RespBean.ok("查询Positions成功", positionDao.queryAll(null));
    }

    @GetMapping("/workId")
    public RespBean getWorkId() {
        String defaultValue = "00000000";
        int i = Integer.parseInt(service.getWorkId()) + 1;
        int length = Integer.toString(i).length();
        int end = defaultValue.length() - length;
        String workId = defaultValue.substring(0, end) + i;
        return RespBean.ok("获取最新workId成功", workId);
    }

    @PutMapping
    public RespBean update(@RequestBody Employee employee) {
        if (service.update(employee) == 1) return RespBean.ok("修改成功", 1);
        return RespBean.error("修改失败");
    }

    /**
     * 导出数据
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() {
        RespPageBean bean = service.getEmployeeByPage(null, null, null);
        return ExcelOperation.employeeToExcel((List<Employee>) bean.getData());
    }
    /*  File file1 = new File("temp.xls");
        System.out.println(file1.getAbsolutePath());
        file.transferTo(file1);
    */

    /**
     * 导入数据
     */
    @PostMapping("/import")
    public RespBean importData(MultipartFile file) {
        List<Nation> nations = nationDao.queryAll(null);
        List<PoliticsStatus> politicsStatuses = politicsStatusDao.queryAll(null);
        List<Department> departments = departmentDao.queryAll(null);
        List<JobLevel> jobLevels = jobLevelDao.queryAll(null);
        List<Position> positions = positionDao.queryAll(null);
        List<Employee> list = ExcelOperation.excelToEmployee(file, nations, politicsStatuses, departments, jobLevels, positions);
        System.out.println("wait");
        int num = service.insertAll(list);
        return RespBean.ok("上传成功", num);
    }
}
