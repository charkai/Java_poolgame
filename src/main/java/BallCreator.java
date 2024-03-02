public class BallCreator implements GameEntityCreator{

    // Acts as the Concrete Creator in the Factory Method pattern

    private String colour;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double mass;

    public BallCreator(String colour, double positionX, double positionY, double velocityX, double velocityY, double mass) {
        this.colour = colour;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.mass = mass;
    }

    @Override
    public GameEntity create() {

        // Utilises the builder design pattern to create the ball
        BallBuilder b = new PoolTableBallBuilder(this.colour, this.positionX, this.positionY, this.velocityX, this.velocityY, this.mass);
        BallDirector director = new BallDirector(b);
        director.construct();
        return b.getResult();
    }
}


