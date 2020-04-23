package algorithms;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import grid.Grid;
import util.*;

public class AStar extends Algorithms {
    
    public AStar(Grid grid, int grass_cost, String world_name) {
        super(grid, grass_cost, world_name);
        calc(grid.getStartidx(), grid.getTerminalidx());
        grid.VisualizeGrid(world_name + toString(), steps.stream().mapToInt(i -> i).toArray());
    }

    void calc(int start, int end) {
        boolean closedList [] = new boolean[vertices];
        LinkedList<Pair> openList = new LinkedList<>();
        CellDetail cellDetails [] = new CellDetail[vertices];
        int searchCost = 0;

        // Comparator 
        final Comparator<Pair> COST_COMPARATOR = new Comparator<Pair>() {
            @Override
            public int compare(Pair p1, Pair p2) {
                return p1.cost - p2.cost; // increasing order
            }
        };

        // Initialize cellDetails
        for(int i=0; i<vertices; i++){       
            cellDetails[i] = new CellDetail(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, -1); 
        }
        
        cellDetails[start].setDetails(0, 0, 0, -1);

        openList.add(new Pair(start, cellDetails[start].f));


        while(!openList.isEmpty()){
            Collections.sort(openList, COST_COMPARATOR); // Sort open List
            Pair current = openList.pop();
            closedList[current.index] = true;
            searchCost += grid.getCell(current.index).getCost();

            if(current.index == end){
                steps = traceSteps(current.index, cellDetails, searchCost);
            }

            Iterator<Integer> i = graph.adjacentLists[current.index].listIterator(); 
            while (i.hasNext()) { 
                int next = i.next(); 
                if(closedList[next] == false){
                    int gNew = cellDetails[current.index].g + grid.getCell(next).getCost();
                    int hNew = calculateHValue(next, end);
                    int fNew = gNew + hNew;

                    if(cellDetails[next].f == Integer.MAX_VALUE || cellDetails[next].f > fNew){
                        openList.add(new Pair(next,fNew)); // Add to open list

                        // Update details of cell
                        cellDetails[next].setDetails(fNew, gNew, hNew, current.index);
                    }
                }
                
            }
        }
    }

    private class CellDetail{
        int f, g, h, parent;

        CellDetail(int f, int g, int h, int parent){
            setDetails(f, g, h, parent);
        }

        void setDetails(int f, int g, int h, int parent){
            this.f = f;
            this.g = g;
            this.h = h;
            this.parent = parent;
        }
    }

    private class Pair {
        int index;
        int cost;

        Pair(int index, int cost) {
            this.index = index;
            this.cost = cost;
        }
    }

    private int calculateHValue(int start, int end) {
        int[] indexStart = Util.getArrIndex(start, columns);
        int[] indexEnd = Util.getArrIndex(end, columns);
        return Math.abs(indexStart[0] - indexEnd[0]) + Math.abs(indexStart[1] - indexEnd[1]); // Manhatan heuristic
    }

    private LinkedList<Integer> traceSteps(int end, CellDetail[] cellDetails, int searchCost){
        LinkedList<Integer> steps = new LinkedList<>();
        
        while(end != -1){
            steps.add(end);
            end = cellDetails[end].parent;
        }
        steps.pollFirst(); // Remove first and last element
        steps.pollLast();
        System.out.println(world_name + toString() + " Finished with " + steps.size() + " steps, " + Util.calculateAlgoCost(steps, grid) + " cost and " + searchCost + " search cost!");
        return steps;
    }

    @Override
    public String toString() {
        return ": A-Star (" + Util.getCostType(Util.CELL_TYPES.GRASS) + ")";
    }
}