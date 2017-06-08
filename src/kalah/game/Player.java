package kalah.game;


import com.qualitascorpus.testsupport.IO;

/**
 * Created by Jay on 5/8/2017.
 */
public abstract class Player {
    private int playerNumber;
    private Pit[] playerBoard;

    public Player(int playerNumber, Pit[] playerBoard) {
        this.playerNumber = playerNumber;
        this.playerBoard = playerBoard;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }


    public Pit[] getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(Pit[] playerBoard) {
        this.playerBoard = playerBoard;
    }


    public abstract int getMove(IO io, Pit[] gameBoard);

}
