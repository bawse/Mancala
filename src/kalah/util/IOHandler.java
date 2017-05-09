package kalah.util;

import com.qualitascorpus.testsupport.IO;
import kalah.game.Pit;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Jay on 5/8/2017.
 */
public class IOHandler {
    public static IO io;

    public IOHandler(IO io){
        this.io = io;
    }

    public void boardString(Pit[] board){
        StringBuilder stringBuilder = new StringBuilder();
        Pit[] p1 = Arrays.copyOfRange(board, 0, board.length/2);
        Pit[] p2 = Arrays.copyOfRange(board, board.length/2, board.length);
        int p1Store = p1[p1.length - 1].getSeeds();
        int p2Store = p2[p2.length - 1].getSeeds();
        Collections.reverse(Arrays.asList(p2));
        io.println("+----+-------+-------+-------+-------+-------+-------+----+");

        for (int i=0; i<p2.length + 1;i++){
            if (i == 0){
                stringBuilder.append("| P2 | ");
            } else if (i == (p2.length)){
                stringBuilder.append(seedString(p1Store) + " |");
            } else {
                stringBuilder.append(p2.length - i); // house number
                stringBuilder.append("[" + seedString(p2[i].getSeeds()) + "] | ");
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
                stringBuilder.append("[" + seedString(p1[i-1].getSeeds()) + "] | ");
            }
        }
        io.println(stringBuilder.toString());
        io.println("+----+-------+-------+-------+-------+-------+-------+----+");
    }

//    public Pit[] reverse(Pit[] original){
//
//    }

    public void displayScore(Pit[] board){

        Pit[] p1 = Arrays.copyOfRange(board, 0, board.length/2);
        Pit[] p2 = Arrays.copyOfRange(board, board.length/2, board.length);

        int player1Score = sum(p1);
        int player2Score = sum(p2);

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

    public String seedString(int seeds){
        if (seeds >=10){
            return "" + seeds;
        } else {
            return " " + seeds;
        }
    }

    public int sum(Pit[] playerBoard){
        int sum = 0;

        for (int i=0;i<playerBoard.length;i++){
            sum = sum + playerBoard[i].getSeeds();
        }
        return sum;
    }

    public static void printGameBoard(Pit[] gameBoard){
        int[] array = new int[gameBoard.length];
        for (int i=0;i<gameBoard.length;i++){
            array[i] = gameBoard[i].getSeeds();
        }
        System.out.println(Arrays.toString(array));
    }
}
