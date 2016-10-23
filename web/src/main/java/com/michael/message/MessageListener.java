package com.michael.message;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael
 */
@ServerEndpoint(
        value = "/messages"
)
public class MessageListener {

    // 持有所有的连接
    public static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        for (Session s : SESSIONS) {
            // 排除自身
            if (s.getId().equals(session.getId())) {
                continue;
            }
            try {
                s.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                SESSIONS.remove(s);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        SESSIONS.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        SESSIONS.remove(session);
    }
}
