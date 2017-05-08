package kalah.game;

/**
 * Created by Jay on 5/8/2017.
 */
public class House extends Pit {

    private int houseNumber;
    public House(int seeds ,int belongsTo, int houseNumber){
        super(seeds,belongsTo);
        this.houseNumber = houseNumber;
    }
}
