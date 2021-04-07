package com.example.dao;

import org.apache.ibatis.annotations.Param;

public interface SalSobCfgDao {

    Integer update(@Param("eId") Integer employeeId,
                   @Param("sId") Integer salaryId);

    Integer add(@Param("eId") Integer employeeId,
                @Param("sId") Integer salaryId);
}
