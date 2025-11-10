package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;
/*
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoorCam {

    private final RebootDevice rebootDevice;
    private final SimpMessageSendingOperations messaging;
    private final VideoRecorderService videoRecorderService;

    private String imageUrl = "http://192.168.88.54/picture/1/current/";

    private static int  fpsCount = 0;
    private static int  fpmCount = 0;
    private static int fps = 0;
    private static int fpm = 0;

    private static int resCounter = 10;
    private static int resInterval = 10; //minutes without frame before reset

    public static byte[] imageBytes;
    public static String imageString;


    @Scheduled(fixedRate = 40)
    public void getImage(){
        while (true) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000); // Set a timeout of 10 seconds
                con.setReadTimeout(5000);
                imageBytes = con.getInputStream().readAllBytes();
                imageString = Base64.getEncoder().encodeToString(imageBytes);
                fpsCount++;
                fpmCount++;
                messaging.convertAndSend("/topic/imageDoorCam", "{\"image\":\"" + imageString + "\" }");
                //videoRecorderService.setImage(imageBytes);
                //log.info("frame");
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
        //log.info(String.valueOf(fps));
        messaging.convertAndSend("/topic/frameRate", "{\"fps\":" + fps + ",\"fpm\":" + fpm + " }");
    }

    @Scheduled(fixedRate = 60000)
    private synchronized void fpmCounter() {
        fpm = fpmCount;
        fpmCount = 0;

        //Self Reboot Logic
        if (fpm == 0) resCounter--;
        else resCounter = resInterval;
        if (resCounter == 0) rebootDevice.rebootDev(RebootDevice.destination.DOORMAN);
    }
}

 */



