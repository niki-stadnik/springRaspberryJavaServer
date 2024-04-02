package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenESP2;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip.kitchenLEDstripService;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class kitchenESP2Service {

    private static boolean active = false;

    private static boolean here = false;
    public void setData(kitchenESP2Model data){
        active = true;
        System.out.println(data);
        here = data.isHere();
    }

    @Scheduled(fixedRate = 300000)    //every 5m
    private synchronized void selfReboot(){
        if (!active) kitchenLEDstripService.rebootkitchen2();
        active = false;
    }

    public static synchronized void rebootkitchenLEDstrip() {
        System.out.println("reboot kitchenLEDstrip");
        JSONObject jo = new JSONObject();
        jo.put("relayRestart", true);
        String data = jo.toString();
        SendMessage.sendMessage("/topic/restartKitchen1", data);
    }

}
