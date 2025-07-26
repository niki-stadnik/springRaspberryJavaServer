package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock;


import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DoorlockService {

    private static int position = 0;
    private static int move = 0;


    static JSONObject jo;


    public void command(DoorlockClientModel data) {
        System.out.println(data);
        int move = data.getMove();
        JSONObject jo = new JSONObject();
        jo.put("move", move);
        String mess = jo.toString();
        SendMessage.sendMessage("/topic/doorlock", mess);
    }

    public void setData(DoorlockModel data) {
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        position = data.getPosition();
    }

    @Scheduled(fixedRate = 1000)
    private synchronized void lockUpdate() {
        jo = new JSONObject();
        jo.put("posit", position);
        String data = jo.toString();
        //System.out.println(data);
        SendMessage.sendMessage("/topic/clientDoorlock", data);
    }
}
