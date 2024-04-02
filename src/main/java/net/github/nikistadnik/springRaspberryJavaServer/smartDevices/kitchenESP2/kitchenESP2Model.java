package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.kitchenESP2;

import lombok.Data;

@Data
public class kitchenESP2Model {

    private boolean here;

    public kitchenESP2Model() {
    }

    public kitchenESP2Model(boolean here) {
        this.here = here;
    }

    public boolean isHere() {
        return here;
    }

    public void setHere(boolean here) {
        this.here = here;
    }
}
