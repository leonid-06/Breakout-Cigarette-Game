package com.leonid;

import acm.graphics.*;
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

    private static final int SEPARATION_BETWEEN_OBJECTS = 3;

    /* Count of possible objects in side (those needs to be broken) */
    private static final int COUNT_TARGET_OBJECT_OF_SIDE = 10;

    /* Offset of the cigarette up from the bottom */
    private static final int CIGARETTE_Y_OFFSET = 30;

    private static final int BALL_RADIUS = 8;

    private int TARGET_OBJECT_SIZE;
    private int currentCountOfObjects;

    /* Game objects (child of GObjects) */
    private Cigarette cigarette;
    private ArrayList<GRect> targetObjects;
    GOval ball;
    GLabel tipLabel;

    /* Horizontal and vertical velocity components, respectively */
    double dx;
    double dy;

    // allow the user to not interact with the racket without loosing
    private boolean autoPlay = false;

    /* from 0 to 10 */
    private int speed = 4;
    private int currentAttempts = 3;

    @Override
    public void init() {
        setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        this.setBackground(Color.BLACK);
        CIGARETTE_HEIGHT = CIGARETTE_WIDTH / 10;
        TARGET_OBJECT_SIZE = (getWidth() - SEPARATION_BETWEEN_OBJECTS * (COUNT_TARGET_OBJECT_OF_SIDE + 1))
                / COUNT_TARGET_OBJECT_OF_SIDE;
    }

    @Override
    public void run() {

        /* draw target objects, cigarette and ball */
        preparingGameObject();

        checkGameInputData();

        /* for cigarette control */
        addMouseListeners();

        playGame();
    }

    private void checkGameInputData() {
        boolean isCorrectSpeed = speed >= 0 && speed <= 10;
        boolean isCorrectBallSize = (TARGET_OBJECT_SIZE / (2.0 * BALL_RADIUS)) > 0.5;
        if (!isCorrectSpeed || !isCorrectBallSize) exit();
    }

    /**
     * Allows you to run
     */
    private void playGame() {

        RandomGenerator generator = RandomGenerator.getInstance();

        while (!isGameOver()) {
            dx = generator.nextDouble(-2, 3);
            dy = generator.nextDouble(1, 2);

            ball.setLocation(getWidth() / 2.0 - BALL_RADIUS, cigarette.getY() - 100);

            // display tip label
            tipLabel = GameLabel.createTipLabel("Click to start", cigarette, getWidth());
            add(tipLabel);
            waitForClick();
            remove(tipLabel);

            playRound();
            currentAttempts--;
        }

        if (currentCountOfObjects == 0) {
            tipLabel = GameLabel.createTipLabel("You are winner, but you don't have lungs anymore", cigarette, getWidth());
        } else {
            tipLabel = GameLabel.createTipLabel("You are looser, but you you still have lungs", cigarette, getWidth());
        }
        add(tipLabel);
        waitForClick();
        exit();

    }

    private boolean isGameOver() {
        return currentAttempts == 0 || currentCountOfObjects == 0;
    }

    private void playRound() {
        while (!isRoundOver()) {
            checkObjectHit();
            if (isTouchingRacket() || isTouchTopWall()) dy = -dy;
            if (isTouchRightWall() || isTouchLeftWall()) dx = -dx;
            if (autoPlay) followToTheRocket();
            ball.move(dx, dy);
            pause(12 - speed);
        }
    }

    private void checkObjectHit() {

        GPoint[] ballPoints = getGPoints();

        for (GPoint ballPoint : ballPoints) {
            // potential object (brick)
            GObject object = getElementAt(ballPoint.getX(), ballPoint.getY());

            // if ballPoint inside object (brick)
            if (object != cigarette && object != null) {

                // coordinates of object (brick)
                double xOfCenter = object.getX() + object.getWidth() / 2;
                double yOfCenter = object.getY() + object.getHeight() / 2;
                double xOfEnd = object.getX() + object.getWidth();
                double yOfEnd = object.getY() + object.getHeight();

                // coordinates of ball
                double xPoint = ballPoint.getX();
                double yPoint = ballPoint.getY();


                // top(1) or left(4)
                if (xPoint > (object.getX() - 1) && xPoint <= (xOfCenter + 1) && yPoint >= (object.getY() - 1) && yPoint <= (yOfCenter + 1)) {
                    if (abovePrimary(ballPoint, object)) dy = -dy;
                    else dx = -dx;
                }

                // // top(1) or right(2)
                if (xPoint < (xOfEnd + 1) && xPoint > (xOfCenter - 1) && yPoint > (object.getY() - 1) && yPoint < (yOfCenter + 1)) {
                    if (aboveSecondary(ballPoint, object)) dy = -dy;
                    else dx = -dx;
                }

                // right(2) or down(3)
                if (xPoint <= (xOfEnd + 1) && xPoint >= (xOfCenter - 1) && yPoint <= (yOfEnd + 1) && yPoint >= (yOfCenter - 1)) {
                    if (abovePrimary(ballPoint, object)) dx = -dx;
                    else dy = -dy;
                }

                // left(4) or down(3)
                if (xPoint >= (object.getX() - 1) && xPoint <= (xOfCenter + 1) && yPoint <= (yOfEnd + 1) && yPoint >= (yOfCenter - 1)) {
                    if (aboveSecondary(ballPoint, object)) dx = -dx;
                    else dy = -dy;
                }

                remove(object);
                currentCountOfObjects--;
                break;
            }
        }
    }

    /**
     * Returns an array of GPoints representing the corners of the ball's bounding box.
     */
    private GPoint[] getGPoints() {
        GPoint leftTop = new GPoint(ball.getX(), ball.getY());
        GPoint rightTop = new GPoint(ball.getX() + 2 * BALL_RADIUS, ball.getY());
        GPoint rightDown = new GPoint(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
        GPoint leftDown = new GPoint(ball.getX(), ball.getY() + 2 * BALL_RADIUS);

        return new GPoint[]{leftTop, rightTop, rightDown, leftDown};
    }

    /**
     * Return true if GPoint is under primary (from left-top to right-down) diagonal in square
     */
    private boolean abovePrimary(GPoint point, GObject object) {
        return (point.getX() - object.getX()) > (point.getY() - object.getY());
    }

    private boolean aboveSecondary(GPoint point, GObject object) {
        return (point.getX() - object.getX() + point.getY() - object.getY()) <= TARGET_OBJECT_SIZE;
    }

    private boolean isRoundOver() {
        boolean isBallOut = ball.getY() > getHeight();
        boolean isNoObjects = currentCountOfObjects == 0;
        return isNoObjects || isBallOut;
    }

    /**
     * Create game objects and add them to canvas
     */
    private void preparingGameObject() {
        cigarette = new Cigarette(CIGARETTE_WIDTH, CIGARETTE_HEIGHT);
        add(cigarette, (getWidth() - CIGARETTE_WIDTH) / 2, getHeight() - CIGARETTE_Y_OFFSET);

        targetObjects = TargetObjects.createObjects(TARGET_OBJECT_SIZE, SEPARATION_BETWEEN_OBJECTS, COUNT_TARGET_OBJECT_OF_SIDE);
        for (GRect box : targetObjects) add(box);
        currentCountOfObjects = targetObjects.size();

        ball = Ball.makeBall(BALL_RADIUS * 2);
        ball.sendToFront();
        add(ball);

    }

    private boolean isTouchingRacket() {
        double bottomOfTheBallY = ball.getY() + 2 * BALL_RADIUS;
        double topOfTheCigaretteY = cigarette.getY();
        double touchPointX = ball.getX() + BALL_RADIUS;
        boolean ok_on_y = bottomOfTheBallY >= topOfTheCigaretteY && (bottomOfTheBallY - topOfTheCigaretteY) < 4;
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

    private void followToTheRocket() {
        if ((ball.getX() + BALL_RADIUS) < (getWidth() - CIGARETTE_WIDTH / 2.0) && (ball.getX() + BALL_RADIUS) > (CIGARETTE_WIDTH / 2.0)) {
            cigarette.setLocation(ball.getX() + BALL_RADIUS - CIGARETTE_WIDTH / 2.0, cigarette.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouse) {
        if (mouse.getX() > (CIGARETTE_WIDTH / 2.0) && mouse.getX() < (getWidth() - CIGARETTE_WIDTH / 2.0) && !autoPlay) {
            cigarette.setLocation(mouse.getX() - CIGARETTE_WIDTH / 2.0, cigarette.getY());
        }
    }
}