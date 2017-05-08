package kalah.game;

import com.google.common.collect.Iterables;
import com.google.common.collect.ObjectArrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jay on 5/8/2017.
 */
public abstract class Outcome implements Strategy {

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

        List<Pit> player2List = toList(player2Side);
        List<Pit> player1List = toList(player1Side);
        Collections.reverse(player2List);

        if (playerTurn == 1){
            return (player2List.get((houseNo - 1)).getSeeds() > 0);
        } else {
            return (player1List.get(player1List.size() - houseNo).getSeeds() > 0);
        }
    }

    public Pit[] capture (int finalIndex, int playerTurn, Pit[] gameBoard, Player player1, Player player2){

        //IOHandler.printGameBoard(gameBoard);
        int player1StoreIndex = (gameBoard.length/2) - 1;
        int player2StoreIndex = gameBoard.length - 1;
        int player1Store = gameBoard[player1StoreIndex].getSeeds();
        int player2Store = gameBoard[player2StoreIndex].getSeeds();
        //System.out.println("Player1store " + player1Store);
        //System.out.println("Player2stoe " + player2Store);

        gameBoard[finalIndex].setSeeds(0); // need to subtract last sown seed as we need to add it to the player store
        //IOHandler.printGameBoard(gameBoard);
//        player1.setPlayerBoard(Arrays.copyOfRange(gameBoard, 0, gameBoard.length/2));
//        player2.setPlayerBoard(Arrays.copyOfRange(gameBoard, gameBoard.length/2, gameBoard.length ));
        int houseNo;
        if (finalIndex > (gameBoard.length/2 - 1)){
            houseNo = finalIndex - gameBoard.length/2 + 1;
        } else {
            houseNo = finalIndex + 1;
        }

        Pit[] player2Side = Arrays.copyOfRange(player2.getPlayerBoard(),0, player2.getPlayerBoard().length -1); // player2 without store
        Pit[] player1Side = Arrays.copyOfRange(player1.getPlayerBoard(), 0, player1.getPlayerBoard().length - 1); // player1 without store

        List<Pit> player2List = toList(player2Side);
        List<Pit> player1List = toList(player1Side);
        Collections.reverse(player2List);
        //IOHandler.printGameBoard(listToArray(player2List));
        List<Pit> rearrangedBoard = new ArrayList<>();
        rearrangedBoard.addAll(player1List); rearrangedBoard.addAll(player2List);
        //IOHandler.printGameBoard(listToArray(rearrangedBoard));

        int seedsToTransfer = 1; // last sown seed

        if (playerTurn == 1){
            seedsToTransfer = seedsToTransfer + rearrangedBoard.get((houseNo - 1) + rearrangedBoard.size()/2).getSeeds();
            rearrangedBoard.set((houseNo - 1) + (rearrangedBoard.size()/2), new Pit(0,2));
            //IOHandler.printGameBoard(listToArray(rearrangedBoard));
            rearrangedBoard.add(rearrangedBoard.size()/2, new Pit(player1Store + seedsToTransfer,1));
            //IOHandler.printGameBoard(listToArray(rearrangedBoard));
            rearrangedBoard.add(rearrangedBoard.size()/2 + 1, new Pit(player2Store,2));
            //IOHandler.printGameBoard(listToArray(rearrangedBoard));
        } else {
            seedsToTransfer = seedsToTransfer + rearrangedBoard.get(rearrangedBoard.size() - houseNo - (rearrangedBoard.size()/2)).getSeeds();
            rearrangedBoard.set(rearrangedBoard.size() - houseNo - (rearrangedBoard.size()/2), new Pit(0,1));
            rearrangedBoard.add(rearrangedBoard.size()/2, new Pit(player1Store,1));
            rearrangedBoard.add(rearrangedBoard.size()/2 + 1, new Pit(player2Store + seedsToTransfer, 2));

        }

        Pit[] rearrangedBoardArray = Iterables.toArray(rearrangedBoard, Pit.class);
        //IOHandler.printGameBoard(rearrangedBoardArray);
        //System.out.println(Arrays.toString(rearrangedBoardArray));

        Pit[] p1 = Arrays.copyOfRange(rearrangedBoardArray, 0, rearrangedBoardArray.length/2);
        //IOHandler.printGameBoard(p1);
        Pit[] p2 = Arrays.copyOfRange(rearrangedBoardArray, rearrangedBoardArray.length/2, rearrangedBoardArray.length);
        //IOHandler.printGameBoard(p2);
        Collections.reverse(Arrays.asList(p2));
        //IOHandler.printGameBoard(p2);
        Pit[] newBoard = ObjectArrays.concat(p1,p2, Pit.class);
        return newBoard;
    }

    public static List<Pit> toList(Pit[] playerBoard){
        List<Pit> intList = new ArrayList<>();
        for (int index = 0; index < playerBoard.length; index++)
        {
            intList.add(playerBoard[index]);
        }
        return intList;
    }

    public Pit[] listToArray(List<Pit> list){
        Pit[] array = new Pit[list.size()];

        for (int i=0;i<list.size();i++){
            array[i] = list.get(i);
        }
        return array;
    }


}
