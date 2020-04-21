import java.util.HashMap;
import java.util.Map;

public class Util {

    public static enum CELL_TYPES {LAND, GRASS, WALL}
    public static enum CELL_CONDS {NORMAL, START, END}

    public static Map<CELL_TYPES, Integer> CELL_COSTS = new HashMap<CELL_TYPES, Integer>(){{ 
        put(CELL_TYPES.LAND, 1);
        put(CELL_TYPES.GRASS, 2);
        put(CELL_TYPES.WALL, Integer.MAX_VALUE);
    }};

    public static int getCostType(CELL_TYPES cell_type){
        return CELL_COSTS.get(cell_type);
    }

    public static int getNumIndex(int i, int j, int columns){
        return (i * columns + j);
    }

    public static int[] getArrIndex(int index, int columns){
        int[] arrIndex = new int[2];
        arrIndex[0] = index / columns;
        arrIndex[1] = index % columns;
        return arrIndex;
    }
}