import java.util.ArrayList;

public class PoolTable extends GameEntity{

    // This class represents the playing space for the game of pool

    // Each table is initialised with a width, height, a friction value and a colour
    private double friction;
    private long tableX;
    private long tableY;

    // Each table also has a list of pockets
    private ArrayList<Pocket> pockets;

    public void setTableX(long tableX) {
        this.tableX = tableX;
    }

    public void setTableY(long tableY) {
        this.tableY = tableY;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public void setPockets(ArrayList<Pocket> pockets) {
        this.pockets = pockets;
    }


    public long getTableX() {
        return tableX;
    }

    public long getTableY() {
        return tableY;
    }

    public double getFriction() {
        return friction;
    }

    public String toString() {
        return "I am a table with x: " + tableX + " , y: " + tableY + ", colour: " + colour + ", friction: " + friction;
    }

    public ArrayList<Pocket> getPockets() {
        return pockets;
    }


}
