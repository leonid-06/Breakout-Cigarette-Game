package com.leonid;

import acm.graphics.GLabel;

import java.awt.*;

public class GameLabel {
    public static GLabel createTipLabel(String name, Cigarette cigarette, int width) {
        GLabel label = new GLabel(name);
        label.setFont("Monospace-15");
        label.setColor(Color.WHITE);
        double xForTipLabel = (width - label.getWidth()) / 2.0;
        double yForTipLabel = cigarette.getY() - 10;
        label.setLocation(xForTipLabel, yForTipLabel);
        return label;
    }
}
