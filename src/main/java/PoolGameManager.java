import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

public class PoolGameManager {

    private PoolTable table;
    private ArrayList<Ball> balls;
    private ArrayList<Ball> allBalls;

    private Ball whiteBall;

    // The manager keeps track of the initial setup of the balls, in case the game has to be reset to the beginning
    private ArrayList<Ball> initialSetup;

    private Scene scene;

    private CueStick cue;
    private final GraphicsContext gc;

    private Canvas canvas;

    public PoolGameManager(long width, long height) {

        // Initialising the canvas and graphics
        Group root = new Group();
        this.scene = new Scene(root);

        this.cue = new CueStick();

        this.canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
    }

    /**
     * Runs the functionality of the game. Acts as a continuous loop that executes to play the game
     */
    void run() {
        // Sets up a continuous loop to run for the game
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        // Checks for whether the mouse is pressed (possibly indiciating the start of a shot at the cue ball)
        canvas.setOnMousePressed((MouseEvent event) -> {

                // If the white ball was pressed, and the whiteBall is no longer moving, we can press on it
                if (ballPressed(event.getX(), event.getY()) && whiteBall.getVelocityX() == 0 && whiteBall.getVelocityY() == 0) {

                    // If it was pressed, we want to attach a cue to the whiteBall
                    whiteBall.attachOrDetachCue(true);
                }

                    canvas.setOnMouseDragged((MouseEvent drag) -> {

                        // Due to the nature of the 'setOn_' functions, we need to continuously check that the ball was pressed to execute the function
                        if (ballPressed(event.getX(), event.getY()) && whiteBall.getVelocityX() == 0 && whiteBall.getVelocityY() == 0) {

                            // Start drawing the cue as a line between two points- the mouse and the point on the ball where it was intiially pressed
                            this.cue.setxStartPos(event.getX());
                            this.cue.setyStartPos(event.getY());
                            this.cue.setxEndPos(drag.getX());
                            this.cue.setyEndPos(drag.getY());
                        }

                        canvas.setOnMouseReleased((MouseEvent event2) -> {
                            // Checking that the white ball was indeed (validly) pressed
                            if (ballPressed(event.getX(), event.getY()) && whiteBall.getVelocityX() == 0 && whiteBall.getVelocityY() == 0) {

                                // Enact the velocity change on the whiteball, and remove the cue
                                whiteBall.setVelocityX(event.getX() - event2.getX());
                                whiteBall.setVelocityY(event.getY() - event2.getY());
                                whiteBall.attachOrDetachCue(false);
                            }
                        });
                });

        });
    }

    public void draw() {
        this.allBalls = (ArrayList<Ball>) balls.clone();
        this.allBalls.add(whiteBall);
        // We want to consider all balls in the tick and draw functions, so we make sure that the white ball is also included in the tick and draw functions

        tick();
        drawTable();
        drawBalls();
    }
    void tick() {


        // If there hasn't yet been a win, execute the game loop
        if (!checkWin()) {
            ArrayList<Ball> ballsToRemove = new ArrayList<>();
            for (Ball ball : allBalls) {
                ball.tick();

                // Bounce balls off walls of pool table
                // This code was directly inspired by the code from the Week 6 exercise

                // If the ball hits the right side of the table, bounce back
                if (ball.getPositionX() + ball.getDiameter() > table.getTableX()) {
                    ball.setPositionX(table.getTableX() - ball.getDiameter());
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }
                // If the ball has hit the left side of the table, bounce back
                if (ball.getPositionX() < 0) {
                    ball.setPositionX(0 + ball.getRadius());
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }

                // If the ball has hit the bottom of the table, bounce back
                if (ball.getPositionY() + ball.getDiameter() > table.getTableY()) {
                    ball.setPositionY(table.getTableY() - ball.getDiameter());
                    ball.setVelocityY(ball.getVelocityY() * -1);
                }
                // if the ball hits the top of the table, bounce back
                if (ball.getPositionY() < 0) {
                    ball.setPositionY(0 + ball.getRadius());
                    ball.setVelocityY(ball.getVelocityY() * -1);
                }
                for (Ball ballB : balls) {
                    // Checks to see whether a collision has occured
                    if (detectCollision(ball, ballB)) {

                        Point2D positionA = new Point2D(ball.getPositionX() + ball.getRadius(), ball.getPositionY() + ball.getRadius());
                        Point2D velocityA = new Point2D(ball.getVelocityX(), ball.getVelocityY());
                        double massA = ball.getMass();
                        Point2D positionB = new Point2D(ballB.getPositionX() + ballB.getRadius(), ballB.getPositionY() + ballB.getRadius());
                        Point2D velocityB = new Point2D(ballB.getVelocityX(), ballB.getVelocityY());
                        double massB = ballB.getMass();

                        // Calculate the resulting velocities of the collision and enact the result on the balls
                        Pair<Point2D, Point2D> collisionVals = calculateCollision(positionA, velocityA, massA, positionB, velocityB, massB);
                        enactCollision(collisionVals, ball, ballB);
                    }
                }

                // ONLY if the ball is moving, decelerate it
                if (ball.getVelocityX() != 0 || ball.getVelocityY() != 0) {
                    ball.decelerate(table.getFriction());
                }
                //

                boolean removeBall = false; // We want to know whether a given ball should be taken off the game space (i.e. it has fallen in a pocket)
                if (checkInPocket(ball)) {

                    // STRATEGY PATTERN EXECUTED HERE
                    removeBall = ball.pocketStrategy(this);

                    // if the ball should be removed from the playing space (depends on the strategy), ensure it will be removed from the list
                    if (removeBall) {
                        ballsToRemove.add(ball);
                    }
                }
            }

            // remove all of the balls that are no longer in the playing space from the game
            balls.removeAll(ballsToRemove);
        }
        else {
            // If the game has been won, print message and quit
            System.out.println("Win and bye!");
            System.exit(0);

        }
    }

    /**
     * Collision Function
     * @param positionA The coordinates of the centre of ball A
     * @param velocityA The delta x,y vector of ball A (how much it moves per tick)
     * @param massA The mass of ball A (for the moment this should always be the same as ball B)
     * @param positionB The coordinates of the centre of ball B
     * @param velocityB The delta x,y vector of ball B (how much it moves per tick)
     * @param massB The mass of ball B (for the moment this should always be the same as ball A)
     *
     * @return A Pair<Point2D, Point2D> in which the first (key) Point2D is the new delta x,y vector for ball A, and the second (value) Point2D is the new delta x,y vector for ball B.
     */
    public static Pair<Point2D, Point2D> calculateCollision(Point2D positionA, Point2D velocityA, double massA, Point2D positionB, Point2D velocityB, double massB) {
        // Find the angle of the collision - basically where is ball B relative to ball A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();

        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);

        // If you don't detect the collision at just the right time, balls might collide again before they leave
        // each others' collision detection area, and bounce twice.
        // This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the original velocities
        if (vB <= 0 && vA >= 0) {
            return new Pair<>(velocityA, velocityB);
        }

        // This is the optimisation function described in the gamasutra link. Rather than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos)
        // this is a much simpler - and faster - way of obtaining the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);

        // Now we apply that calculated function to the pair of balls to obtain their final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));

        return new Pair<>(velAPrime, velBPrime);

    }

    /**
     * Changes the velocity of two balls after they have been involved in a collision
     * @param pair A set of 2 Point2D points that contain the x and y velocity changes for the two balls
     * @param ballA One of the balls involved in the collision
     * @param ballB The other ball involved in the collision
     */
    public void enactCollision(Pair<Point2D, Point2D> pair, Ball ballA, Ball ballB) {

        Point2D velocityBallA = pair.getKey();

        // In order to prevent the velocities becoming exponentially long floats, we need to round at least 1 of the resultant velocities to 1 decimal place
        // This ensures that the balls are not continuously moving and decreasing without ever reaching 0
        double velocityXBallA = (double) (Math.round(velocityBallA.getX())*10)/10;
        double velocityYBallA = (double) (Math.round(velocityBallA.getY())*10)/10;

        Point2D velocityBallB = pair.getValue();

        double velocityXBallB = (double) Math.round(velocityBallB.getX());
        double velocityYBallB = (double) Math.round(velocityBallB.getY());

        // Enact the velocity changes on the balls
        ballA.setVelocityX(velocityXBallA);
        ballA.setVelocityY(velocityYBallA);
        ballB.setVelocityX(velocityXBallB);
        ballB.setVelocityY(velocityYBallB);

    }

    /**
     * Detects whether a collision has occurred between two balls
     * @param ballA The current ball of interest
     * @param ballB The ball we are checking to see if BallA may have collided with
     * @return a boolean representing whether a collision did or did not occur
     */
    public boolean detectCollision(Ball ballA, Ball ballB) {

        // THIS FUNCTION was directly inspired by the StrategyBalls code from Week 6 Exercise
        if (ballA == ballB) {
            return false;
        }

        return Math.abs(ballA.getPositionX() - ballB.getPositionX()) < ballA.getRadius() + ballB.getRadius() &&
                Math.abs(ballA.getPositionY() - ballB.getPositionY()) < ballA.getRadius() + ballB.getRadius();

    }

    /**
     * Checks if a ball has fallen within the bounds of the pockets on the table
     * @param ball The Ball we want to check
     * @return Boolean representing whether the ball is in the pocket (true) or not (false)
     */
    public boolean checkInPocket(Ball ball) {

        for (Pocket pocket: table.getPockets()) {
            // if the middle of the ball breaches the edges of the pocket,it is considered 'in' the pocket
            if (ball.getPositionX()+ball.getRadius() > pocket.getXCoordinate() && ball.getPositionX()+ball.getRadius() < pocket.getXCoordinate() + pocket.getWidth() && ball.getPositionY()+ball.getRadius() > pocket.getYCoordinate() && ball.getPositionY()+ball.getRadius() < pocket.getYCoordinate() + pocket.getHeight()) {
                return true;
            }

        }
        return false;


    }

    /**
     * Draw the table on the screen
     */
    public void drawTable() {
        Color c = Color.web(table.getColour());
        gc.setFill(c);
        long width = table.getTableX();
        long height = table.getTableY();
        gc.fillRect(0, 0, width, height);

        // draw all of the pockets on the screen
        for (Pocket pocket: table.getPockets()){
            Color c2 = Color.web(pocket.getColour());
            gc.setFill(c2);
            gc.fillRect(pocket.getXCoordinate(), pocket.getYCoordinate(), pocket.getWidth(), pocket.getHeight());
        }

    }

    /**
     * Draws all of the ball elements on the screen
     */
    public void drawBalls(){

        // Draws all the coloured balls
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            Color c = Color.web(ball.getColour());
            gc.setFill(c);
            gc.fillOval(ball.getPositionX(), ball.getPositionY(), ball.getDiameter(), ball.getDiameter());
        }

        // Draws the cue ball
        Color c = Color.web(whiteBall.getColour());
        gc.setFill(c);
        gc.fillOval(whiteBall.getPositionX(), whiteBall.getPositionY(), whiteBall.getDiameter(), whiteBall.getDiameter());

        // If the whiteball currently is part of the hit sequence, draw the cue aiming at it
        if (whiteBall.isCueAttached()) {
            loadVisualCue(cue.getxStartPos(), cue.getyStartPos(), cue.getxEndPos(), cue.getyEndPos());
        }


    }

    /**
     * Checks whether a particular x, y coordinate falls within the bounds of the white ball. Used to confirm whether a user has
     * clicked on the whiteball to try and hit it
     * @param x The x-coordinate (of the mouse click)
     * @param y The y-coordinate (of the mouse click)
     * @return A boolean representing whether the coordinates are within the ball i.e. if the ball was pressed (true) or not (false)
     */
    public boolean ballPressed(double x, double y) {


        if (x > whiteBall.getPositionX() && x < whiteBall.getPositionX() + whiteBall.getDiameter() && y > whiteBall.getPositionY() && y < whiteBall.getPositionY() + whiteBall.getDiameter()) {
            return true;
        }
        return false;
    }

    /**
     * Loads the cue on the screen between two points
     * @param xStart The starting x-point of the cue
     * @param yStart The starting y-point of the cue
     * @param xEnd The ending x-point of the cue
     * @param yEnd The ending y-point of the cue
     */
    public void loadVisualCue(double xStart, double yStart, double xEnd, double yEnd) {
        gc.setFill(Color.BROWN);
        gc.strokeLine(xStart,yStart, xEnd, yEnd);
    }

    public Scene getScene() {
        return scene;
    }

    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public void setTable(PoolTable table) {
        this.table = table;
    }

    public void setWhiteBall(Ball ball) {
        this.whiteBall = ball;
    }
    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public Ball getWhiteBall() {
        return whiteBall;
    }
    public void setInitialState(ArrayList<Ball> balls) {
        this.initialSetup = balls;

    }

    public ArrayList<Ball> getInitialState() {
        return initialSetup;
    }

    /**
     * Resets a game back into its initial state (all balls back on table and in initial positions)
     */
    public void resetGame() {
        this.balls = this.initialSetup;
        for (Ball ball: balls) {
            ball.reset();
        }
        this.whiteBall.reset();
    }

    /**
     * Checks if the game has been won (all the balls are in the pockets except for the white ball)
     * @return a boolean representing whether the game has been won (true) or not (false)
     */
    public boolean checkWin() {
        if (balls.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

}
