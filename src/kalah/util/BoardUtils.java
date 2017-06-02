package kalah.util;

import kalah.game.Board;
import kalah.game.Pit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpan889 on 2/06/2017.
 */
public final class BoardUtils {
    private BoardUtils(){}

    public static boolean otherPlayerStore(int index, int gameBoardLength, int playerTurn){
        int player1StoreIndex = (gameBoardLength/2) - 1;
        int player2StoreIndex = gameBoardLength - 1;

        if (index == player1StoreIndex && playerTurn == 2){
            return true;
        } else if (index == player2StoreIndex && playerTurn == 1) return true;
        else {
            return false;
        }
    }

    public static boolean gameOver(Board board, int playerTurn){
        boolean gameOver = true;
        Pit[] player1Board = board.getPlayer(1).getPlayerBoard();
        Pit[] player2Board = board.getPlayer(2).getPlayerBoard();

        if (playerTurn == 1){
            for (int i=0;i<player1Board.length-1;i++){
                if (player1Board[i].getSeeds() != 0){
                    return false;
                }
            }
        } else {
            for (int i=0;i<player2Board.length-1;i++){
                if (player2Board[i].getSeeds() != 0){
                    return false;
                }
            }
        }
        return gameOver;
    }

    public static int returnNextPlayer(int playerTurn){
        if (playerTurn == 1) return 2; else return 1;
    }

    public static List<Pit> toList(Pit[] playerBoard){
        List<Pit> intList = new ArrayList<>();
        for (int index = 0; index < playerBoard.length; index++)
        {
            intList.add(playerBoard[index]);
        }
        return intList;
    }
}
