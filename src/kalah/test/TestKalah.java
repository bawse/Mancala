package kalah.test;

import junit.framework.TestCase;

import kalah.Kalah;

import com.qualitascorpus.testsupport.MockIO;
/**
 * Provides tests for standard cases for Kalah. Works for both the
 * standard capture rule and the empty house capture rule (just change
 * the test spec files) but assumes the endgame check is done at the 
 * end of the move.
 */
public class TestKalah extends TestCase {
	
	private MockIO _mockIO;
	public void setUp() {
		_mockIO = new MockIO();
	}
	public void tearDown() {
		_mockIO.finished();
	}
	public void testQuit() {
		playGame("/test/quit.txt");
	}
	public void testSimpleStart() {
		playGame("/test/simple_start.txt");
	}
	public void testP1Continue() {
		playGame("/test/p1_continue.txt");
	}
	public void testSimpleTwoMoves() {
		playGame("/test/simple_two_moves.txt");
	}
	public void testSingleWrap() {
		playGame("/test/single_wrap.txt");
	}
	public void testContinueWrap() {
		playGame("/test/continue_wrap.txt");
	}
	public void testCapture() {
		playGame("/test/capture.txt");
	}
	public void testEmptyHouseCapture() {
		playGame("/test/endinempty.txt");
	}


	/**
	 * Player 1 wins
	 */
	public void testFullGame1() {
		playGame("/test/full_game1.txt");
	}

	/**
	 * Player 2 wins
	 */
	public void testFullGame2() {
		playGame("/test/full_game2.txt");
	}

	/**
	 * Player 2 wins by moving the last seed into its store
	 */
	public void testFullGameStore() {
		playGame("/test/full_game_store.txt");
	}

	/**
	 * Player 2 wins by moving the last seed into its store
	 */
	public void testFullGameEmpty() {
		playGame("/test/full_game_empty.txt");
	}

	/**
	 * Tie
	 */
	public void testFullGameTie() {
		playGame("/test/full_game_tie.txt");
	}

	/**
	 * This distinguishes whether the end of game check is done at the
	 * beginning of the turn or the end
	 */
	public void testFullGameEmptyHouses() {
		playGame("/test/full_game_empty_houses.txt");
	}
	
	/*
	 * Various non-standard or rare situations
	 */
	
	public void testUseEmpty() {
		playGame("/test/useempty.txt");
	}

	public void testWrapped() {
		playGame("/test/wrapped.txt");
	}

	public void testDoubleWrap() {
		playGame("/test/double_wrap.txt");
	}

	public void testExactLapCapture() {
		playGame("/test/exactlapcapture.txt");
	}

	public void testExactLapEmptyCapture() {
		playGame("/test/exactlapemptycapture.txt");
	}
	
	private void playGame(String spec) {
		_mockIO.setExpected(spec);
		new Kalah().play(_mockIO);
	}
}
