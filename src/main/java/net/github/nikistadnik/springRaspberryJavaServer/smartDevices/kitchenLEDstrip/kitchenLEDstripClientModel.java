package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenLEDstrip;

import lombok.Data;

@Data
public class kitchenLEDstripClientModel {

    private int command;

    private int duty;

    private int time;

    public kitchenLEDstripClientModel() {
    }

    public kitchenLEDstripClientModel(int command, int duty, int time) {
        this.command = command;
        this.duty = duty;
        this.time = time;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
