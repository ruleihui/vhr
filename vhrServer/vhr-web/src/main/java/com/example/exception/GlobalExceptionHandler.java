package com.example.exception;

import com.example.entity.help.AuthException;
import com.example.entity.help.RespBean;
import com.example.entity.help.SqlException;
import com.example.exception.custom.CustomException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAccessException.class)
    public RespBean DataAccessException(DataAccessException e) {
        return RespBean.error(SqlException.getReason(e));
    }

    @ExceptionHandler(AuthenticationException.class)
    public RespBean Exception(AuthenticationException e) {
        return RespBean.error(AuthException.getReason(e));
    }

    @ExceptionHandler(CustomException.class)
    public RespBean Exception(CustomException e) {
        return RespBean.error(e.getMessage());
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public RespBean Exception(FileUploadException e) {
        return RespBean.error(e.getMessage());
    }



/*    @ExceptionHandler(Exception.class)
    public RespBean Exception(Exception e) {
        return RespBean.error("未知错误");
    }*/
}
