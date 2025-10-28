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


    public void setData (DoormanModel data){
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        active = true;
        doorOpen = data.isDoorOpen();
        doorLock = data.isDoorLock();
        doorButton = data.isDoorButton();
        bell = data.isBell();
        rfid = data.isRfid();
    }

    public void command(DoormanClientModel data){
        System.out.println(data);
        int com = data.getCommand();
        if (com == 1) rebootLightSwitch();
        if (com == 2) LightSwitchService.rebootDoorman();
    }





    @Scheduled(fixedRate = 10000)    //every 10s
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
