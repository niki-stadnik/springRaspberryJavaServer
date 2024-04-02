package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import lombok.Data;

@Data
public class kitchenLEDstripModel {

    private int duty;

    public kitchenLEDstripModel() {
    }

    public kitchenLEDstripModel(int duty) {
        this.duty = duty;
    }

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }
}
