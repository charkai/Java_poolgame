import java.util.ArrayList;

public class RedBallStrategy implements BallStrategy{

    @Override
    public boolean pocketStrategy(Ball ball, PoolGameManager game) {

        // Check that ball is actually attached to the table - if so, automatically remove it as it has fallen in the pocket
        if (game.getBalls().contains(ball)) {
            return true;
        }
        // If there is no attachment between the ball and table, then don't do anything
        return false;
    }



}
