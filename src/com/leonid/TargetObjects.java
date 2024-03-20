package com.leonid;

import acm.graphics.GImage;
import acm.graphics.GRoundRect;

import java.awt.*;
import java.util.ArrayList;

public class TargetObjects {

    /* OBJECT is square with the side OBJECT_SIZE */
    private static double OBJECT_SIZE;
    private static double SEPARATION_BETWEEN_BOXES;
    private static int COUNT_TARGET_OBJECT_OF_ROW;
    private static final ArrayList<GRoundRect> objects = new ArrayList<>();


    public static ArrayList<GRoundRect> createObjects(double size, double separation, int count){
        OBJECT_SIZE = size;
        SEPARATION_BETWEEN_BOXES = separation;
        COUNT_TARGET_OBJECT_OF_ROW = count;
        create();
        return objects;
    }

    public static void create(){
        boolean[][] res = recognizeFileObject();
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                if (res[i][j]){
                    double x = (j+1) * SEPARATION_BETWEEN_BOXES + j * OBJECT_SIZE;
                    double y = (i+1) * SEPARATION_BETWEEN_BOXES + i * OBJECT_SIZE;
                    GRoundRect rect = new GRoundRect(x, y, OBJECT_SIZE, OBJECT_SIZE);
                    rect.setFilled(true);
                    rect.setFillColor(Color.GRAY);
                    objects.add(rect);
                }
            }
        }
    }

    /**
     * Reads file and return boolean array, represented white-black photo
     */
    public static boolean[][] recognizeFileObject() {

        GImage image = new GImage("img_3.png");
        int[][] pixels = image.getPixelArray();
        int width = pixels.length;
        int height = pixels[0].length;

        int ime = width / COUNT_TARGET_OBJECT_OF_ROW;
        int jme = height / COUNT_TARGET_OBJECT_OF_ROW;


        boolean[][] binaryPhoto = new boolean[COUNT_TARGET_OBJECT_OF_ROW][COUNT_TARGET_OBJECT_OF_ROW];

        for (int i = 0; i < binaryPhoto.length; i++) {
            for (int j = 0; j < binaryPhoto[0].length; j++) {
                int pixel = pixels[i*ime][j*jme];
                int avg = (GImage.getRed(pixel) + GImage.getGreen(pixel) + GImage.getBlue(pixel)) / 3;
                if (avg>10) binaryPhoto[i][j] = true;
            }
        }

        return binaryPhoto;
    }
}

