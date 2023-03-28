package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BathroomFanService {

    private static Double bathTemp = null;
    private static Double bathHum = null;
    private static Double bathLight = null;
    private static boolean bathFan = false;


    double BathroomFanDelay;
    int counterForBathroomFan = 0;
    JSONObject jo;

    public BathroomFanService() {
        TempStorage.mapStorage.put("bathroomFanMode", "auto");
        TempStorage.mapStorage.put("bathroomFanDelay", 30);
    }


    public void setData(BathroomFanModel data){
        System.out.println(data);
        bathTemp = data.getBathTemp();
        TempStorage.mapStorage.put("bathTemp", bathTemp);
        bathHum = data.getBathHum();
        TempStorage.mapStorage.put("bathHum", bathHum);
        bathLight = data.getBathLight();
        TempStorage.mapStorage.put("bathLight", bathLight);
        bathFan = data.isBathFan();
        TempStorage.mapStorage.put("bathFan", bathFan);
    }

    @Scheduled(fixedRate = 100)
    private void Auto() {
        BathroomFanDelay = Double.parseDouble(TempStorage.mapStorage.get("bathroomFanDelay").toString()) / (100 / 1000);

        //todo does the 30 sek delay work with the changes (not calculated in auto but in class)

        if (bathHum != null && bathLight != null && bathTemp != null) {
            if ((bathHum > 60 || bathLight >= 0.01) && !bathFan) {
                switchON();
            } else if (bathHum < 50 && bathLight < 0.01 && bathFan) {
                if (counterForBathroomFan >= BathroomFanDelay) {
                switchOFF();
                } else {
                    counterForBathroomFan++;
                    //System.out.println("off delay: " + counterForBathroomFan);
                }
            } else {
                //fanCom = 0;
                if (bathLight >= 0.01) counterForBathroomFan = 0;
            }

        } else {
            //System.out.println("no data from bathroomFan");
        }
    }


    public synchronized void switchON() {
        System.out.println("switch it on");
        jo = new JSONObject();
        jo.put("data", true);
        String data = jo.toString();
        SendMessage.sendMessage("/topic/bathroomFan", data);
    }

    public synchronized void switchOFF() {
        System.out.println("switch it off");
        jo = new JSONObject();
        jo.put("data", false);
        String data = jo.toString();
        SendMessage.sendMessage("/topic/bathroomFan", data);
    }




    public static Double getBathTemp() {
        return bathTemp;
    }

    public static void setBathTemp(Double bathTemp) {
        BathroomFanService.bathTemp = bathTemp;
    }

    public static Double getBathHum() {
        return bathHum;
    }

    public static void setBathHum(Double bathHum) {
        BathroomFanService.bathHum = bathHum;
    }

    public static Double getBathLight() {
        return bathLight;
    }

    public static void setBathLight(Double bathLight) {
        BathroomFanService.bathLight = bathLight;
    }

    public static boolean isBathFan() {
        return bathFan;
    }

    public static void setBathFan(boolean bathFan) {
        BathroomFanService.bathFan = bathFan;
    }

}
