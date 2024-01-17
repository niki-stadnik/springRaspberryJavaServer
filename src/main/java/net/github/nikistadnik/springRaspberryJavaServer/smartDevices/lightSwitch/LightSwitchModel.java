package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch;

import lombok.Data;

@Data
public class LightSwitchModel {

    //private Double fuseBoxTemp;
    //private Double fuseBoxHum;
    //private boolean fuseBoxFan;
    private boolean light0;
    private boolean light1;
    private boolean light2;
    private boolean light3;
    private boolean light4;
    private boolean light5;
    private boolean light6;
    private boolean light7;


    public LightSwitchModel() {
    }

    public LightSwitchModel(/*Double fuseBoxTemp, Double fuseBoxHum, boolean fuseBoxFan,*/ boolean light0, boolean light1, boolean light2, boolean light3, boolean light4, boolean light5, boolean light6, boolean light7) {
        //this.fuseBoxTemp = fuseBoxTemp;
        //this.fuseBoxHum = fuseBoxHum;
        //this.fuseBoxFan = fuseBoxFan;
        this.light0 = light0;
        this.light1 = light1;
        this.light2 = light2;
        this.light3 = light3;
        this.light4 = light4;
        this.light5 = light5;
        this.light6 = light6;
        this.light7 = light7;
    }
/*
    public Double getFuseBoxTemp() {
        return fuseBoxTemp;
    }

    public void setFuseBoxTemp(Double fuseBoxTemp) {
        this.fuseBoxTemp = fuseBoxTemp;
    }

    public Double getFuseBoxHum() {
        return fuseBoxHum;
    }

    public void setFuseBoxHum(Double fuseBoxHum) {
        this.fuseBoxHum = fuseBoxHum;
    }

    public boolean isFuseBoxFan() {
        return fuseBoxFan;
    }

    public void setFuseBoxFan(boolean fuseBoxFan) {
        this.fuseBoxFan = fuseBoxFan;
    }
*/
    public boolean isLight0() {
        return light0;
    }

    public void setLight0(boolean light0) {
        this.light0 = light0;
    }

    public boolean isLight1() {
        return light1;
    }

    public void setLight1(boolean light1) {
        this.light1 = light1;
    }

    public boolean isLight2() {
        return light2;
    }

    public void setLight2(boolean light2) {
        this.light2 = light2;
    }

    public boolean isLight3() {
        return light3;
    }

    public void setLight3(boolean light3) {
        this.light3 = light3;
    }

    public boolean isLight4() {
        return light4;
    }

    public void setLight4(boolean light4) {
        this.light4 = light4;
    }

    public boolean isLight5() {
        return light5;
    }

    public void setLight5(boolean light5) {
        this.light5 = light5;
    }

    public boolean isLight6() {
        return light6;
    }

    public void setLight6(boolean light6) {
        this.light6 = light6;
    }

    public boolean isLight7() {
        return light7;
    }

    public void setLight7(boolean light7) {
        this.light7 = light7;
    }
}
