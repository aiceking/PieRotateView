package com.zhufaner.pierotateview.bean;

import java.util.List;

public class PieRotateBean {
    private List<String> list_names;
    private List<Float> list_numbers;
//    private List<Float> list_degrees;
    private List<Integer> list_colors;
    private boolean isShowTextonMove;

    public boolean isShowOutSide() {
        return isShowOutSide;
    }

    public void setIsShowOutSide(boolean isShowOutSide) {
        this.isShowOutSide = isShowOutSide;
    }

    private boolean isShowOutSide;
    private int TextColor;

    public boolean isShowTextonMove() {
        return isShowTextonMove;
    }

    public void setIsShowTextonMove(boolean isShowTextonMove) {
        this.isShowTextonMove = isShowTextonMove;
    }

    private float Max_number;
//    public List<Float> getList_degrees() {
//        return list_degrees;
//    }
//
//    public void setList_degrees(List<Float> list_degrees) {
//        this.list_degrees = list_degrees;
//    }
    public List<String> getList_names() {
        return list_names;
    }

    public void setList_names(List<String> list_names) {
        this.list_names = list_names;
    }

    public List<Float> getList_numbers() {
        return list_numbers;
    }

    public void setList_numbers(List<Float> list_numbers) {
        this.list_numbers = list_numbers;
    }

    public List<Integer> getList_colors() {
        return list_colors;
    }

    public void setList_colors(List<Integer> list_colors) {
        this.list_colors = list_colors;
    }

    public float getMax_number() {
        return Max_number;
    }

    public void setMax_number(float max_number) {
        Max_number = max_number;
    }
    public int getTextColor() {
        return TextColor;
    }

    public void setTextColor(int textColor) {
        TextColor = textColor;
    }
}
