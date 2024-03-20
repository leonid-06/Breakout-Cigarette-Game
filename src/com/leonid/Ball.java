package com.leonid;

import acm.graphics.GOval;

import java.awt.*;

public class Ball {

    public static GOval makeBall(double diameter){
        GOval oval = new GOval(diameter, diameter);
        oval.setFilled(true);
        oval.setFillColor(new Color(255,90,0));
        return oval;
    }
}
