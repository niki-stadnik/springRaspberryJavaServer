package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchService;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class DoorCam {
    //private String imageUrl = "http://192.168.88.69/capture";
    private String imageUrl = "http://192.168.88.54/picture/1/current/";

    private static int  fpsCount = 0;
    private static int  fpmCount = 0;
    private static int fps = 0;
    private static int fpm = 0;
    static JSONObject jo;

    private static int resCounter = 10;
    private static int resInterval = 10; //minutes without frame before reset

    @Scheduled(fixedRate = 40)
    public void getImage(){
        while (true) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000); // Set a timeout of 10 seconds
                con.setReadTimeout(5000);
                String imageString = Base64.getEncoder().encodeToString(con.getInputStream().readAllBytes());
                fpsCount++;
                fpmCount++;
                JSONObject json = new JSONObject();
                json.put("image", imageString);
                SendMessage.sendMessage("/topic/imageDoorCam", String.valueOf(json));
            } catch (IOException e) {
                //System.out.println("connection with the cam has been broken");
                //throw new RuntimeException(e);
            }
            break;
        }
    }

    @Scheduled(fixedRate = 1000)
    private synchronized void fpsCounter() {
        fps = fpsCount;
        fpsCount = 0;
        //System.out.print("FPS: ");
        //System.out.println(fps);

    //send fps AND fpm data
        jo = new JSONObject();
        jo.put("fps", fps);  //on off state for fan and not lights
        jo.put("fpm", fpm);  // all the other are light pulses
        String data = jo.toString();
        SendMessage.sendMessage("/topic/frameRate", data);
    }
    @Scheduled(fixedRate = 60000)
    private synchronized void fpmCounter() {
        fpm = fpmCount;
        fpmCount = 0;
        //System.out.print("FPM: ");
        //System.out.println(fpm);

    //Self Reboot Logic
        if (fpm == 0) resCounter--;
        else resCounter = resInterval;
        if (resCounter == 0) LightSwitchService.rebootDoorman();
    }
}