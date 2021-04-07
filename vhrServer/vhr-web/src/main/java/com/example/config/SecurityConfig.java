package com.example.config;

import com.example.entity.Hr;
import com.example.entity.help.AuthException;
import com.example.entity.help.RespBean;
import com.example.service.HrService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    HrService hrService;
    RoleDecisionManager decisionManager;
    RoleFilter roleFilter;
    VerificationCodeFilter verificationCodeFilter;

    public SecurityConfig(HrService hrService,
                          RoleDecisionManager decisionManager,
                          RoleFilter roleFilter,
                          VerificationCodeFilter verificationCodeFilter) {
        this.hrService = hrService;
        this.decisionManager = decisionManager;
        this.roleFilter = roleFilter;
        this.verificationCodeFilter = verificationCodeFilter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //org.apache.catalina.session.StandardSession.invalidate
    //org.apache.catalina.session.StandardSession.expire(boolean)
    //注册后StandardSession才能感知到Spring容器中的session创建和销毁事件
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
/*    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //配置跳过security保护的路径
        web.ignoring().antMatchers(
                "/css/**",
                "/js/**",
                "/index.html",
                "/img/**",
                "/fonts/**",
                "/favicon.ico",
                "/verifyCode");;
    }

    //1---YWRtaW46MTYxNjI0MTU4NzY2MzowZTNjMzdhNDVmMGI4NGY5YjNiNzY4YmYwNGFkMGFiNg
    //2---YWRtaW46MTYxNjI0MTU4NzY2MzowZTNjMzdhNDVmMGI4NGY5YjNiNzY4YmYwNGFkMGFiNg
    public static void main(String[] args) {
        String test = new String(Base64.getDecoder()
                .decode("YWRtaW46MTYxNjI0MTU4NzY2MzowZTNjMzdhNDVmMGI4NGY5YjNiNzY4YmYwNGFkMGFiNg"));
        System.out.println(test);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //test
       /* http.authorizeRequests().anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().rememberMe().key("test")
                .and().csrf().disable();*/
        //将验证码过滤器加在登录判断过滤器之前
        http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
//                .anyRequest().authenticated()
                //如果AccessDecision失败,跳转到/login请求
                //org.springframework.security.config.annotation.configuration.AutowireBeanFactoryObjectPostProcessor.postProcess
                //调价对象后置处理器,对FilterSecurityInterceptor类的实例后置处理,添加
                //AccessDecisionManager和SecurityMetadataSource
                //为安全和配置难度考虑,security的部分对象的部分属性并没有暴露出来,
                //无法通过bean的方式直接配置,但对于自定义需求提供了ObjectPostProcessor的方法
                //可以处理指定的----泛型类----的实例
                //在 Aware methods, InitializingBean.afterPropertiesSet()
                //之后调用ObjectPostProcessor的postProcess方法
                //https://docs.spring.io/spring-security/site/docs/5.4.5/reference/html5/#post-processing-configured-objects
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(decisionManager);
                        object.setSecurityMetadataSource(roleFilter);
                        return object;
                    }
                })
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")
                .loginPage("/login")
                .successHandler(SecurityConfig::successHandler)
                .failureHandler(SecurityConfig::failureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(SecurityConfig::logoutSuccessHandler)
                .and()
                //AbstractAuthenticationProcessingFilter.doFilter(HttpServletRequest, HttpServletResponse, FilterChain)
                //UsernamePasswordAuthenticationFilter.attemptAuthentication
                //ConcurrentSessionControlAuthenticationStrategy.onAuthentication
                .sessionManagement()
                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true)
                .and()
                .and()
                .csrf().disable()
                .exceptionHandling()
                //Used by ExceptionTranslationFilter to commence an authentication scheme.
                //对于认证异常处理
                .authenticationEntryPoint(SecurityConfig::authenticationExceptionHandler);
    }

    private static void authenticationExceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                                       AuthenticationException exception) {
        jsonResponse(response, AuthException.valueOf0(exception));
    }

    private static void failureHandler(HttpServletRequest request, HttpServletResponse response,
                                       AuthenticationException exception) {
        jsonResponse(response, false, AuthException.getReason(exception), null);
    }


    private static void successHandler(HttpServletRequest request, HttpServletResponse response,
                                       Authentication authentication) {
        Hr hr = (Hr) authentication.getPrincipal();
        hr.setPassword(null);
        jsonResponse(response, true, "登录成功", hr);
    }

    private static void logoutSuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        jsonResponse(response, true, "注销成功", null);
    }

    private static void jsonResponse(HttpServletResponse response, AuthException authException) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            RespBean respBean = RespBean.build(authException.getStatus(), authException.getReason());
            String json = new ObjectMapper().writeValueAsString(respBean);
            writer.write(json);
            writer.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void jsonResponse(HttpServletResponse response, boolean isOk, String msg, Object o) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            RespBean respBean;
            if (isOk) respBean = RespBean.ok(msg, o);
            else respBean = RespBean.error(msg, o);
            String json = new ObjectMapper().writeValueAsString(respBean);
            writer.write(json);
            writer.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
