import java.util.ArrayList;

public class PoolTableCreator implements GameEntityCreator{

    // This class acts as a concrete creator in the FACTORY METHOD PATTERN

    private String colour;
    private long tableX;
    private long tableY;
    private double friction;

    public PoolTableCreator(String colour, long tableX, long tableY, double friction) {
        // The creator is initialised with all of the information it needs to construct a table
        this.colour = colour;
        this.tableX = tableX;
        this.tableY = tableY;
        this.friction = friction;
    }

    @Override
    public GameEntity create() {

        // Creates a pool table and sets all of the values
        PoolTable p = new PoolTable();
        p.setColour(this.colour);
        p.setTableX(this.tableX);
        p.setTableY(this.tableY);
        p.setFriction(this.friction);

        // Sets the pockets on the table based on the dimensions of the table
        ArrayList<Pocket> pockets = new ArrayList<>();
        pockets.add(new Pocket(0, 0));
        pockets.add(new Pocket(this.tableX/2-15, 0));
        pockets.add(new Pocket(this.tableX-30, 0));
        pockets.add(new Pocket(0, this.tableY-30));
        pockets.add(new Pocket(this.tableX/2-15, this.tableY-30));
        pockets.add(new Pocket(this.tableX-30, this.tableY-30));
        p.setPockets(pockets);


        return p;
    }


}
