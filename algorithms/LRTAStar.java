package algorithms;

import grid.Grid;
import util.*;

public class LRTAStar extends Algorithms {

    public LRTAStar(Grid grid, int grass_cost, String world_name) {
        super(grid, grass_cost);
        calc(grid.getStartidx(), grid.getTerminalidx());
        grid.VisualizeGrid(world_name + toString(), steps.stream().mapToInt(i -> i).toArray());
    }

    void calc(int start, int end) {

    }

    private void LRTAStarHelper(int[] result, int[] H, int s, int a){

    }

    private int LRTACost(int s, int a, int sAlt, int[] H){
        return 0;
    }

    @Override
    public String toString() {
        return ": LRTA-Star (" + Util.getCostType(Util.CELL_TYPES.GRASS) + ")";
    }
}