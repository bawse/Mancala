package kalah.game;

import kalah.util.BoardUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jay on 5/8/2017.
 */
public abstract class Outcome implements OutcomeStrategy {

    int NUM_HOUSES_EACH = 6; // default value is 6

    public void setNUM_HOUSES_EACH(int NUM_HOUSES_EACH) {
        this.NUM_HOUSES_EACH = NUM_HOUSES_EACH;
    }

    @Override
    public boolean outcome1(int finalIndex, int playerTurn, Pit[] gameBoard){

        int houseNo;
        boolean ownHouse;

        if (finalIndex <= (gameBoard.length/2) -1){
            houseNo = finalIndex + 1;
            if (gameBoard[finalIndex].getBelongsTo() == playerTurn){
                ownHouse = true;
            } else {
                ownHouse = false;
            }
        } else {
            houseNo = finalIndex - NUM_HOUSES_EACH;
            if (gameBoard[finalIndex].getBelongsTo() == playerTurn){
                ownHouse = true; // if the finalIndex is on the right-hand side of the 1D board and it's P2's turn, then they own the house
            } else {
                ownHouse = false;
            }
        }

        if (houseNo > NUM_HOUSES_EACH){
            return false; // house > NUM_HOUSES_EACH means that the last seed was sown on a store not a house
        }

        if (!ownHouse || gameBoard[finalIndex].getSeeds() - 1 >= 1){ // the seed has already been sown so need to subtract 1 to check if there were no seeds initially
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean outcome2(int finalIndex, int playerTurn, Pit[] gameBoard) {
        int player1StoreIndex = (gameBoard.length/2) - 1;
        int player2StoreIndex = gameBoard.length - 1;

        if (finalIndex == player1StoreIndex && playerTurn == 1){
            return true;
        } else return finalIndex == player2StoreIndex && playerTurn == 2;
    }

    @Override
    public boolean outcome3(int finalIndex, int playerTurn, Pit[] gameBoard, Player player1, Player player2) {
        if (ownedByPlayer(finalIndex, playerTurn, gameBoard) && gameBoard[finalIndex].getSeeds()-1 == 0 && oppositeHouseNotEmpty(finalIndex, playerTurn, gameBoard, player1, player2)){
            return true;
        } else {
            return false;
        }
    }

    public boolean ownedByPlayer(int finalIndex, int playerTurn, Pit[] gameBoard){
        int houseNo;
        boolean ownHouse;

        if (finalIndex < (gameBoard.length/2) -1){
            houseNo = finalIndex + 1;
            if (playerTurn == 1 && houseNo < NUM_HOUSES_EACH + 1){ // if finalIndex is on left-hand side of board and its P1's turn, then they own the house
                ownHouse = true;
            } else {
                ownHouse = false;
            }
        } else {
            houseNo = finalIndex - NUM_HOUSES_EACH;
            if (playerTurn  == 2 && houseNo < NUM_HOUSES_EACH + 1){
                ownHouse = true; // if the finalIndex is on the right-hand side of the 1D board and it's P2's turn, then they own the house
            } else {
                ownHouse = false;
            }
        }
        return ownHouse;
    }

    public boolean oppositeHouseNotEmpty(int finalIndex, int playerTurn, Pit[] gameBoard, Player player1, Player player2){
        int houseNo;
        if (finalIndex > (gameBoard.length/2 - 1)){
            houseNo = finalIndex - gameBoard.length/2 + 1;
        } else {
            houseNo = finalIndex + 1;
        }
        Pit[] player2Side = Arrays.copyOfRange(player2.getPlayerBoard(),0, player2.getPlayerBoard().length -1); // player2 without store
        Pit[] player1Side = Arrays.copyOfRange(player1.getPlayerBoard(), 0, player1.getPlayerBoard().length - 1); // player1 without store

        List<Pit> player2List = BoardUtils.toList(player2Side);
        List<Pit> player1List = BoardUtils.toList(player1Side);
        Collections.reverse(player2List);

        if (playerTurn == 1){
            return (player2List.get((houseNo - 1)).getSeeds() > 0);
        } else {
            return (player1List.get(player1List.size() - houseNo).getSeeds() > 0);
        }
    }



}
