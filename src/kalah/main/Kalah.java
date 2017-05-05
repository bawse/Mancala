package kalah.main;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is the starting point for the Modifiability Assignment.
 * The game board is represented as a 1D array as follows:
 * {1,2,3,4,5,6,P1Store | 1,2,3,4,5,6,P2Store}
 */
public class Kalah {

	static final int NUM_HOUSES_EACH = 6;

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
		int[] board = new int[]{15,4,4,4,4,4,0,4,4,4,4,4,4,0};
		// Replace what's below with your implementation
//		io.println("+----+-------+-------+-------+-------+-------+-------+----+");
//		io.println("| P2 | 6[ 4] | 5[ 4] | 4[ 4] | 3[ 4] | 2[ 4] | 1[ 4] |  0 |");
//		io.println("|    |-------+-------+-------+-------+-------+-------|    |");
//		io.println("|  0 | 1[ 4] | 2[ 4] | 3[ 4] | 4[ 4] | 5[ 4] | 6[ 4] | P1 |");
//		io.println("+----+-------+-------+-------+-------+-------+-------+----+");
//		io.println("Player 1's turn - Specify house number or 'q' to quit: ");
		int playerTurn = 1;
		//int indexMultiplier = 1; //player 1 always goes first
		String input = io.readFromKeyboard("Player 1's turn - Specify house number or 'q' to quit: "); // add min/max bounds 1-6
		int houseNumber = Integer.parseInt(input);

		performMove(board, playerTurn, houseNumber);

		//System.out.println(seeds);
//		for (int i=houseNumber+1;seeds>0;i++){
//			if (i > player1.length - 1){
//				int remainingSeeds = seeds;
//				for (int j=0;remainingSeeds>0;remainingSeeds--,j++){
//					player2[j]++;
//				}
//				break;
//			}
//			player1[i]++;
//			seeds--;
//		}
//
//		List<Integer> player2Board = toList(player2);
//		//Collections.reverse(player2Board);
//		System.out.println(Arrays.toString(player2Board.toArray()));
//		System.out.println(Arrays.toString(player1));

	}

	public int performMove(int[] board, int playerTurn, int houseNumber){ // returns a value that indicates which players turn it is after performing the move

		//TODO: THIS ONLY WORKS FOR PLAYER1 TURN - NEED TO CALCULATE HOUSEINDEX IF PLAYERTURN=2
		int seeds = board[houseNumber - 1];
		board[houseNumber - 1]=0;
		int finalIndex = 0;

		for (int i=houseNumber;seeds>0;i++){
			if (i>board.length-1){
				i=0;
			}
			finalIndex = i; // at the end of the loop, finalIndex will be the index at which the last seed was sown.
			if (!otherPlayerStore(i, playerTurn)) {
				board[i]++;
				seeds--;
			}
		}
		System.out.println(Arrays.toString(board));
		return playerTurn; // need to add checks for player turn
	}

	// returns true if the current board index is the other player's store. if it is, then a seed cannot be sown in it.
	public boolean otherPlayerStore(int index, int playerTurn){

		int boardLength = NUM_HOUSES_EACH * 2 + 2;
		int player1StoreIndex = (boardLength/2) - 1;
		int player2StoreIndex = boardLength - 1;
		int[] playerStores = {player1StoreIndex, player2StoreIndex};

		if (index == player1StoreIndex && playerTurn == 2){
			return true;
		} else if (index == player2StoreIndex && playerTurn == 1){
			return true;
		} else {
			return false;
		}

	}

	public boolean outcome1(int finalIndex, int playerTurn){

	}

	public List<Integer> toList(int[] intArray){
		List<Integer> intList = new ArrayList<Integer>();
		for (int index = 0; index < intArray.length; index++)
		{
			intList.add(intArray[index]);
		}
		return intList;
	}
}
