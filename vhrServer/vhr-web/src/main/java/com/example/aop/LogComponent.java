package com.example.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogComponent {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.example..*.*(..))")
    public void plc() {

    }

    @Before("plc()")
    public void before(JoinPoint jp) {
        String name = jp.getSignature().getName();
        String declaringTypeName = jp.getSignature().getDeclaringTypeName();
        logger.info("beforeLog:" + "declaringTypeName:" + declaringTypeName + "method:" + name);
    }

    @AfterThrowing(value = "plc()", throwing = "e")
    public void throwing(JoinPoint jp, Exception e) {
        String name = jp.getSignature().getName();
        String declaringTypeName = jp.getSignature().getDeclaringTypeName();
        logger.warn("throwingLog:"+"declaringTypeName:"+declaringTypeName + "method:" + name + "message:" + e.getMessage());
    }
}
