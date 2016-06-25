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

    private static void sendMessage(ChatSocket socket, String message, String userName) {
        Message msg = new Message();
        msg.setAuthor(userName);
        msg.setContent(message);
        msg.setTimestamp(Calendar.getInstance().getTimeInMillis());
        socket.sendMessage(new Gson().toJson(msg));
    }

    private static void addSocket(String userName, ChatSocket socket) {
        socket.setUserName(userName);
        broadCastMessage(userName + " joined.", "System");
        SOCKETS.put(userName, socket);
        sendMessage(socket, String.format("Welcome %s !", userName), "System");
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(24 * 3600 * 1000);
        factory.register(ChatSocket.class);
    }

    public static void addMember(ChatSocket socket) {
        if (SOCKETS.isEmpty()) {
            addSocket("User_1", socket);
        } else {
            for (int i = 1; i <= SOCKETS.size() + 1; i++) {
                String userName = String.format("User_%s", i);
                if (!SOCKETS.containsKey(userName)) {
                    addSocket(userName, socket);
                    break;
                }
            }
        }
    }

    public static void broadCastMessage(String message, String userName) {
        for (ChatSocket socket : SOCKETS.values()) {
            sendMessage(socket, message, userName);
        }
    }

    public static void removeMember(String userName) {
        broadCastMessage(userName + " left.", "System");
        SOCKETS.remove(userName);
    }

}
