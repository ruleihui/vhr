package com.example.config;

import com.example.entity.help.RespBean;
import com.example.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class VerificationCodeFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpServletResponse res = ((HttpServletResponse) response);
        if ("POST".equals(req.getMethod()) && "/doLogin".equals(req.getServletPath())) {
            //登录请求
            String code = req.getParameter("code");
            String verifyCode = ((String) req.getSession().getAttribute("verifyCode"));
            if (Utils.isNull(code) || Utils.isNull(verifyCode) || !(code.toLowerCase().equals(verifyCode.toLowerCase()))) {
                //验证码不正确
                res.setContentType("application/json;charset=utf-8");
                try (PrintWriter writer = res.getWriter()) {
                    String returnValue = new ObjectMapper().writeValueAsString(RespBean.error("验证码错误"));
                    writer.write(returnValue);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else chain.doFilter(request, response);
        } else chain.doFilter(request, response);

    }
}
