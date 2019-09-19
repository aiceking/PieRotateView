package com.zhufaner.pierotateview.bean;

import java.util.List;

public abstract class PieRotateModel {
    protected List<Float>  list_numbers;

    public List<Integer> getList_colors() {
        return list_colors;
    }

    public void setList_colors(List<Integer> list_colors) {
        this.list_colors = list_colors;
    }

    protected List<Integer>  list_colors;

    public List<Float> getList_numbers() {
        return list_numbers;
    }

    public void setList_numbers(List<Float> list_numbers) {
        this.list_numbers = list_numbers;
    }
}
