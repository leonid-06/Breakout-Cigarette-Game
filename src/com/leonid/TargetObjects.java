package com.leonid;

import acm.graphics.GImage;
import acm.graphics.GRect;

import java.awt.*;
import java.util.ArrayList;

public class TargetObjects {

    /* OBJECT is square with the side OBJECT_SIZE */
    private static double OBJECT_SIZE;
    private static double SEPARATION_BETWEEN_OBJECTS;
    private static int COUNT_TARGET_OBJECT_OF_SIDE;


    /**
     * Creates an ArrayList of GRect representing target objects based on the provided parameters.
     *
     * @param size       The size of each target object.
     * @param separation The separation distance between target objects.
     * @param count      The number of target objects in each row.
     * @return An ArrayList of GRect representing target objects.
     */
    public static ArrayList<GRect> createObjects(double size, double separation, int count){
        OBJECT_SIZE = size;
        SEPARATION_BETWEEN_OBJECTS = separation;
        COUNT_TARGET_OBJECT_OF_SIDE = count;

        boolean[][] binaryPhoto = recognizeFileObject("img_3.png");

        return getObjects(binaryPhoto);
    }


    /**
     * Generates an ArrayList of GRect representing target objects based on the provided boolean array.
     *
     * @param binaryPhoto A boolean array representing a black and white photo.
     * @return An ArrayList of GRect representing target objects.
     */
    public static ArrayList<GRect> getObjects(boolean[][] binaryPhoto){
        ArrayList<GRect> objects = new ArrayList<>();
        for (int i = 0; i < binaryPhoto.length; i++) {
            for (int j = 0; j < binaryPhoto[0].length; j++) {
                if (binaryPhoto[i][j]){
                    double x = (j+1) * SEPARATION_BETWEEN_OBJECTS + j * OBJECT_SIZE;
                    double y = (i+1) * SEPARATION_BETWEEN_OBJECTS + i * OBJECT_SIZE;
                    GRect rect = new GRect(x, y, OBJECT_SIZE, OBJECT_SIZE);
                    rect.setFilled(true);
                    rect.setFillColor(Color.GRAY);
                    objects.add(rect);
                }
            }
        }
        return objects;
    }


    /**
     * Reads an image file and converts it into a boolean array representing a black and white photo.
     *
     * @param fileName The name of the image file to read.
     * @return A boolean array representing a black and white photo.
     */
    public static boolean[][] recognizeFileObject(String fileName) {

        GImage image = new GImage(fileName);
        int[][] pixels = image.getPixelArray();
        int width = pixels.length;
        int height = pixels[0].length;

        int ime = width / COUNT_TARGET_OBJECT_OF_SIDE;
        int jme = height / COUNT_TARGET_OBJECT_OF_SIDE;


        boolean[][] binaryPhoto = new boolean[COUNT_TARGET_OBJECT_OF_SIDE][COUNT_TARGET_OBJECT_OF_SIDE];

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

