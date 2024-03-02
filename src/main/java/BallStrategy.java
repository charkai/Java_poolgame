

public interface BallStrategy {
    // This class defines the interface for the concrete strategies as part of the strategy pattern

    /**
     * Strategy for if a ball falls in the pocket
     * @param ball The ball which has fallen in the pocket
     * @param game The game within which the ball exists
     * @return A boolean representing whether the ball should (true) or should not (false) be removed from the playing space
     */
    public boolean pocketStrategy(Ball ball, PoolGameManager game);

}
