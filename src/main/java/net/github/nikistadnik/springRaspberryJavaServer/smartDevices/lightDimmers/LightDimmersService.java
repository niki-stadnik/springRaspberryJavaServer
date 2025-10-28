package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightDimmers;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LightDimmersService {

    private final SimpMessageSendingOperations messaging;

    public void command(LightDimmerModel data){
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        //todo request data from the client on init (handle message with name=getData and value=200)
        if(data.getName().equals("getData")){
            //System.out.println("init client");
            messaging.convertAndSend("/topic/dimmersClient", new LightDimmerModel("dim1", 33));
        }
    }
    public void setData(LightDimmerModel data){
        System.out.println(data);
        //todo
    }
}
