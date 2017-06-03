package kalah;


import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.game.Board;
import kalah.util.BoardUtils;
import kalah.util.IOHandler;

/**
 * This class is the starting point for the Modifiability Assignment.
 * The game board is represented as a 1D array as follows:
 * {1,2,3,4,5,6,P1Store | 1,2,3,4,5,6,P2Store}
 */
public class Kalah {

	static int NUM_HOUSES_EACH;
	static  int NUM_SEEDS_PER_HOUSE;
	IO io;

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
		this.io = io;
		IOHandler printer = new IOHandler(io);

        setHouses(6);
        setSeeds(4);
		int playerTurn = 1;
		int houseNumber;

		Board board = new Board(playerTurn,NUM_HOUSES_EACH,NUM_SEEDS_PER_HOUSE);
		printer.boardString(board.getGameBoard());
        //(!BoardUtils.gameOver(board, playerTurn) && board.getPlayer(playerTurn) instanceof RobotPlayer && (houseNumber = ((RobotPlayer)board.getPlayer(playerTurn)).getMove(board)) >= 0) ||
		while (!BoardUtils.gameOver(board, playerTurn) && (houseNumber = io.readInteger("Player P"+playerTurn+"'s turn - Specify house number or 'q' to quit: ",1,6,-1,"q")) >= 0) {

			playerTurn = board.performMove(houseNumber, playerTurn);
			printer.boardString(board.getGameBoard());
		}
		//game over message
		io.println("Game over");
		printer.boardString(board.getGameBoard());
		if (BoardUtils.gameOver(board, playerTurn)) {
			printer.displayScore(board.getGameBoard());
		}

	}

	public void setHouses(int numHouses){
	    this.NUM_HOUSES_EACH = numHouses;
    }
    public void setSeeds(int numSeeds){
        this.NUM_SEEDS_PER_HOUSE = numSeeds;
    }


}
