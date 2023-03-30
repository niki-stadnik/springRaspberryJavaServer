package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.bathroomFan;

import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BathroomFanService {

    private static boolean flag = false;
    private static boolean disregard = false;

    private static Double bathTemp = null;
    private static Double bathHum = null;
    private static Double bathLight = null;
    private static boolean bathFan = false;


    private static int bathroomFanDelay = 30;
    private static int counterForBathroomFan = 0;
    private static int bathroomFanCycles = 0;
    JSONObject jo;

    public BathroomFanService() {
    }


    public void setData(BathroomFanModel data) {
        System.out.println(data);
        if (!flag) {
            if (!disregard){
                flag = true;
            }else
            disregard = false;
        }
        bathTemp = data.getBathTemp();
        bathTemp -= 2.2;    //calibrating
        System.out.println(bathTemp);
        TempStorage.mapStorage.put("bathTemp", bathTemp);
        bathHum = data.getBathHum();
        bathHum += 24.7;      //calibrating
        System.out.println(bathHum);
        TempStorage.mapStorage.put("bathHum", bathHum);
        bathLight = data.getBathLight();
        TempStorage.mapStorage.put("bathLight", bathLight);
        bathFan = data.isBathFan();
        TempStorage.mapStorage.put("bathFan", bathFan);
    }


    @Scheduled(fixedRate = 500)    //500
    private void Auto() {
        bathroomFanCycles = bathroomFanDelay * 2;   //it's *2 because the schedule is 2 times per second

        //todo does the 30 sek delay work with the changes (not calculated in auto but in class)

        if (bathHum != null && bathLight != null && bathTemp != null) {
            if ((bathHum > 60 || bathLight >= 0.01) && !bathFan) {
                switchON();
            } else if (bathHum < 55 && bathLight < 0.01 && bathFan) {
                System.out.println(counterForBathroomFan);
                System.out.println(bathroomFanCycles);
                if (counterForBathroomFan >= bathroomFanCycles) {
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
        if(flag) {
            flag = false;
            disregard = true;
            System.out.println("switch it on");
            jo = new JSONObject();
            jo.put("data", true);
            String data = jo.toString();
            SendMessage.sendMessage("/topic/bathroomFan", data);
        }
    }

    public synchronized void switchOFF() {
        if(flag) {
            flag = false;
            disregard = true;
            System.out.println("switch it off");
            jo = new JSONObject();
            jo.put("data", false);
            String data = jo.toString();
            SendMessage.sendMessage("/topic/bathroomFan", data);
        }
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
