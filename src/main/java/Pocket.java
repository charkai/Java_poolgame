public class Pocket extends GameEntity{

    // This class allows for the creation of pockets on a pool table
    // Pockets must have an x and y coordinate as well as a width and height
    private double xCoordinate;
    private double yCoordinate;
    private final double width =30;
    private final double height = 30;

    public Pocket(double x, double y) {
        this.xCoordinate= x;
        this.yCoordinate = y;
        this.colour = "black";
    }

    public void setXCoordinate(double x) {
        this.xCoordinate= x;
    }

    public void setYCoordinate(double y) {
        this.yCoordinate =y;
    }

    public double getXCoordinate() {
        return xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public String toString() {
        return "I am a pocket";
    }



}
