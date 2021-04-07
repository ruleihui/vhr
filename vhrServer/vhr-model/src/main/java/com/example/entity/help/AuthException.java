package com.example.entity.help;

public enum AuthException {
    OtherException(500, "其它认证异常,请联系管理员"),
    LockedException(500, "账户被锁定,请联系管理员"),
    AccountExpiredException(500, "账户过期,请联系管理员"),
    DisabledException(500, "账户被禁用,请联系管理员"),
    SessionAuthenticationException(500, "当前用户已登录,请联系管理员"),
    //合并账户和密码错误,防止撞库
    BadCredentialsException(500, "用户名或密码输入错误,请重新输入"),
    UsernameNotFoundException(500,"用户名不存在,请重新输入"),
    //当跨域访问或登录失效时
    InsufficientAuthenticationException(401, "认证失败,您已经被挤下线,请尝试重新登录/联系管理员"),
    CredentialsExpiredException(401, "认证过期,请尝试重新登录/联系管理员");

    private String reason;
    public String getReason() {
        return reason;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public static AuthException valueOf0(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return OtherException;
        }
    }
    public static AuthException valueOf0(Exception ex) {
        try {
            String name=ex.getClass().getSimpleName();
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return OtherException;
        }
    }

    public static String getReason(Exception e) {
        StringBuilder reason = new StringBuilder("出错:");
        reason.append(AuthException.valueOf0(e).getReason());
        return reason.toString();
    }

    AuthException(String reason) {
        this.reason = reason;
    }

    AuthException(Integer status, String reason) {
        this.status = status;
        this.reason = reason;
    }
}
