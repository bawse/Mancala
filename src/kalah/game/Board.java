package kalah.game;

import kalah.util.BoardUtils;
import kalah.util.IOHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jay on 5/8/2017.
 * In this class, both player sides are represented as a 1D array in the form of {1,2,3,4,5,6,S} where the numbers
 * represent the house number, and S represents the player store.
 */
public class Board extends Outcome implements SeedDispersalStrategy{

    private int playerTurn;
    private Player player1;
    private Player player2;
    private int NUM_HOUSES_EACH; // default is 6 houses each + 1 store
    private int NUM_SEEDS_IN_HOUSE;
    private int gameBoardLength;

    public Board (int playerTurn, int numHouses, int numSeeds){
        this.playerTurn = playerTurn;
        this.NUM_HOUSES_EACH = numHouses;
        super.setNUM_HOUSES_EACH(this.NUM_HOUSES_EACH);
        this.NUM_SEEDS_IN_HOUSE = numSeeds;
        this.gameBoardLength = 2*numHouses + 2;
        initializeGameBoard();
    }

    public Player getPlayer(int playerNumber){
        if (playerNumber == 1) return player1; else return player2;
    }
    public void initializeGameBoard(){
        Pit[] player1Board = new Pit[NUM_HOUSES_EACH + 1];
        Pit[] player2Board = new Pit[NUM_HOUSES_EACH + 1]; // + 1 for the store.

        for (int i=0;i<=NUM_HOUSES_EACH;i++){
            if (i == NUM_HOUSES_EACH) {
                player1Board[i] = new Store(0, 1); // start with 0 seeds in each house
                player2Board[i] = new Store(0,2);
            } else {
                player1Board[i] = new House(NUM_SEEDS_IN_HOUSE, 1, i+1);
                player2Board[i] = new House(NUM_SEEDS_IN_HOUSE, 2, i+1);
            }
        }

        player1 = new HumanPlayer(1, player1Board);
        player2 = new HumanPlayer(2,player2Board);

        //player1 = new HumanPlayer(1, player1Board);
        //player2 = robotPlayer ? new RobotPlayer(2, player2Board) : new HumanPlayer(2,player2Board);
    }


    public void updatePlayerBoards(Pit[] gameBoard){
        player1.setPlayerBoard(Arrays.copyOfRange(gameBoard, 0, gameBoardLength/2));
        player2.setPlayerBoard(Arrays.copyOfRange(gameBoard, gameBoardLength/2, gameBoardLength ));
    }

    public Pit[] getGameBoard(){
        Pit[] p1 = player1.getPlayerBoard();
        Pit[] p2 = player2.getPlayerBoard();
        Pit[] newBoard = new Pit[p1.length + p2.length];

        System.arraycopy(p1,0,newBoard,0,p1.length);
        System.arraycopy(p2,0,newBoard,p1.length,p2.length);
        return newBoard;
    }

    public int performMove(int houseNumber, int playerTurn){
        this.playerTurn = playerTurn;
        int seeds;
        int startIndex;
        Pit[] gameBoard = getGameBoard();
        if (playerTurn == 1){
            seeds = gameBoard[houseNumber - 1].getSeeds();
            if (seeds == 0){
                IOHandler.io.println("House is empty. Move again.");
                return playerTurn;
            }
            gameBoard[houseNumber - 1].setSeeds(0);
            startIndex = houseNumber;
        } else {
            seeds = gameBoard[houseNumber + NUM_HOUSES_EACH].getSeeds();
            if (seeds == 0){
                IOHandler.io.println("House is empty. Move again.");
                return playerTurn;
            }
            gameBoard[houseNumber + NUM_HOUSES_EACH].setSeeds(0);
            startIndex = houseNumber + NUM_HOUSES_EACH + 1; // we need to start from the next house
        }

        int finalIndex;
        finalIndex = disperse(startIndex,seeds,gameBoard);

        if (outcome1(finalIndex,playerTurn, gameBoard)){
            this.playerTurn = BoardUtils.returnNextPlayer(playerTurn);
            return BoardUtils.returnNextPlayer(playerTurn);
        } else if (outcome2(finalIndex, playerTurn, gameBoard)){
            return playerTurn; // player gets another turn;
        } else if (outcome3(finalIndex, playerTurn, gameBoard, player1, player2)){
            gameBoard = capture(finalIndex, playerTurn, gameBoard);
            updatePlayerBoards(gameBoard);
            this.playerTurn = BoardUtils.returnNextPlayer(playerTurn);
            return BoardUtils.returnNextPlayer(playerTurn);
        } else {
            this.playerTurn = BoardUtils.returnNextPlayer(playerTurn);
            return BoardUtils.returnNextPlayer(playerTurn);
        }
    }

    public Pit[] capture (int finalIndex, int playerTurn, Pit[] gameBoard){

        int player1StoreIndex = (gameBoard.length/2) - 1;
        int player2StoreIndex = gameBoard.length - 1;
        int player1Store = gameBoard[player1StoreIndex].getSeeds();
        int player2Store = gameBoard[player2StoreIndex].getSeeds();

        gameBoard[finalIndex].setSeeds(0); // need to subtract last sown seed as we need to add it to the player store
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
        List<Pit> rearrangedBoard = new ArrayList<>();
        rearrangedBoard.addAll(player1List); rearrangedBoard.addAll(player2List);

        int seedsToTransfer = 1; // last sown seed

        if (playerTurn == 1){
            seedsToTransfer = seedsToTransfer + rearrangedBoard.get((houseNo - 1) + rearrangedBoard.size()/2).getSeeds();
            rearrangedBoard.set((houseNo - 1) + (rearrangedBoard.size()/2), new Pit(0,2));
            rearrangedBoard.add(rearrangedBoard.size()/2, new Pit(player1Store + seedsToTransfer,1)); // adding the stores with the captured seeds
            rearrangedBoard.add(rearrangedBoard.size()/2 + 1, new Pit(player2Store,2));
        } else {
            seedsToTransfer = seedsToTransfer + rearrangedBoard.get(rearrangedBoard.size() - houseNo - (rearrangedBoard.size()/2)).getSeeds();
            rearrangedBoard.set(rearrangedBoard.size() - houseNo - (rearrangedBoard.size()/2), new Pit(0,1));
            rearrangedBoard.add(rearrangedBoard.size()/2, new Pit(player1Store,1));
            rearrangedBoard.add(rearrangedBoard.size()/2 + 1, new Pit(player2Store + seedsToTransfer, 2));

        }

        Pit[] rearrangedBoardArray = rearrangedBoard.toArray(new Pit[rearrangedBoard.size()]);

        Pit[] p1 = Arrays.copyOfRange(rearrangedBoardArray, 0, rearrangedBoardArray.length/2);
        Pit[] p2 = Arrays.copyOfRange(rearrangedBoardArray, rearrangedBoardArray.length/2, rearrangedBoardArray.length);
        Collections.reverse(Arrays.asList(p2));
        Pit[] newBoard = new Pit[p1.length + p2.length];

        System.arraycopy(p1,0,newBoard,0,p1.length);
        System.arraycopy(p2,0,newBoard,p1.length,p2.length);
        return newBoard;
    }


    @Override
    public int disperse(int startIndex, int seeds, Pit[] gameBoard) {
        int finalIndex = 0;

        for (int i=startIndex; seeds>0;i++){
            if (i>gameBoardLength-1){
                i=0;
            }
            finalIndex = i;
            if (!BoardUtils.otherPlayerStore(i, gameBoardLength, playerTurn)){
                gameBoard[i].incrementSeeds();
                seeds--;
            }
        }
        updatePlayerBoards(gameBoard);
        return finalIndex;
    }
}
