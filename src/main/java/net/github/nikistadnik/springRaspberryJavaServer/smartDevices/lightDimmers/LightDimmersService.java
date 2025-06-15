package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightDimmers;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchClientModel;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class LightDimmersService {

    JSONObject jo;

    public LightDimmersService() {
    }

    public void command(LightDimmerModel data){
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        //todo request data from the client on init (handle message with name=getData and value=200)
        if(data.getName().equals("getData")){
            //System.out.println("init client");
            jo = new JSONObject();
            jo.put("name", "dim1");
            jo.put("value", 33);
            String dataOut = jo.toString();
            SendMessage.sendMessage("/topic/dimmersClient", dataOut);
        }
    }
    public void setData(LightDimmerModel data){
        System.out.println(data);
        //todo
    }
}
