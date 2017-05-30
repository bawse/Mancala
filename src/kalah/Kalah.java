package kalah;


import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.game.Board;
import kalah.util.IOHandler;



/**
 * This class is the starting point for the Modifiability Assignment.
 * The game board is represented as a 1D array as follows:
 * {1,2,3,4,5,6,P1Store | 1,2,3,4,5,6,P2Store}
 */
public class Kalah {

	static final int NUM_HOUSES_EACH = 6;
	static final int NUM_SEEDS_PER_HOUSE = 4;
	int boardLength = NUM_HOUSES_EACH * 2 + 2;
	boolean robotPlayer = false;
	IO io;

	//int[] board = new int[]{4,4,4,4,4,4,0,4,4,4,4,4,4,0};

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
		this.io = io;
		IOHandler printer = new IOHandler(io);
		// Replace what's below with your implementation
//		io.println("+----+-------+-------+-------+-------+-------+-------+----+");
//		io.println("| P2 | 6[ 4] | 5[ 4] | 4[ 4] | 3[ 4] | 2[ 4] | 1[ 4] |  0 |");
//		io.println("|    |-------+-------+-------+-------+-------+-------|    |");
//		io.println("|  0 | 1[ 4] | 2[ 4] | 3[ 4] | 4[ 4] | 5[ 4] | 6[ 4] | P1 |");
//		io.println("+----+-------+-------+-------+-------+-------+-------+----+");
//		io.println("Player 1's turn - Specify house number or 'q' to quit: ");
		int playerTurn = 1;
		//int indexMultiplier = 1; //player 1 always goes first
		int houseNumber;

		Board board = new Board(playerTurn,NUM_HOUSES_EACH,NUM_SEEDS_PER_HOUSE, robotPlayer);
		printer.boardString(board.getGameBoard());
		while (!board.gameOver() && (houseNumber = io.readInteger("Player P"+playerTurn+"'s turn - Specify house number or 'q' to quit: ",1,6,-1,"q")) >= 0) {

			playerTurn = board.performMove(houseNumber, playerTurn);
			printer.boardString(board.getGameBoard());
		}
		//game over message
		io.println("Game over");
		printer.boardString(board.getGameBoard());
		if (board.gameOver()) {
			printer.displayScore(board.getGameBoard());
		}

	}


}
