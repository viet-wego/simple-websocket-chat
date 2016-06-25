/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.viettd.simplewebsocketchat;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 *
 * @author viettd
 */
public class ChatSocket extends WebSocketAdapter {

    private String userName;

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        ChatSocketServlet.addMember(this);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        ChatSocketServlet.broadCastMessage(message, userName);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        ChatSocketServlet.removeMember(userName);
    }

    public void sendMessage(String message) {
        if (isConnected()) {
            try {
                this.getRemote().sendString(message);
            } catch (Exception e) {

            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
