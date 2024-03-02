import java.util.ArrayList;

public class WhiteBallStrategy implements BallStrategy{

    @Override
    public boolean pocketStrategy(Ball ball, PoolGameManager game) {
        // we want the game to reset from the beginning, which means that this strategy will trigger a reset event
        game.resetGame();

        // we don't want to remove the white ball from the table, so we return false
        return false;
    }
}
