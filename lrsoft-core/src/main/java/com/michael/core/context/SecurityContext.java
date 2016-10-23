package com.michael.core.context;

/**
 * 安全上下文，用于在任意层方便的获取当前登录用户的部分信息
 * Created by Michael on 2014/10/19.
 */
public class SecurityContext {
    /**
     * 登录信息
     */
    private static ThreadLocal<Login> _login = new ThreadLocal<Login>();


    /**
     * 全部移除
     */
    public static void remove() {
        _login.remove();
    }

    public static void set(Login login) {
        _login.set(login);
    }

    public static Login get() {
        Login login = _login.get();
        if (login == null) {
            login = new Login();
        }
        return login;
    }

    /**
     * 获取员工ID
     *
     * @return 员工ID
     */
    public static String getEmpId() {
        return get().getEmpId();
    }

    /**
     * 获取员工编号（可能为空）
     *
     * @return 员工编号（注意，可能没有值）
     */
    public static String getEmpCode() {
        return get().getEmpCode();
    }

    /**
     * 获取登录用户名
     *
     * @return 登录用户名
     */
    public static String getLoginName() {
        return get().getLoginName();
    }

    /**
     * 获取员工的直属机构ID
     *
     * @return 机构ID
     */
    public static String getOrgId() {
        return get().getOrgId();
    }

    /**
     * 获取员工的名称
     *
     * @return 员工名称
     */
    public static String getEmpName() {
        return get().getEmpName();
    }

    /**
     * 获取员工所属机构的名称
     *
     * @return 机构名称
     */
    public static String getOrgName() {
        return get().getOrgName();
    }

}
