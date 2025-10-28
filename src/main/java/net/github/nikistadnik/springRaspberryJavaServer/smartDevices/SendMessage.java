package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendMessage {

    private static SimpMessagingTemplate messagingTemplate = null;

    public SendMessage(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public static void sendMessage(String address, String data) {
        messagingTemplate.convertAndSend(address, data);
        //System.out.println(data);
    }
}