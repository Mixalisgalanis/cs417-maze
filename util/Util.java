package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import grid.*;

public class Util {

    public static final int FIRST_GRASS_COST_OPTION = 2;
    public static final int SECOND_GRASS_COST_OPTION = 10;

    public static enum CELL_TYPES {LAND, GRASS, WALL}
    public static enum CELL_CONDS {NORMAL, START, END}

    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static Map<CELL_TYPES, Integer> CELL_COSTS = new HashMap<CELL_TYPES, Integer>(){
        
        private static final long serialVersionUID = 1L;
        {
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

    public static int calculateAlgoCost(LinkedList<Integer> steps, Grid grid){
        int totalCost = 0;
        for (int step : steps){
            totalCost += grid.getCell(step).getCost();
        }
        return totalCost;
    }

    public static int readInt(String message) {
        System.out.print(message);
        try {
            String str = in.readLine();
            return Integer.parseInt(str);
        } catch (IOException | NumberFormatException ex) {
            return -1;
        }
    }
}