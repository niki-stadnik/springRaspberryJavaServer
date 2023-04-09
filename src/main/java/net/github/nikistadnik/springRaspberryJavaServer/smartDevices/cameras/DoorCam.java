package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DoorCam {
    private static String imageUrl = "http://192.168.88.69/capture";

    @Scheduled(fixedRate = 40)
    public static void getImage() throws JSONException {
        while (true) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000); // Set a timeout of 10 seconds
                con.setReadTimeout(5000);
                String imageString = Base64.getEncoder().encodeToString(con.getInputStream().readAllBytes());
                JSONObject json = new JSONObject();
                json.put("image", imageString);
                SendMessage.sendMessage("/topic/imageDoorCam", String.valueOf(json));
            } catch (IOException e) {
                System.out.println("connection with the cam has been broken");
                //throw new RuntimeException(e);
            }
            break;
        }
    }
}