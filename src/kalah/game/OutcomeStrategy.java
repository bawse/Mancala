package kalah.game;

/**
 * Created by Jay on 5/8/2017.
 */
public interface OutcomeStrategy {

    public boolean outcome1(int finalIndex, int playerTurn, Pit[] gameBoard);
    public boolean outcome2(int finalIndex, int playerTurn, Pit[] gameBoard);
    public boolean outcome3(int finalIndex, int playerTurn, Pit[] gameBoard, Player player1, Player player2);


}
