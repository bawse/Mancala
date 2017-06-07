package kalah.game;

import com.qualitascorpus.testsupport.IO;

/**
 * Created by Jay on 5/29/2017.
 */
public class HumanPlayer extends Player {
    public HumanPlayer(int playerNumber, Pit[] playerBoard) {
        super(playerNumber, playerBoard);
    }

    @Override
    public int getMove(IO io) {
        return io.readInteger("Player P"+getPlayerNumber()+"'s turn - Specify house number or 'q' to quit: ",1,6,-1,"q");
    }


}
