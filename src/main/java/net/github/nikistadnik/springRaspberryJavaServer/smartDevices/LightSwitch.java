package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import net.github.nikistadnik.springRaspberryJavaServer.CentralCommand;
import net.github.nikistadnik.springRaspberryJavaServer.Clients;
import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LightSwitch {

    private final Clients clt;

    int rInt = CentralCommand.getRepeatInterval();
    double spamDelay = 2 / ((double)rInt / 1000 );
    int counterSpamDelay = 0;

    JSONObject jo;
    JSONObject ja;

    public static boolean fuseBoxFan = false;
    public static Double fuseBoxTemp = null;
    public static Double fuseBoxHum = null;
    public static Double[] lightAmps = new Double[9];

    boolean[] lightStatus = new boolean[9];



    @Autowired
    LightSwitch(Clients clt) {
        this.clt = clt;
        for (int i = 0; i < 9; i++){
            lightAmps[i] = null;
        }
    }


    public synchronized void Auto() {
        takeData(); //every 10ms = repeatInterval
        fanControl();
        //lightsControl();
        if (counterSpamDelay > 0) {
            counterSpamDelay--;
        }
    }

    void lightsControl(){
       //todo auto lights
    }

    void fanControl(){
        if (fuseBoxTemp != null && fuseBoxHum != null){
            if (fuseBoxTemp > 80 || fuseBoxTemp < 0){
                //high alert
                //send notifications to app
                if (counterSpamDelay == 0) {
                    boolean[] alloff = {false, false, false, false, false, false, false, false, false};
                    for (int i = 1; i < 9; i++){
                        if (lightStatus[i]){
                            alloff[i] = true;
                        }
                    }
                    ChangeState(alloff);
                    counterSpamDelay = (int) spamDelay;
                }
            }
            if ((fuseBoxTemp > 30 || fuseBoxHum > 60) && !fuseBoxFan){
                if (counterSpamDelay == 0) {
                    boolean[] fanon = {true, false, false, false, false, false, false, false, false};
                    ChangeState(fanon);
                    counterSpamDelay = (int)spamDelay;
                }
            }else if (fuseBoxFan){
                if (counterSpamDelay == 0) {
                    boolean[] fanoff = {false, false, false, false, false, false, false, false, false};
                    ChangeState(fanoff);
                    counterSpamDelay = (int) spamDelay;
                }
            }
        }
    }

    void takeData(){
        if (TempStorage.mapStorage.get("fuseBoxFan") != null) {
            fuseBoxFan = (boolean) TempStorage.mapStorage.get("fuseBoxFan");
        }
        if (TempStorage.mapStorage.get("fuseBoxTemp") != null) {
            fuseBoxTemp = Double.valueOf(TempStorage.mapStorage.get("fuseBoxTemp").toString());
        }
        if (TempStorage.mapStorage.get("fuseBoxHum") != null) {
            fuseBoxHum = Double.valueOf(TempStorage.mapStorage.get("fuseBoxHum").toString());
        }
        if (TempStorage.mapStorage.get("light0") != null) {
            lightAmps[1] = Double.valueOf(TempStorage.mapStorage.get("light0").toString());
        }
        if (TempStorage.mapStorage.get("light1") != null) {
            lightAmps[2] = Double.valueOf(TempStorage.mapStorage.get("light1").toString());
        }
        if (TempStorage.mapStorage.get("light2") != null) {
            lightAmps[3] = Double.valueOf(TempStorage.mapStorage.get("light2").toString());
        }
        if (TempStorage.mapStorage.get("light3") != null) {
            lightAmps[4] = Double.valueOf(TempStorage.mapStorage.get("light3").toString());
        }
        if (TempStorage.mapStorage.get("light4") != null) {
            lightAmps[5] = Double.valueOf(TempStorage.mapStorage.get("light4").toString());
        }
        if (TempStorage.mapStorage.get("light5") != null) {
            lightAmps[6] = Double.valueOf(TempStorage.mapStorage.get("light5").toString());
        }
        if (TempStorage.mapStorage.get("light6") != null) {
            lightAmps[7] = Double.valueOf(TempStorage.mapStorage.get("light6").toString());
        }
        if (TempStorage.mapStorage.get("light7") != null) {
            lightAmps[8] = Double.valueOf(TempStorage.mapStorage.get("light7").toString());
        }

        for (int i = 1; i < 9; i++){
            if (lightAmps[i] != null) {
                lightStatus[i] = lightAmps[i] >= 1.0;
            }
        }
    }


    //expects boolean array with 9 variables ot gives exception
    public synchronized void ChangeState(boolean[] puls) {
        System.out.println("Change State");

        jo = new JSONObject();
        jo.put("ID", 2);
        ja = new JSONObject();
        ja.put("pulse0", puls[0]);  //on off state for fan and not lights
        ja.put("pulse1", puls[1]);  // all the other are light pulses
        ja.put("pulse2", puls[2]);
        ja.put("pulse3", puls[3]);
        ja.put("pulse4", puls[4]);
        ja.put("pulse5", puls[5]);
        ja.put("pulse6", puls[6]);
        ja.put("pulse7", puls[7]);
        ja.put("pulse8", puls[8]);
        jo.put("data", ja);
        String data = jo.toString();
        System.out.println(data);
        //String encrypted = Encryption.encrypt(data);
        String encrypted = data;
        clt.sendC(encrypted);
    }

    public static synchronized void action(){
        System.out.println("working light action :)");
    }

}

/*todo
fire notification
adjust mamps in takedata for the light status
ако съобщенията пращани от тук са бродкаст да кажа на микроконтролерите да четат само тези за тяхното си id
 */