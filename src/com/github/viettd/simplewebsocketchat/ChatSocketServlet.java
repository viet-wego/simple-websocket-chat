/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.viettd.simplewebsocketchat;

import com.google.gson.Gson;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 *
 * @author viettd
 */
public class ChatSocketServlet extends WebSocketServlet {

    private static final Map<String, ChatSocket> SOCKETS = new HashMap<>();

    public static void addMember(ChatSocket socket) {
        int number = SOCKETS.size() + 1;
        String userName = "User_" + number;
        socket.setUserName(userName);
        SOCKETS.put(userName, socket);
        String message = userName + " joined.";
        broadCastMessage(message, "System");
    }

    public static void broadCastMessage(String message, String userName) {
        for (ChatSocket socket : SOCKETS.values()) {
            Message msg = new Message();
            msg.setAuthor(userName);
            msg.setContent(message);
            msg.setTimestamp(Calendar.getInstance().getTimeInMillis());
            socket.sendMessage(new Gson().toJson(msg));
        }
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(24 * 3600 * 1000);
        factory.register(ChatSocket.class);
    }
}
