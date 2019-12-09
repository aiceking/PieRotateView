package com.wxy.pierotateview.model;


import android.graphics.Path;

public class PieRotateViewModel {
    private String name;
    private float num;
    private Path path;
    private int color;
    private float centerDregee;
    private float selfDregee;
    private String percent;

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public float getSelfDregee() {
        return selfDregee;
    }

    public void setSelfDregee(float selfDregee) {
        this.selfDregee = selfDregee;
    }

    public float getCenterDregee() {
        return centerDregee;
    }

    public void setCenterDregee(float centerDregee) {
        this.centerDregee = centerDregee;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


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
