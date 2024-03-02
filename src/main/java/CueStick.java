public class CueStick extends GameEntity{

    // As the cue stick acts as a line between two points, it must have starting and ending y and x positions
    private double xStartPos;
    private double yStartPos;

    private double xEndPos;
    private double yEndPos;

    public String toString() {
        return "I am a cuestick";
    }

    public double getxEndPos() {
        return xEndPos;
    }

    public double getxStartPos() {
        return xStartPos;
    }

    public double getyEndPos() {
        return yEndPos;
    }

    public double getyStartPos() {
        return yStartPos;
    }

    public void setxEndPos(double xEndPos) {
        this.xEndPos = xEndPos;
    }

    public void setxStartPos(double xStartPos) {
        this.xStartPos = xStartPos;
    }

    public void setyEndPos(double yEndPos) {
        this.yEndPos = yEndPos;
    }

    public void setyStartPos(double yStartPos) {
        this.yStartPos = yStartPos;
    }
}
