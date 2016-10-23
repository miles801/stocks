package com.michael.message;

import com.michael.core.security.LoginInfo;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Socket建立连接（握手）和断开
 * 在我手之后，往WebSocket的session中设置当前用户的参数
 * 即：从当前session中获取当前用户信息，并设置到websocket的attributes中
 *
 * @author Michael
 */
public class HandShake implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpSession session = servletRequest.getServletRequest().getSession(false);
        //
        String userId = (String) session.getAttribute(LoginInfo.EMPLOYEE);
        System.out.println("Websocket:用户[ID:" + session.getAttribute(LoginInfo.EMPLOYEE_NAME) + "]已经建立连接");
        // 标记用户
        if (userId == null) {
            return false;
        }
        map.put(LoginInfo.EMPLOYEE, userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
