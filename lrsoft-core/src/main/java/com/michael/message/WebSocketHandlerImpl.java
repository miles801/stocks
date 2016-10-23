package com.michael.message;

import com.michael.core.security.LoginInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * websocket的消息处理器
 *
 * @author Michael
 */
@Component
class WebSocketHandlerImpl implements WebSocketHandler {

    private Logger logger = Logger.getLogger(WebSocketHandlerImpl.class);
    /**
     * key：用户ID
     * value：WebSocketSession
     */
    static final Map<String, WebSocketSession> userSocketSessionMap = new HashMap<String, WebSocketSession>();


    /**
     * 建立连接后
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String empId = (String) session.getAttributes().get(LoginInfo.EMPLOYEE);
        logger.info("WebSocket--用户[" + empId + "]建立连接!");
        if (userSocketSessionMap.get(empId) == null) {
            userSocketSessionMap.put(empId, session);
        }
    }

    /**
     * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
     */
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message.getPayloadLength() == 0) return;
        String msgText = message.getPayload().toString();
        logger.info("接收到来自客户端的消息：" + msgText);

        // 将消息返回给原来的对象
//        MessageSender.getInstance().send((String) session.getAttributes().get(LoginInfo.EMPLOYEE), new TextMessage(msgText));
    }

    /**
     * 消息传输错误处理
     */
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String userId = (String) session.getAttributes().get(LoginInfo.EMPLOYEE);
        if (session.isOpen()) {
            session.close();
        }
        userSocketSessionMap.remove(userId);
    }

    /**
     * 关闭连接后
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("Websocket:" + session.getId() + "已经关闭");
        String userId = (String) session.getAttributes().get(LoginInfo.EMPLOYEE);
        userSocketSessionMap.remove(userId);
        logger.info("Socket会话已经移除:用户ID" + userId);
    }

    public boolean supportsPartialMessages() {
        return false;
    }


}
