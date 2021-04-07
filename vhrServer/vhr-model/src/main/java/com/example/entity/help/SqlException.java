package com.example.entity.help;


public enum SqlException {
    OtherException("其它数据访问异常,请联系管理员"),
    DataIntegrityViolationException("当前数据存在关联异常,无法处理"),
    DuplicateKeyException("值不能重复");
    private String reason;

    public String getReason() {
        return reason;
    }
    public static String getReason(Exception e) {
        StringBuilder reason = new StringBuilder("出错:");
        String simpleName = e.getClass().getSimpleName();
        reason.append(SqlException.valueOf0(simpleName).getReason());
        return reason.toString();
    }
    public static SqlException valueOf0(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return OtherException;
        }
    }

    SqlException(String reason) {
        this.reason = reason;
    }
}
