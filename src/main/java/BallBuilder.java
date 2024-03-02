public interface BallBuilder {

    // Acts as the builder in the Builder pattern
    public void addColour();
    public void addPositionX();
    public void addPositionY();

    public void setInitialPositions();
    public void addVelocityX();
    public void addVelocityY();
    public void addMass();

    public void addStrategy();
    public Ball getResult();
}
