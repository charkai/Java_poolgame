import java.util.ArrayList;

public class BlueBallStrategy implements BallStrategy{

    @Override
    public boolean pocketStrategy(Ball ball, PoolGameManager game) {
        if (ball.getHoleCounter() == 0) {
            // CHECK that you can reset ball - i.e. there is another ball occupying the intiial position of this ball
            for (Ball otherBall: game.getBalls()) {
                // if the new X and Y coordinates are within the bounds of another ball
                // (bounds = position (as position = top left of ball)+diameter),
                // then it is an occupied space, and the ball is removed from the game
                double newX = ball.getInitialPositionX();
                double newY = ball.getInitialPositionY();
                if (newX >= otherBall.getPositionX() && newX <= otherBall.getPositionX()+otherBall.getDiameter() && newY >= otherBall.getPositionY() && newY <= otherBall.getPositionY() + otherBall.getDiameter()) {
                    return true;
                }
            }

            // If the ball is not in an occupied space, reset it to its original position and increment the hole counter
            // The ball should not be removed from the game yet, so it returns a false value
            ball.reset();
            ball.setHoleCounter(1);
            return false;
        }
        else {
            // If the ball has already fallen in a pocket, it should return a true value as it should be removed from the game
            return true;
        }

    }
}
