package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman;

import lombok.Data;

@Data
public class DoormanClientModel {
    private int command;

    public DoormanClientModel(){
    }

    public DoormanClientModel(int command) {
        this.command = command;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }
}
