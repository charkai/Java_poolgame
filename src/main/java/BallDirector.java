public class BallDirector {

    // Acts as the director in the Builder design pattern
    private BallBuilder b;

    public BallDirector(BallBuilder b) {
        this.b = b;
    }

    public void construct() {
        b.addColour();
        b.addPositionX();
        b.addPositionY();
        b.setInitialPositions();
        b.addVelocityX();
        b.addVelocityY();
        b.addMass();
        b.addStrategy();
    }
}
