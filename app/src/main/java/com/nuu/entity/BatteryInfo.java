package com.nuu.entity;

public class BatteryInfo {
    private int pow = 0;
    private int charge = 0;  //1 isCharging; 0 not charge

    public int getPow() {
        return pow;
    }

    public int getCharge() {
        return charge;
    }

    public void setPow(int pow) {
        this.pow = pow;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }
}
