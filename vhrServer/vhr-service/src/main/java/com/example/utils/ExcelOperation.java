package com.example.utils;

import com.example.entity.*;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelOperation {
    //自定义事件格式
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    //employee类的所有字段
    static Field[] declaredFields = Employee.class.getDeclaredFields();

    public static ResponseEntity<byte[]> employeeToExcel(List<Employee> list) {
        //1.创建一个Excel文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2.创建文档摘要
        workbook.createInformationProperties();
        //3.获取并配置文档摘要信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        setDocumentSummaryInformation(docInfo);
        //4.获取摘要信息
        SummaryInformation summaryInfo = workbook.getSummaryInformation();
        setSummaryInformation(summaryInfo);
        //5.创建工作表
        HSSFSheet employeeSheet = workbook.createSheet("员工信息表");
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //6.创建标题行
        HSSFRow titleRow = employeeSheet.createRow(0);
        //创建标题列,并设置样式
        setTitleCell(titleRow, headerStyle);
        //设置每列的宽度
        setCellWidth(employeeSheet);
        //创建数据行和数据列,并设置数据
        setEmployeeData(employeeSheet, list);

        return buildResponseEntity(workbook);
    }

    private static ResponseEntity<byte[]> buildResponseEntity(HSSFWorkbook workbook) {
        HttpHeaders headers = new HttpHeaders();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            String filename = new String("员工表.xls".getBytes("UTF-8"), "ISO-8859-1");
            headers.setContentDispositionFormData("attachment", filename);
            workbook.write(stream);
            return new ResponseEntity<byte[]>(stream.toByteArray(), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setCellWidth(HSSFSheet employeeSheet) {
        InputStream excelTitle = ExcelOperation.class.getClassLoader().getResourceAsStream("ExcelTitle.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(excelTitle))) {
            String s = reader.readLine();
            //读取第二行的宽度数据
            s = reader.readLine();
            String[] titles = s.split(",");
            for (int i = 0; i < titles.length; i++) {
                //移除对其空格数据
                String trim = titles[i].trim();
                int num = Integer.valueOf(trim);
                //Set the width (in units of 1/256th of a character width)
                employeeSheet.setColumnWidth(i, num * 256);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setEmployeeData(HSSFSheet employeeSheet, List<Employee> list) {
        //已经有一个标题行,所以数据从1开始
        for (int i = 1; i < list.size(); i++) {
            HSSFRow row = employeeSheet.createRow(i);
            setEmployeeCellData(list.get(i), row);
        }
    }

    private static void setEmployeeCellData(Employee employee, HSSFRow row) {
        int titleNum = row.getSheet().getRow(0).getPhysicalNumberOfCells();
        //i从1开始跳过序列化ID
        for (int i = 1; i < titleNum; i++) {
            Field field = declaredFields[i];
            field.setAccessible(true);
            try {
                Object o = field.get(employee);
                //从第一列开始赋值
                HSSFCell cell = row.createCell(i - 1);
                String value = null;
                if (o != null) {
                    value = o.toString();
                    if (o instanceof Date) value = dateFormat.format(o);
                    String name = resolveEntity(o);
                    if (name != null) value = name;
                    cell.setCellValue(value);
                }
                cell.setCellValue(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static String resolveEntity(Object o) {
        String packagePath = o.getClass().getPackage().getName();
        if (packagePath.contains("com.example.entity")) {
            try {
                Field name = o.getClass().getDeclaredField("name");
                name.setAccessible(true);
                return name.get(o).toString();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setTitleCell(HSSFRow titleRow, HSSFCellStyle style) {
        InputStream excelTitle = ExcelOperation.class.getClassLoader().getResourceAsStream("ExcelTitle.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(excelTitle))) {
            String s = reader.readLine();
            String[] titles = s.split(",");
            for (int i = 0; i < titles.length; i++) {
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(style);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setSummaryInformation(SummaryInformation summaryInfo) {
        //标题
        summaryInfo.setTitle("employeeInfoTable");
        //作者
        summaryInfo.setAuthor("hr");
        //注释
        summaryInfo.setComments("这是一条注释");
    }

    private static void setDocumentSummaryInformation(DocumentSummaryInformation docInfo) {
        //文档类别
        docInfo.setCategory("员工信息");
        //文档管理员
        docInfo.setManager("hr");
        //公司
        docInfo.setCompany("example");

    }

    /**
     * 将Excel解析为employee集合
     *
     * @param file
     * @param nations
     * @param politicsStatuses
     * @param departments
     * @param jobLevels
     * @param positions
     * @return
     */
    public static List<Employee> excelToEmployee(MultipartFile file, List<Nation> nations, List<PoliticsStatus> politicsStatuses, List<Department> departments, List<JobLevel> jobLevels, List<Position> positions) {
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            //1.根据上传文件创建workbook对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2.处理每一个表单
            workbook.sheetIterator()
                    .forEachRemaining(it -> resolveSheet(it, employees, nations, politicsStatuses, departments, jobLevels, positions));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static void resolveSheet(Sheet sheet, List<Employee> employees, List<Nation> nations, List<PoliticsStatus> politicsStatuses, List<Department> departments, List<JobLevel> jobLevels, List<Position> positions) {
        int rows = sheet.getPhysicalNumberOfRows();
        //获取标题行的列数,作为赋值属性的长度
        int cells = sheet.getRow(0).getPhysicalNumberOfCells();
        //从1开始,跳过标题行
        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);
            //每一行初始化一个employee实例
            Employee employee = new Employee();
            for (int j = 1; j < cells; j++) {
                //从1开始,跳过序列化ID
                Field field = declaredFields[j];
                field.setAccessible(true);
                //获取当前行每一列的数据
                //从0开始,从列数据于对象字段对应
                Cell cell = row.getCell(j - 1);
                String value = cell != null ? cell.getStringCellValue() : "";
                try {
                    //如果为null跳过赋值,使用实例默认值
                    if (Utils.isNull(value)) continue;
                    else if (field.getType().equals(Date.class)) field.set(employee, dateFormat.parse(value));
                    else if (field.getType().equals(String.class)) field.set(employee, value);
                    else if (field.getType().equals(Double.class)) field.set(employee, Double.valueOf(value));
                    else if (field.getType().equals(Integer.class)) field.set(employee, Integer.valueOf(value));
                        //民族
                    else if (field.getType().equals(Nation.class)) field.set(employee, ValueResolve(value, nations));
                        //政治面貌
                    else if (field.getType().equals(PoliticsStatus.class))
                        field.set(employee, ValueResolve(value, politicsStatuses));
                        //部门
                    else if (field.getType().equals(Department.class))
                        field.set(employee, ValueResolve(value, departments));
                        //职称
                    else if (field.getType().equals(JobLevel.class))
                        field.set(employee, ValueResolve(value, jobLevels));
                        //职位
                    else if (field.getType().equals(Position.class))
                        field.set(employee, ValueResolve(value, positions));
                } catch (IllegalAccessException | ParseException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            employees.add(employee);
        }
    }

    public static void main(String[] args) throws ParseException {
    }
    public static <T> T ValueResolve(String value, List<T> objs) throws NoSuchFieldException, IllegalAccessException {
        T obj = objs.get(0);
        Field name = obj.getClass().getDeclaredField("name");
        name.setAccessible(true);
        for (T o : objs) {
            String name0 = ((String) name.get(o));
            if (value.equals(name0)) return o;
        }
        return null;
    }
}
