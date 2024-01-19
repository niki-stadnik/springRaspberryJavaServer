package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchService;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DoormanService {

    private static boolean active = false;

    private static boolean doorOpen = false;
    private static boolean doorLock = false;
    private static boolean doorButton = false;
    private static boolean bell = false;
    private static boolean rfid = false;


    public DoormanService() {
    }

    public void setData (DoormanModel data){
        System.out.println(data);
        active = true;
        doorOpen = data.isDoorOpen();
        doorLock = data.isDoorLock();
        doorButton = data.isDoorButton();
        bell = data.isBell();
        rfid = data.isRfid();
    }







    @Scheduled(fixedRate = 5000)    //every 5s
    private synchronized void selfReboot(){
        if (!active) LightSwitchService.rebootDoorman();
        active = false;
    }

    public static synchronized void rebootLightSwitch() {
        System.out.println("reboot LightSwitch");
        JSONObject jo = new JSONObject();
        jo.put("relayRestart", true);
        String data = jo.toString();
        SendMessage.sendMessage("/topic/doorman", data);
    }

}
