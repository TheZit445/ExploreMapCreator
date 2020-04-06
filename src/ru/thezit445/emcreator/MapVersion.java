package ru.thezit445.emcreator;

public enum MapVersion {

    v1_15_R1(2227),
    v1_15_R0(2225),
    v1_14_R1(1957),
    v1_14_R0(1952),
    v1_13_R2(1631),
    v1_13_R1(1628);

    private final int data;

    MapVersion(int data) {
        this.data = data;
    }

    public int getData(){
        return data;
    }

}
