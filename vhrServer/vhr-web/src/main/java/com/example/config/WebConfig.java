package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new FileInterceptor());
    }

    //避免json序列化时区的问题
    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        DateFormat dateFormat = objectMapper.getDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    public static void main(String[] args) {
        Date date = new Date("[native Date Mon Feb 08 2021 00:00:00 GMT+0800 (China Standard Time)]");
        System.out.println(date);
    }

    //org.springframework.core.convert.support.GenericConversionService
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Date.class, WebConfig::conversion);
    }

    //将字符串时间戳转换为时间
    public static Date conversion(String origin) {
        return new Date(Long.valueOf(origin));
    }

    class FileInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String uri = request.getRequestURI();
            if (uri.contains("upload")) {
                uri = uri.replaceFirst("/upload/", "");
                uri= URLDecoder.decode(uri,"utf-8");
                File file = new File(uri);
                try (
                        ServletOutputStream outputStream = response.getOutputStream();
                        FileInputStream inputStream = new FileInputStream(file)
                ) {

                    byte[] _1MB = new byte[1024 * 1024];
                    while (inputStream.read(_1MB) != -1) {
                        outputStream.write(_1MB);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
            return true;
        }
    }
}
