package kalah;


import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.game.Board;
import kalah.game.RobotPlayer;
import kalah.util.BoardUtils;
import kalah.util.IOHandler;

/**
 * This class is the starting point for the Modifiability Assignment.
 * The game board is represented as a 1D array as follows:
 * {1,2,3,4,5,6,P1Store | 1,2,3,4,5,6,P2Store}
 */
public class Kalah {

	static final int NUM_HOUSES_EACH = 6;
	static final int NUM_SEEDS_PER_HOUSE = 4;
	boolean robotPlayer = false;
	IO io;

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
		this.io = io;
		IOHandler printer = new IOHandler(io);

		int playerTurn = 1;
		int houseNumber;

		Board board = new Board(playerTurn,NUM_HOUSES_EACH,NUM_SEEDS_PER_HOUSE, robotPlayer);
		printer.boardString(board.getGameBoard());
		while ((!BoardUtils.gameOver(board, playerTurn) && board.getPlayer(playerTurn) instanceof RobotPlayer && (houseNumber = ((RobotPlayer)board.getPlayer(playerTurn)).getMove(board)) >= 0) ||
				!BoardUtils.gameOver(board, playerTurn) && (houseNumber = io.readInteger("Player P"+playerTurn+"'s turn - Specify house number or 'q' to quit: ",1,6,-1,"q")) >= 0) {

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


}
