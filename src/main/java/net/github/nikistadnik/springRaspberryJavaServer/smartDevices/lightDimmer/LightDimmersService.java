package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightDimmer;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LightDimmersService extends SmartDevice<LightDimmerModel, LightDimmerModel> {
    @Override
    protected String provideName() {
        return "lightDimmer";
    }

    @Override
    protected String pairedWithName() {
        return "";
    }

    @Override
    protected Class provideDeviceModel() {
        return LightDimmerModel.class;
    }

    @Override
    protected Class provideClientModel() {
        return null;
    }

    @Override
    protected void handleDeviceData(LightDimmerModel data) {
        messaging.convertAndSend("/topic/dimmersClient", data);
        System.out.println(data);
        //todo
    }

    @Override
    protected void handleClientData(LightDimmerModel data) {
        JSONObject jsonObject = new JSONObject(data);
        System.out.println(jsonObject.toString());
        //todo request data from the client on init (handle message with name=getData and value=200)
        if(data.getName().equals("getData")){
            //System.out.println("init client");
            messaging.convertAndSend("/topic/dimmersClient", new LightDimmerModel("dim1", 33));
        }

    }
}
