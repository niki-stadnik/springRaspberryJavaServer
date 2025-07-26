package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock;


import lombok.Data;

@Data
public class DoorlockClientModel {

    private int move;

    public DoorlockClientModel() {
    }

    public DoorlockClientModel(int move) {
        this.move = move;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }
}
