package kalah.game;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Jay on 5/29/2017.
 */
public class RobotPlayer extends Player {
    public RobotPlayer(int playerNumber, Pit[] playerBoard) {
        super(playerNumber, playerBoard);
    }

    public int getMove(Board board){
        return ThreadLocalRandom.current().nextInt(1, 7);
    }

}
