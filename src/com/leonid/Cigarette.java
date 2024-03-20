package com.leonid;

import acm.graphics.GCompound;
import acm.graphics.GRect;

import java.awt.*;

public class Cigarette extends GCompound {
    private final double generalWidth;
    private final double generalHeight;
    private final double filterWidth;
    private final double tobaccoWidth;

    public Cigarette(double generalWidth, double generalHeight) {
        this.generalWidth = generalWidth;
        this.generalHeight = generalHeight;
        filterWidth = generalWidth * 0.25;
        tobaccoWidth = generalWidth * 0.75;
        create();
    }

    /**
     * Create Cigarette and add it to local canvas;
     */
    public void create(){
        GRect filter = new GRect(filterWidth, generalHeight);
        filter.setFilled(true);
        filter.setFillColor(new Color(221, 157, 41));

        GRect tobacco = new GRect(tobaccoWidth, generalHeight);
        tobacco.setFilled(true);
        tobacco.setFillColor(new Color(247, 247, 247));

        add(filter, 0, 0);
        add(tobacco, filterWidth, 0);
    }
}
