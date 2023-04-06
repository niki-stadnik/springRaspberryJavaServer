package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        System.out.println(message);
        session.sendMessage(new TextMessage("Hi how may we help you?"));
    }

}