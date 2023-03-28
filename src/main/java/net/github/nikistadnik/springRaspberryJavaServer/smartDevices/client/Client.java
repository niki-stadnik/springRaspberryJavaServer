package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.client;

import net.github.nikistadnik.springRaspberryJavaServer.Encryption;
import net.github.nikistadnik.springRaspberryJavaServer.TempStorage;
import org.json.simple.JSONObject;

public class Client {
/*
    void handleCommand(String command) {
        switch (command) {
            case "giveData":
                JSONObject jo = new JSONObject();
                jo.put("ID", 999);
                jo.put("data", TempStorage.mapStorage);
                String data = jo.toString();
                String encrypted = Encryption.encrypt(data);
                out.println(encrypted);              //only to the client of this thread
                System.out.println("here is your data");
                System.out.println(TempStorage.mapStorage);
                break;
            case "bathroomFanON":
                TempStorage.bathroomFanMode = TempStorage.Mode.ON;
                TempStorage.mapStorage.put("bathroomFanMode", "on");
                bathroomFan.switchON();
                break;
            case "bathroomFanAuto":
                TempStorage.bathroomFanMode = TempStorage.Mode.AUTO;
                TempStorage.mapStorage.put("bathroomFanMode", "auto");
                break;
            case "bathroomFanOFF":
                TempStorage.bathroomFanMode = TempStorage.Mode.OFF;
                TempStorage.mapStorage.put("bathroomFanMode", "off");
                bathroomFan.switchOFF();
                break;
            case "dataDB":
                System.out.println("data from db");
                //PostgreSQL.getAllData();
                //handle data and send back to android
        }
    }
*/
}
