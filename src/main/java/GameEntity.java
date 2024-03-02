public abstract class GameEntity {

    // This abstract class is used for all of the game entities. It acts as the abstract product
    // in the factory method pattern
    protected String colour;
//    public abstract void setColour(String colour);
//    public abstract String getColour();
    public void setColour(String colour) {
        this.colour = colour;
    }
    public String getColour() {
        return colour;
    }

    public abstract String toString();
}
