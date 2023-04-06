package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.client;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
public class ClientService {
    public ClientService() {
    }

    void setData(ClientModel data){
        System.out.println(data);
    }
    @Scheduled(fixedRate = 10000)
    public void testSpam() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("test", "pest");
        data.put("wtf", "49");
        data.put("tt", true);
        data.put("te", 25.02);
        //ClientModel mod = new ClientModel(HtmlUtils.htmlEscape("test beee"));
        SendMessage.sendMessage("/topic/client", String.valueOf(data));
    }



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
