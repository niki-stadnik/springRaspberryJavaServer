package net.github.nikistadnik.springRaspberryJavaServer.smartDevices;

import net.github.nikistadnik.springRaspberryJavaServer.CentralCommand;
import net.github.nikistadnik.springRaspberryJavaServer.Clients;
import net.github.nikistadnik.springRaspberryJavaServer.Encryption;
import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BathroomFan {

    private final Clients clt;

    int rInt = CentralCommand.getRepeatInterval();
    double BathroomFanDelay;
    double spamDelay = 2 / ((double) rInt / 1000);

    JSONObject jo;
    int counterForBathroomFan = 0;
    int noResponse = 0;
    int counterSpamDelay = 0;
    boolean flag = false;

    static int fanCom = 0;

    public static Double bathTemp = null;
    public static Double bathHum = null;
    public static Double bathLight = null;
    public static boolean bathFan = false;

    @Autowired
    BathroomFan(Clients clt) {
        this.clt = clt;
        TempStorage.mapStorage.put("bathroomFanMode", "auto");
        TempStorage.mapStorage.put("bathroomFanDelay", 30);
    }

    public synchronized void Auto(int repeatInterval) {
        BathroomFanDelay = Double.parseDouble(TempStorage.mapStorage.get("bathroomFanDelay").toString()) / ((double) rInt / 1000);

        if (TempStorage.mapStorage.get("bathTemp") != null) {
            bathTemp = Double.valueOf(TempStorage.mapStorage.get("bathTemp").toString());
        }
        if (TempStorage.mapStorage.get("bathHum") != null) {
            bathHum = Double.valueOf(TempStorage.mapStorage.get("bathHum").toString());
        }
        if (TempStorage.mapStorage.get("bathLight") != null) {
            bathLight = Double.valueOf(TempStorage.mapStorage.get("bathLight").toString());
        }
        if (TempStorage.mapStorage.get("bathFan") != null) {
            bathFan = (boolean) TempStorage.mapStorage.get("bathFan");
        }

        if (bathHum != null && bathLight != null && bathTemp != null) {
            if ((bathHum > 60 || bathLight >= 0.01) && !bathFan) {
                //fanCom = 1;
                if (counterSpamDelay == 0) {
                    switchON();
                    counterSpamDelay = (int)spamDelay;
                }
                counterForBathroomFan = 0;
            } else if (bathHum < 50 && bathLight < 0.01 && bathFan) {
                if (counterForBathroomFan >= BathroomFanDelay) {
                    //fanCom = 2;
                    if (counterSpamDelay == 0) {
                        switchOFF();
                        counterSpamDelay = (int)spamDelay;
                    }
                } else {
                    counterForBathroomFan++;
                    //System.out.println("off delay: " + counterForBathroomFan);
                }
            } else {
                //fanCom = 0;
                if (bathLight >= 0.01) counterForBathroomFan = 0;
            }
            if (counterSpamDelay > 0) {
                counterSpamDelay--;
                //System.out.println("spam: " + counterSpamDelay);
            }

        } else {
            //System.out.println("no data from bathroomFan");
        }
    }

    public synchronized void switchON() {
        System.out.println("switch it on");
        jo = new JSONObject();
        jo.put("ID", 1);
        jo.put("data", true);
        String data = jo.toString();
        System.out.println(data);
        //String encrypted = Encryption.encrypt(data);
        String encrypted = data;
        clt.sendC(encrypted);
    }

    public synchronized void switchOFF() {
        System.out.println("switch it off");
        jo = new JSONObject();
        jo.put("ID", 1);
        jo.put("data", false);
        String data = jo.toString();
        //String encrypted = Encryption.encrypt(data);
        String encrypted = data;
        clt.sendC(encrypted);
    }

    public static synchronized void action(){
        System.out.println("working fan action :)");
    }


}

/*todo
before shower = temp 23 & hum 44
while shower on = temp ~ 26 & hum fluctuates between 43 & 80
when shower is just turned off = temp 26 & 45 but a lot af hum can be felt
control by temp or 30min delay?
when temp sensors are added in rooms and outside - bathroom temp can be used easily
atm bathroom temp in the summer can trigger the fan to be on all the time
 */