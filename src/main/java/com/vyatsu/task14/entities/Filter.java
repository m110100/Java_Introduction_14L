package com.vyatsu.task14.entities;

import java.util.ArrayList;

public class Filter {
    private String pattern = "";
    private int max = 10000;
    private int min = 0;
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
