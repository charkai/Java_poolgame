import java.util.ArrayList;

public class Ball extends GameEntity{

    private double positionX;
    private double positionY;

    // The initial Position of the ball as read in by the config
    private double initialPositionX;
    private double initialPositionY;

    private double velocityX;
    private double velocityY;
    private double mass;

    // Counts how many times the ball has fallen in a pocket
    private int holeCounter;

    // References the strategy pattern to be used for the ball
    private BallStrategy strategy;

    // Keeps track of whether the ball is being tracked by the cue
    private boolean cueAttached;

    private final double diameter=25;

    public Ball() {

        this.holeCounter = 0;

    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setStrategy(BallStrategy strategy) {
        this.strategy = strategy;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getVelocityX() {
        return velocityX;
    }
    public double getVelocityY() {
        return velocityY;
    }

    public double getMass() {
        return mass;
    }


    public String toString() {
        return "I am a ball, with x: " + positionX + " , y: " + positionY + ", mass: " + mass + ", colour: " + colour + ", velocity x: " + velocityX + ", velocity y: " + velocityY;
    }

    /**
     * This function essentially enacts the motion for the ball- and will execute as part of a loop. For every 'tick', the position will change by the value of the velocity
     */

    public void tick() {
        positionX += velocityX;
        positionY += velocityY;
    }

    public double getDiameter() {
        return diameter;
    }

    /**
     * This function slows the ball down. This will be due to the friction of the table
     * @param constant The deceleration value for which the ball will slow down by
     */
    public void decelerate(double constant) {

        // As we want the ball to slow down towards 0, we want to check the initial direction
        // If the velocity is positive, we want to decrease towards 0, but the opposite is true for a negative velocity.
        if (velocityX > 0) {

            velocityX -= constant;
        }
        else if (velocityX < 0) {

            velocityX += constant;
        }

        if (velocityY > 0) {

            velocityY -= constant;
        }
        else if (velocityY < 0) {

            velocityY += constant;
        }

    }

    public double getRadius() {
        return this.diameter/2;
    }

    public int getHoleCounter(){
        return holeCounter;
    }

    public void setHoleCounter(int x){
        this.holeCounter+=x;
    }

    public boolean pocketStrategy(PoolGameManager game) {
        return strategy.pocketStrategy(this, game);
    }

    public void setInitialPositionX(double x){
        this.initialPositionX = x;
    }

    public double getInitialPositionX() {
        return initialPositionX;
    }

    public void setInitialPositionY(double initialPositionY) {
        this.initialPositionY = initialPositionY;
    }

    public double getInitialPositionY() {
        return initialPositionY;
    }

    /**
     * Resets the ball back to its starting position and in a neutral state
     */
    public void reset() {
        this.positionX = initialPositionX;
        this.positionY = initialPositionY;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public void attachOrDetachCue(boolean isAttach) {
        this.cueAttached = isAttach;
    }

    public boolean isCueAttached() {
        return cueAttached;
    }

}
