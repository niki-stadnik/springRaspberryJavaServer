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
   // int test = CentralCommand.;

    JSONObject jo;
    JSONObject ja;

    public static boolean fuseBoxFan = false;
    public static Double fuseBoxTemp = null;
    public static Double fuseBoxHum = null;
    public static Double light0 = null;
    public static Double light1 = null;
    public static Double light2 = null;
    public static Double light3 = null;
    public static Double light4 = null;
    public static Double light5 = null;
    public static Double light6 = null;
    public static Double light7 = null;



    @Autowired
    LightSwitch(Clients clt) {
        this.clt = clt;
    }


    public synchronized void Auto() {
        /*todo
        да се спами максимално бързо
        да се проверява за подадена команда?? или отделно?
        да се сложи закъснение от последната команда но не и от първата
         */

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
            light0 = Double.valueOf(TempStorage.mapStorage.get("light0").toString());
        }
        if (TempStorage.mapStorage.get("light1") != null) {
            light1 = Double.valueOf(TempStorage.mapStorage.get("light1").toString());
        }
        if (TempStorage.mapStorage.get("light2") != null) {
            light2 = Double.valueOf(TempStorage.mapStorage.get("light2").toString());
        }
        if (TempStorage.mapStorage.get("light3") != null) {
            light3 = Double.valueOf(TempStorage.mapStorage.get("light3").toString());
        }
        if (TempStorage.mapStorage.get("light4") != null) {
            light4 = Double.valueOf(TempStorage.mapStorage.get("light4").toString());
        }
        if (TempStorage.mapStorage.get("light5") != null) {
            light5 = Double.valueOf(TempStorage.mapStorage.get("light5").toString());
        }
        if (TempStorage.mapStorage.get("light6") != null) {
            light6 = Double.valueOf(TempStorage.mapStorage.get("light6").toString());
        }
        if (TempStorage.mapStorage.get("light7") != null) {
            light7 = Double.valueOf(TempStorage.mapStorage.get("light7").toString());
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
with the fan on at x temp - protection for fire should be added by stopping the fans at temp higher than 80? and notification sent

ако съобщенията пращани от тук са бродкаст да кажа на микроконтролерите да четат само тези за тяхното си id
 */