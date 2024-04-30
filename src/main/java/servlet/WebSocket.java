package servlet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocket {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Open Connection ...");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public String onMessage(String message) {
        System.out.println("Message from the client: " + message);
        String echoMsg = "Echo from the server : " + message;
        return echoMsg;
    }
}