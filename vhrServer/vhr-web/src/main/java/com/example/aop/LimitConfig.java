package com.example.aop;

import com.example.exception.custom.ExceedLimitException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
@ConfigurationProperties(prefix = "visit.limit")
public class LimitConfig {
    private Logger logger = LoggerFactory.getLogger(LimitConfig.class);
    private static LocalDateTime date = LocalDateTime.now();
    public int maxLimit;
    public int visitCount;

    public static void main(String[] args) {

    }

    @Pointcut("execution(* com.example.controller.employee.EmployeeController.importData(..))")
    public void pc() {
    }

    @Before("pc()")
    public void before(JoinPoint jp) throws Exception {
        if (date.getDayOfYear() != LocalDateTime.now().getDayOfYear()) {
            visitCount = 0;
            date = LocalDateTime.now();
            logger.info("刷新每日限制次数");
        }
        if (++visitCount > maxLimit) {
            logger.warn("超出每日最大访问限制"+maxLimit+"次");
            throw new ExceedLimitException("超出每日最大访问限制"+maxLimit+"次");
        }
        logger.info("maxLimit:" + maxLimit + "," + "visitCount:" + visitCount);
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }
}
