package kalah.game;

/**
 * Created by Jay on 5/8/2017.
 */
public class Pit {
    private int seeds;
    private int belongsTo; // belongs to player 1 or 2

    public Pit(int seeds, int belongsTo){
        this.seeds = seeds;
        this.belongsTo = belongsTo;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }

    public void incrementSeeds(){
        this.seeds = this.seeds + 1;
    }

    public int getBelongsTo() {
        return belongsTo;
    }

}
