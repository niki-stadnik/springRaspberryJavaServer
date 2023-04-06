package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.client;

import lombok.Data;

@Data
public class ClientModel {

    private String freeH;

    public ClientModel() {
    }

    public ClientModel(String freeH) {
        this.freeH = freeH;
    }

    public String getFreeH() {
        return freeH;
    }

    public void setFreeH(String freeH) {
        this.freeH = freeH;
    }
}
