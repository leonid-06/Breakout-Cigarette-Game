package com.leonid;

import acm.graphics.GOval;
import acm.graphics.GRoundRect;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Game extends WindowProgram {

    /* Width and height of application window in pixels */
    private static final int APPLICATION_WIDTH = 400;
    private static final int APPLICATION_HEIGHT = 600;

    /* Width and height of cigarette (racket that hits the ball) in pixels */
    private static final double CIGARETTE_WIDTH = 100;
    private static double CIGARETTE_HEIGHT;

    private static final int SEPARATION_BETWEEN_BOXES = 5;

    /* Count of possible objects (those needs to be broken) */
    private static final int COUNT_TARGET_OBJECT_OF_ROW = 30;
    private static final int COUNT_TARGET_OBJECT_OF_COL = 30;

    /* Offset of the cigarette up from the bottom */
    private static final int CIGARETTE_Y_OFFSET = 30;

    private static final int BALL_RADIUS = 10;

    private int TARGET_OBJECT_SIZE;
    private int currentCountOfObjects;

    /* Game objects (child of GObjects) */
    private Cigarette cigarette;
    private ArrayList<GRoundRect> targetObjects;
    GOval ball;

    /* Horizontal and vertical velocity components, respectively */
    double dx;
    double dy;

    @Override
    public void init() {
        setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        this.setBackground(Color.BLACK);
        CIGARETTE_HEIGHT = CIGARETTE_WIDTH / 10;
        TARGET_OBJECT_SIZE = (getWidth() - SEPARATION_BETWEEN_BOXES * (COUNT_TARGET_OBJECT_OF_ROW + 1))
                / COUNT_TARGET_OBJECT_OF_ROW;
    }

    @Override
    public void run() {

        /* draw target objects, cigarette and ball */
        preparingGameObject();

        /* for cigarette control */
        addMouseListeners();

        playGame();

    }


    /**
     * Allows you to run
     */
    private void playGame() {

        //waitForClick();

        RandomGenerator generator = RandomGenerator.getInstance();
        dx = generator.nextDouble(-2, 3);
        dy = generator.nextDouble(1, 2);

        while (currentCountOfObjects != 0) {

            if (isGameOver()) break;

            if (isTouchingPaddle() || isTouchTopWall()) dy = -dy;
            if (isTouchRightWall() || isTouchLeftWall()) dx = -dx;

            ball.move(dx, dy);

            pause(10);
        }
    }

    private boolean isGameOver() {
        return ball.getY()>getHeight();
    }

    /**
     * Create game objects and add them to canvas
     */
    private void preparingGameObject() {
        cigarette = new Cigarette(CIGARETTE_WIDTH, CIGARETTE_HEIGHT);
        add(cigarette, (getWidth() - CIGARETTE_WIDTH) / 2, getHeight() - CIGARETTE_Y_OFFSET);

        targetObjects = TargetObjects.createObjects(TARGET_OBJECT_SIZE, SEPARATION_BETWEEN_BOXES, COUNT_TARGET_OBJECT_OF_ROW);
        for (GRoundRect box : targetObjects) add(box);
        currentCountOfObjects = targetObjects.size();

        ball = Ball.makeBall(BALL_RADIUS * 2);
        ball.sendToFront();
        add(ball, (getWidth() - CIGARETTE_WIDTH) / 2, getHeight() - 400);
    }



    private boolean isTouchingPaddle() {
        double bottomOfTheBallY = ball.getY() + 2 * BALL_RADIUS;
        double topOfTheCigaretteY = cigarette.getY();
        double touchPointX = ball.getX() + BALL_RADIUS;
        boolean ok_on_y = bottomOfTheBallY >= topOfTheCigaretteY;
        boolean ok_on_x = (touchPointX > cigarette.getX() && touchPointX < (cigarette.getX() + CIGARETTE_WIDTH));
        return (ok_on_y && ok_on_x && dy > 0);
    }

    private boolean isTouchRightWall() {
        return ball.getX() >= (getWidth() - 2 * BALL_RADIUS);
    }
    private boolean isTouchLeftWall() {
        return ball.getX() <= 0;
    }

    private boolean isTouchTopWall() {
        return (ball.getY() <= 0) && (currentCountOfObjects > 0);
    }

    @Override
    public void mouseMoved(MouseEvent mouse) {
        if (mouse.getX() > (CIGARETTE_WIDTH / 2.0) && mouse.getX() < (getWidth() - CIGARETTE_WIDTH / 2.0)) {
            cigarette.setLocation(mouse.getX() - CIGARETTE_WIDTH / 2.0, cigarette.getY());
        }
    }
}