package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenESP2;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SmartDevice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KitchenESP2Service extends SmartDevice<KitchenESP2Model, KitchenESP2Model> {


    @Override
    protected String provideName() {
        return "kitchen2";
    }

    @Override
    protected String pairedWithName() {
        return "kitchenStrip";
    }

    @Override
    protected Class provideDeviceModel() {
        return KitchenESP2Model.class;
    }

    @Override
    protected Class provideClientModel() {
        return KitchenESP2Model.class;
    }

    @Override
    protected void handleDeviceData(KitchenESP2Model data) {
        log.info(data.toString());

    }

    @Override
    protected void handleClientData(KitchenESP2Model data) {

    }
}
