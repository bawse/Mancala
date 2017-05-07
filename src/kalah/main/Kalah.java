package kalah.main;

import com.google.common.primitives.Ints;
import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class is the starting point for the Modifiability Assignment.
 * The game board is represented as a 1D array as follows:
 * {1,2,3,4,5,6,P1Store | 1,2,3,4,5,6,P2Store}
 */
public class Kalah {

	static final int NUM_HOUSES_EACH = 6;
	int boardLength = NUM_HOUSES_EACH * 2 + 2;
	IO io;

	int[] board = new int[]{4,4,4,4,4,4,0,4,4,4,4,4,4,0};

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
		this.io = io;
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

		boardString(board, io, playerTurn);
		while (!gameOver(board, playerTurn) && (houseNumber = io.readInteger("Player P"+playerTurn+"'s turn - Specify house number or 'q' to quit: ",1,6,-1,"q")) >= 0) {

			playerTurn = performMove(board, playerTurn, houseNumber);
			boardString(board, io, playerTurn);
		}
		//game over message
		io.println("Game over");
		boardString(board,io,playerTurn);
		if (gameOver(board, playerTurn)) {
			displayScore(board);
		}

	}

	public void displayScore(int[] board){

		int[] p1 = Arrays.copyOfRange(board, 0, board.length/2);
		int[] p2 = Arrays.copyOfRange(board, board.length/2, board.length);

		int player1Score = IntStream.of(p1).sum();
		int player2Score = IntStream.of(p2).sum();

		io.println("\t" + "player 1:" + player1Score);
		io.println("\t" + "player 2:" + player2Score);
		if (player1Score > player2Score){
			io.println("Player 1 wins!");
		} else if (player2Score > player1Score){
			io.println("Player 2 wins!");
		} else {
			io.println("A tie!");
		}
	}

	public boolean gameOver(int[] board, int playerTurn){
		int[] p1 = Arrays.copyOfRange(board, 0, board.length/2 -1);
		int[] p2 = Arrays.copyOfRange(board, board.length/2, board.length -1 );

		boolean gameOver = true;
		if (playerTurn == 1){
			for (int seeds : p1){
				if (seeds != 0){
					return false;
				}
			}
		} else {
			for (int seeds : p2){
				if (seeds != 0){
					return false;
				}
			}
		}
		return gameOver;

	}

	public void setBoard(int[] newBoard){
		this.board = newBoard;
	}

	public void boardString(int[] board, IO io, int playerTurn){
		StringBuilder stringBuilder = new StringBuilder();
		int[] p1 = Arrays.copyOfRange(board, 0, board.length/2);
		int[] p2 = Arrays.copyOfRange(board, board.length/2, board.length);
		int p1Store = p1[p1.length - 1];
		int p2Store = p2[p2.length - 1];
		Collections.reverse(Ints.asList(p2));
		io.println("+----+-------+-------+-------+-------+-------+-------+----+");

		for (int i=0; i<p2.length + 1;i++){
			if (i == 0){
				stringBuilder.append("| P2 | ");
			} else if (i == (p2.length)){
				stringBuilder.append(seedString(p1Store) + " |");
			} else {
				stringBuilder.append(p2.length - i); // house number
				stringBuilder.append("[" + seedString(p2[i]) + "] | ");
			}
		}
		io.println(stringBuilder.toString());
		stringBuilder.setLength(0);
		io.println("|    |-------+-------+-------+-------+-------+-------|    |");

		for (int i=0;i<p1.length + 1;i++){
			if (i==0){
				stringBuilder.append("| "+seedString(p2Store)+" | ");
			} else if (i == p2.length){
				stringBuilder.append("P1 |");
			} else {
				stringBuilder.append(i); // house number
				stringBuilder.append("[" + seedString(p1[i-1]) + "] | ");
			}
		}
		io.println(stringBuilder.toString());
		io.println("+----+-------+-------+-------+-------+-------+-------+----+");
	}


	public String seedString(int seeds){
		if (seeds >=10){
			return "" + seeds;
		} else {
			return " " + seeds;
		}
	}

	public int performMove(int[] board, int playerTurn, int houseNumber){ // returns a value that indicates which players turn it is after performing the move


		int seeds;
		int startIndex;
		if (playerTurn == 1){
			seeds = board[houseNumber -1];
			if (seeds == 0){
				io.println("House is empty. Move again.");
				return playerTurn;
			}
			board[houseNumber - 1]=0;
			startIndex = houseNumber;
		} else {
			seeds = board[houseNumber + 6];
			if (seeds == 0){
				io.println("House is empty. Move again.");
				return playerTurn;
			}
			board[houseNumber + 6] = 0;
			startIndex = houseNumber + 7; // change hard-coded numbers to use constants during refactor
		}

		int finalIndex = 0;

		for (int i=startIndex;seeds>0;i++){
			if (i>board.length-1){
				i=0;
			}
			finalIndex = i; // at the end of the loop, finalIndex will be the index at which the last seed was sown.
			if (!otherPlayerStore(i, playerTurn)) {
				board[i]++;
				seeds--;
			}
		}
		if (outcome1(finalIndex, playerTurn, board)){

			//System.out.println("outcome1");
			if (playerTurn == 1){
				return 2;
            } else { return 1; }
        } else if (outcome2(finalIndex, playerTurn, board)){
			//System.out.println("outcome2");
			return playerTurn; // player gets another turn
		} else if (outcome3(finalIndex, playerTurn, board)){
			board = capture(finalIndex, playerTurn, board);
			//System.out.println("outcome3");
			//System.out.println(Arrays.toString(board));
			setBoard(board);
			if (playerTurn == 1) return 2; else return 1;
		} else {
			if (playerTurn == 1) return 2; else return 1;
		}
		//System.out.println(Arrays.toString(board));
		//return playerTurn; // need to add checks for player turn
	}

	// returns true if the current board index is the other player's store. if it is, then a seed cannot be sown in it.
	public boolean otherPlayerStore(int index, int playerTurn){

		int player1StoreIndex = (boardLength/2) - 1;
		int player2StoreIndex = boardLength - 1;
		int[] playerStores = {player1StoreIndex, player2StoreIndex};

		if (index == player1StoreIndex && playerTurn == 2){
			return true;
		} else if (index == player2StoreIndex && playerTurn == 1) return true;
		else {
			return false;
		}

	}


	public boolean ownedByPlayer(int finalIndex, int playerTurn, int[] board){
		int houseNo;
		boolean ownHouse;

		if (finalIndex < (board.length/2) -1){
			houseNo = finalIndex + 1;
			if (playerTurn == 1 && houseNo < 7){ // if finalIndex is on left-hand side of board and its P1's turn, then they own the house
				ownHouse = true;
			} else {
				ownHouse = false;
			}
		} else {
			houseNo = finalIndex - 6; // change 6 to NUMBER_HOUSES constant later
			if (playerTurn  == 2 && houseNo < 7){ // probably dont need the second condition
				ownHouse = true; // if the finalIndex is on the right-hand side of the 1D board and it's P2's turn, then they own the house
			} else {
				ownHouse = false;
			}
		}
		return ownHouse;
	}
	public boolean outcome1(int finalIndex, int playerTurn, int[] board){

		int houseNo;
		boolean ownHouse;

		if (finalIndex <= (board.length/2) -1){
			houseNo = finalIndex + 1;
			if (playerTurn == 1){ // if finalIndex is on left-hand side of board and its P1's turn, then they own the house
				ownHouse = true;
			} else {
				ownHouse = false;
			}
		} else {
			houseNo = finalIndex - 6; // change 6 to NUMBER_HOUSES constant later
			if (playerTurn  == 2){
				ownHouse = true; // if the finalIndex is on the right-hand side of the 1D board and it's P2's turn, then they own the house
			} else {
				ownHouse = false;
			}
		}

		if (houseNo > 6){ // change 6 to use constant as above
			return false; // house > 6 means that the last seed was sown on a store not a house
		}

		if (!ownHouse || board[finalIndex] - 1 >= 1){ // the seed has already been sown so need to subtract 1 to check if there were no seeds initially
			return true;
		} else {
			//System.out.println("There were initially " + (board[finalIndex] - 1 + " seeds in  house " + (finalIndex + 1)));
			return false;
		}
	}

	public boolean outcome2(int finalIndex, int playerTurn, int[] board){

		int player1StoreIndex = (boardLength/2) - 1;
		int player2StoreIndex = boardLength - 1;

		if (finalIndex == player1StoreIndex && playerTurn == 1){
			return true;
		} else return finalIndex == player2StoreIndex && playerTurn == 2;
	}

	public boolean outcome3(int finalIndex, int playerTurn, int[] board){

		if (ownedByPlayer(finalIndex, playerTurn, board) && board[finalIndex]-1 == 0 && oppositeHouseNotEmpty(finalIndex, playerTurn, board)){
			return true;
		} else {
			return false;
		}
	}

	public int[] capture (int finalIndex, int playerTurn, int[] board){

		int player1StoreIndex = (boardLength/2) - 1;
		int player2StoreIndex = boardLength - 1;
		int player1Store = board[player1StoreIndex];
		int player2Store = board[player2StoreIndex];

		board[finalIndex]--; // need to subtract last sown seed as we need to add it to the player store

		int houseNo;
		if (finalIndex > (boardLength/2 - 1)){
			houseNo = finalIndex - boardLength/2 + 1;
		} else {
			houseNo = finalIndex + 1;
		}

		int[] clone = board.clone();
		int[] player2Side = Arrays.copyOfRange(clone,boardLength/2, boardLength - 1); // player2 without store
		int[] player1Side = Arrays.copyOfRange(clone, 0, boardLength/2 - 1); // player1 without store

		List<Integer> player2List = toList(player2Side);
		List<Integer> player1List = toList(player1Side);
		Collections.reverse(player2List);
		List<Integer> rearrangedBoard = new ArrayList<>();
		rearrangedBoard.addAll(player1List); rearrangedBoard.addAll(player2List);

		int seedsToTransfer = 1; // last sown seed

		if (playerTurn == 1){
			seedsToTransfer = seedsToTransfer + rearrangedBoard.get((houseNo - 1) + rearrangedBoard.size()/2);
			rearrangedBoard.set((houseNo - 1) + (rearrangedBoard.size()/2), 0);
			rearrangedBoard.add(rearrangedBoard.size()/2, player1Store + seedsToTransfer);
			rearrangedBoard.add(rearrangedBoard.size()/2 + 1, player2Store);
		} else {
			seedsToTransfer = seedsToTransfer + rearrangedBoard.get(rearrangedBoard.size() - houseNo - (rearrangedBoard.size()/2));
			rearrangedBoard.set(rearrangedBoard.size() - houseNo - (rearrangedBoard.size()/2), 0);
			rearrangedBoard.add(rearrangedBoard.size()/2, player1Store);
			rearrangedBoard.add(rearrangedBoard.size()/2 + 1, player2Store + seedsToTransfer);

		}
		int[] rearrangedBoardArray = Ints.toArray(rearrangedBoard);
		//System.out.println(Arrays.toString(rearrangedBoardArray));

		int[] p1 = Arrays.copyOfRange(rearrangedBoardArray, 0, rearrangedBoardArray.length/2);
		int[] p2 = Arrays.copyOfRange(rearrangedBoardArray, rearrangedBoardArray.length/2, rearrangedBoardArray.length);
		Collections.reverse(Ints.asList(p2));
		int[] newBoard = Ints.concat(p1,p2);

		return newBoard;
	}

	public boolean oppositeHouseNotEmpty(int finalIndex, int playerTurn, int[] board){

		int houseNo;
		if (finalIndex > (boardLength/2 - 1)){
			houseNo = finalIndex - boardLength/2 + 1;
		} else {
			houseNo = finalIndex + 1;
		}

		int[] clone = board.clone();
		int[] player2Side = Arrays.copyOfRange(clone,boardLength/2, boardLength - 1); // player2 without store
		int[] player1Side = Arrays.copyOfRange(clone, 0, boardLength/2 - 1); // player1 without store

		List<Integer> player2List = toList(player2Side);
		List<Integer> player1List = toList(player1Side);
		Collections.reverse(player2List);

		if (playerTurn == 1){
			//System.out.println(houseNo - 1); // 6
			return (player2List.get((houseNo - 1)) > 0);
		} else {
			return (player1List.get(player1List.size() - houseNo) > 0);
		}


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
