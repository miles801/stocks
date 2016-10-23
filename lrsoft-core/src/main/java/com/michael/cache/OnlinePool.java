package com.michael.cache;


import org.springframework.util.Assert;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用于记录本机的在线用户
 *
 * @author Michael
 */
public class OnlinePool {
    private static OnlinePool ourInstance = new OnlinePool();

    /**
     * key：在线用户ID
     * value：该用户对应的session
     */
    private Map<String, HttpSession> users = new HashMap<>();

    public static OnlinePool getInstance() {
        return ourInstance;
    }

    private OnlinePool() {
    }

    /**
     * 添加用户（用户成功登录）
     *
     * @param empId   用户ID
     * @param session HttpSession
     */
    public void add(String empId, HttpSession session) {
        Assert.hasText(empId, "在线用户添加失败!用户ID不能为空!");
        Assert.notNull(session, "在线用户添加失败!session不能为空!");
        users.put(empId, session);
    }

    /**
     * 删除用户（用户退出）
     * 注意：该方法仅仅是将用户从在线列表中移除，session并不关闭
     *
     * @param empId 用户ID
     * @return 返回用户的HttpSession，也可能返回null
     */
    public HttpSession remove(String empId) {
        Assert.hasText(empId, "在线用户移除失败!用户ID不能为空!");
        HttpSession session = users.get(empId);
        if (session != null) {
            users.remove(empId);
        }
        return session;
    }

    /**
     * 获取所有在线用户ID集合
     *
     * @return 在线用户集合
     */
    public Set<String> onlineEmpIds() {
        return users.keySet();
    }

    /**
     * 返回在线用户数
     *
     * @return 在线用户数
     */
    public int onlineCount() {
        return users.size();
    }

    /**
     * 获取指定用户的session，如果用户不在线，则返回null
     *
     * @param empId 用户ID
     */
    public HttpSession getSession(String empId) {
        Assert.hasText(empId, "获取用户HttpSession失败!用户ID不能为空！");
        return users.get(empId);
    }
}
