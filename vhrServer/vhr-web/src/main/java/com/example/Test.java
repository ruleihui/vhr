package com.example;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws IllegalAccessException, ParseException {
//        Date date = DateFormat.getDateTimeInstance().parse("1990-01-01 12:00:00");
//        String format = DateFormat.getDateTimeInstance().format(new Date());
/*        try {
            Class<?> birthday = Employee.class.getDeclaredField("birthday").getType();
            System.out.println(birthday.equals(Date.class));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }*/
/*        Object o = new Object();
        System.out.println(o);
        int hashCode = o.hashCode();
        String hexString = Integer.toHexString(hashCode);
        System.out.println(hashCode);
        System.out.println(hexString);
        //Object.equals判断为true
        //要求两者指针相同,指向同一块内存地址
        //表明两者为同一对象
        o.equals(new Object());*/
        //获取所有部门
        //遍历所有部门,用每个部门对象的id去filter所有部门对象,将parentId==Id
        //的对象添加到当前遍历到的部门对象
        //通过这样替代递归查询数据库获取子部门的方法
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9).stream();
        stream.filter(it->it%2==0).forEach(System.out::println);
    }
}
