public class PoolTableBallBuilder implements BallBuilder{

    // This class acts as the CONCRETE BUILDER in the Builder pattern.
    // It implements all the methods in the abstract builder (BallBuilder) to create a pool table ball
    private Ball ball;
    private String colour;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double mass;


    public PoolTableBallBuilder(String colour, double positionX, double positionY, double velocityX, double velocityY, double mass) {
        // The concrete ballBuilder is initialised with all the parameters it would need to build the ball
        this.ball = new Ball();
        this.colour = colour;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.mass = mass;
    }


    @Override
    public void addColour() {
        this.ball.setColour(this.colour);
    }

    @Override
    public void addPositionX() {
        this.ball.setPositionX(this.positionX);
    }

    @Override
    public void addPositionY() {
        this.ball.setPositionY(this.positionY);
    }

    @Override
    public void addVelocityX() {
        this.ball.setVelocityX(this.velocityX);

    }

    @Override
    public void addVelocityY() {
        this.ball.setVelocityY(this.velocityY);
    }

    @Override
    public void addMass() {
        this.ball.setMass(this.mass);
    }

    @Override
    public void addStrategy() {

        // Adds the strategy to the ball based on the colour
        if (colour.toLowerCase().equals("blue")) {
            this.ball.setStrategy(new BlueBallStrategy());
        }
        else if (colour.toLowerCase().equals("red")) {
            this.ball.setStrategy(new RedBallStrategy());
        }
        else if (colour.toLowerCase().equals("white")) {
            this.ball.setStrategy(new WhiteBallStrategy());
        }
    }

    public Ball getResult() {
        return this.ball;
    }

    public void reset() {
        this.ball = new Ball();
    }

    public void setInitialPositions() {
        ball.setInitialPositionX(this.positionX);
        ball.setInitialPositionY(this.positionY);
    }

}
