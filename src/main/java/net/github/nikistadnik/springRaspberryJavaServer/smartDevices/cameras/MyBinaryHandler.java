package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorCam.DoorCamClientModel;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.util.Arrays;
import java.util.Base64;

@Component
public class MyBinaryHandler extends BinaryWebSocketHandler {

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        //System.out.println("yes");
        // handle the binary message here
        byte[] binaryData = message.getPayload().array();
        // process the binary data as needed
        //System.out.println(Arrays.toString(binaryData));
        String base64ImageData = Base64.getEncoder().encodeToString(binaryData);
        JSONObject json = new JSONObject();
        json.put("image", base64ImageData);
        SendMessage.sendMessage("/topic/imageDoorCam", String.valueOf(json));
    }

}
