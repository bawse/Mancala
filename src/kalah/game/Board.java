package kalah.game;

import com.google.common.collect.ObjectArrays;
import kalah.util.IOHandler;

import java.util.Arrays;

/**
 * Created by Jay on 5/8/2017.
 * In this class, both player sides are represented as a 1D array in the form of {1,2,3,4,5,6,S} where the numbers
 * represent the house number, and S represents the player store.
 */
public class Board extends Outcome{

    private int playerTurn;
    //private Pit[] gameBoard;
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

    public void initializeGameBoard(){
        Pit[] player1Board = new Pit[NUM_HOUSES_EACH + 1];
        Pit[] player2Board = new Pit[NUM_HOUSES_EACH + 1]; // + 1 for the store.
        //gameBoard = new Pit[2*(NUM_HOUSES_EACH + 1)];

        for (int i=0;i<=NUM_HOUSES_EACH;i++){
            if (i == NUM_HOUSES_EACH) {
                player1Board[i] = new Store(0, 1); // start with 0 seeds in each house
                player2Board[i] = new Store(0,2);
            } else {
                player1Board[i] = new House(NUM_SEEDS_IN_HOUSE, 1, i+1);
                player2Board[i] = new House(NUM_SEEDS_IN_HOUSE, 2, i+1);
            }
        }
        player1 = new Player(1, player1Board);
        player2 = new Player(2,player2Board);
        //gameBoard = ObjectArrays.concat(player1Board, player2Board,Pit.class);
    }

    public boolean gameOver(){

        //updatePlayerBoards();
        boolean gameOver = true;
        IOHandler.printGameBoard(player1.getPlayerBoard());
        IOHandler.printGameBoard(player2.getPlayerBoard());

        System.out.println("game over player turn: " + playerTurn);
        Pit[] player1Board = player1.getPlayerBoard();
        Pit[] player2Board = player2.getPlayerBoard();

        if (this.playerTurn == 1){
            for (int i=0;i<player1.getPlayerBoard().length-1;i++){
                if (player1Board[i].getSeeds() != 0){
                    System.out.println(player1Board[i].getSeeds());
                    return false;
                }
            }
        } else {
            for (int i=0;i<player2Board.length-1;i++){
                if (player2Board[i].getSeeds() != 0){
                    System.out.println(player2Board[i].getSeeds());
                    return false;
                }
            }
        }
        return gameOver;
    }

    public void updatePlayerBoards(Pit[] gameBoard){
        player1.setPlayerBoard(Arrays.copyOfRange(gameBoard, 0, gameBoardLength/2));
        player2.setPlayerBoard(Arrays.copyOfRange(gameBoard, gameBoardLength/2, gameBoardLength ));
    }

    public Pit[] getGameBoard(){
        return ObjectArrays.concat(player1.getPlayerBoard(), player2.getPlayerBoard(),Pit.class);
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

        int finalIndex = 0;

        for (int i=startIndex; seeds>0;i++){
            if (i>gameBoardLength-1){
                i=0;
            }
            finalIndex = i;
            if (!otherPlayerStore(i)){
                gameBoard[i].incrementSeeds();
                seeds--;
            }
        }
        updatePlayerBoards(gameBoard);
        //IOHandler.printGameBoard(gameBoard);

        if (outcome1(finalIndex,playerTurn, gameBoard)){
            System.out.println("outcome1");
            this.playerTurn = returnNextPlayer(playerTurn);
            return returnNextPlayer(playerTurn);
        } else if (outcome2(finalIndex, playerTurn, gameBoard)){
            System.out.println("outcome2 returning " + playerTurn);
            return playerTurn; // player gets another turn;
        } else if (outcome3(finalIndex, playerTurn, gameBoard, player1, player2)){
            System.out.println("outcome3");
            gameBoard = capture(finalIndex, playerTurn, gameBoard, player1, player2);
            updatePlayerBoards(gameBoard);
            this.playerTurn = returnNextPlayer(playerTurn);
            return returnNextPlayer(playerTurn);
        } else {
            this.playerTurn = returnNextPlayer(playerTurn);
            return returnNextPlayer(playerTurn);
        }
    }



    public boolean otherPlayerStore(int index){

        int player1StoreIndex = (gameBoardLength/2) - 1;
        int player2StoreIndex = gameBoardLength - 1;

        if (index == player1StoreIndex && playerTurn == 2){
            return true;
        } else if (index == player2StoreIndex && playerTurn == 1) return true;
        else {
            return false;
        }

    }

    public int returnNextPlayer(int playerTurn){
        if (playerTurn == 1){
            return 2;
        } else {
            return 1;
        }
    }

}
