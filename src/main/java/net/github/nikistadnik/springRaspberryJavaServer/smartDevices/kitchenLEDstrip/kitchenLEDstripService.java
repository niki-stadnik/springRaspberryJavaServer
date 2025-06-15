package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenESP2.kitchenESP2Service;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class kitchenLEDstripService {

    private static boolean active = false;

    private static int duty = 0;
    private static int newDuty = 0;
    private static int time = 0;
    private static int from;
    private static int to;
    private static int over;

    public kitchenLEDstripService() {
    }

    public void setData (kitchenLEDstripModel data){
        active = true;
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        duty = data.getDuty();
        if (duty != newDuty){
            stripControl();
        }
    }

    public void command (kitchenLEDstripClientModel data){
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        int com = data.getCommand();
        if (com == 1) rebootkitchen2();
        if (com == 2) kitchenESP2Service.rebootkitchenLEDstrip();
        if (com == 3) reloadPage();
        if (com == 4) {
            newDuty = data.getDuty();
            time = data.getTime();
            stripControl();
        }
    }

    private static void stripControl(){
        if (newDuty != duty){
            System.out.println("led strip change");
            JSONObject jo = new JSONObject();
            jo.put("from", duty);
            jo.put("to", newDuty);
            jo.put("over", time);
            String data = jo.toString();
            SendMessage.sendMessage("/topic/kitchenStrip", data);
        }
    }

    //LightSwitchService calls when a change is detected in the kitchen light
    public static synchronized void receiveAlert(boolean state){
        if (state){
            newDuty = 255;
            time = 10;
            stripControl();
        }else{
            newDuty = 0;
            time = 10;
            stripControl();
        }
    }

    private void reloadPage(){
        //System.out.println("init client");
        JSONObject jo = new JSONObject();
        jo.put("duty", duty);
        String dataOut = jo.toString();
        SendMessage.sendMessage("/topic/clientKitchenStrip", dataOut);
    }

    @Scheduled(fixedRate = 30000)    //every 30s
    private synchronized void selfReboot(){
        if (!active) kitchenESP2Service.rebootkitchenLEDstrip();
        active = false;
    }

    public static synchronized void rebootkitchen2() {
        System.out.println("reboot kitchen2");
        JSONObject jo = new JSONObject();
        jo.put("relayRestart", true);
        String data = jo.toString();
        SendMessage.sendMessage("/topic/restartKitchen2", data);
    }
}
