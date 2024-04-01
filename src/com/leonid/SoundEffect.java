package com.leonid;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundEffect {

    private ExecutorService executor;
    private Clip collisionSound;


    public SoundEffect() {
        executor = Executors.newSingleThreadExecutor();

        try {
            File soundFile = new File("bounce.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            collisionSound = AudioSystem.getClip();
            collisionSound.open(audioInputStream);
        } catch (Exception e) {
            System.err.println("Error loading collision sound: " + e.getMessage());
        }
    }

    public void playCollisionSound() {
        executor.submit(() -> {
            collisionSound.setFramePosition(0);
            collisionSound.start();
//            try {
//                Thread.sleep(collisionSound.getMicrosecondLength() / 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            collisionSound.stop();
            Thread.currentThread().interrupt(); // помечаем поток как прерванный
        });
    }
}
