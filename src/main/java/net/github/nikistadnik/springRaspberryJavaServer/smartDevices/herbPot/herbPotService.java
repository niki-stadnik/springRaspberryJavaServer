package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.herbPot;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman.DoormanClientModel;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchService;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class herbPotService {

    private static float temp1 = 0;
    private static float temp2 = 0;
    private static int moisture1 = 0;
    private static int moisture2 = 0;
    private static float t1 = 0;
    private static float t2 = 0;
    private static int moisture1Percent = 0;
    private static int moisture2Percent = 0;
    private static boolean watered = false;


    static JSONObject jo;

    public void setData(herbPotModel data) {
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        moisture1 = data.getMoisture1();
        moisture2 = data.getMoisture2();
        watered = data.isWatered();
        temp1 = data.getTemp1();
        temp2 = data.getTemp2();
    }

    public void command(herbPotClientModel data){
        System.out.println(data);
        int water = data.getWater();
        boolean restart = data.isRestart();
        JSONObject jo = new JSONObject();
        jo.put("water", water);
        jo.put("restart", restart);
        String mess = jo.toString();
        SendMessage.sendMessage("/topic/herbPot", mess);
    }

    @Scheduled(fixedRate = 1000)
    private synchronized void potUpdate() {
        //(2500 - moisture) / (2500 - 1000)) * 100;
        t1 = (float) (2500 - moisture1) / 15;
        t2 = (float) (2500 - moisture2) / 15;
        moisture1Percent = (int) t1;
        moisture2Percent = (int) t2;
        jo = new JSONObject();
        jo.put("temp1", temp1);
        jo.put("temp2", temp2);
        jo.put("moisture1", moisture1Percent);
        jo.put("moisture2", moisture2Percent);
        String data = jo.toString();
        //System.out.println(data);
        SendMessage.sendMessage("/topic/clientHerbPot", data);
    }

    //moisture value of 2500 is bone dray
    //moisture value of 1000 is fully saturated

}
