package com.wxy.pierotateview.model;

public class PieRotateViewModel {
    private String name;
    private float num;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;

    public PieRotateViewModel(String name, float num, int color) {
        this.name = name;
        this.num = num;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }


}
