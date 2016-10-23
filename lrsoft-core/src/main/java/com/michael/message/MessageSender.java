package com.michael.message;

import com.michael.core.pool.ThreadPool;
import org.springframework.util.Assert;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

import static com.michael.message.WebSocketHandlerImpl.userSocketSessionMap;

/**
 * @author Michael
 */
public class MessageSender {
    private static MessageSender ourInstance = new MessageSender();

    public static MessageSender getInstance() {
        return ourInstance;
    }

    private MessageSender() {
    }


    /**
     * 给所有在线用户发送消息
     *
     * @param message 文本消息
     * @throws IOException
     */
    public void broadcast(final TextMessage message) {

        // 多线程群发
        for (final Map.Entry<String, WebSocketSession> entry : userSocketSessionMap.entrySet()) {
            if (entry.getValue().isOpen()) {
                ThreadPool.getInstance().execute(new Runnable() {
                    public void run() {
                        try {
                            if (entry.getValue().isOpen()) {
                                entry.getValue().sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userId  消息接收这
     * @param message 消息内容
     * @throws IOException
     */
    public void send(String userId, TextMessage message) {
        WebSocketSession session = WebSocketHandlerImpl.userSocketSessionMap.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                Assert.isTrue(false, "消息发送失败!" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
