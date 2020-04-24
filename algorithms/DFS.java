package algorithms;

import java.util.LinkedList;
import java.util.Stack;
import grid.Grid;
import util.*;

public class DFS extends Algorithms {

    public DFS(Grid grid, int grass_cost, String world_name) {
        super(grid, grass_cost, world_name);
        calc(grid.getStartidx(), grid.getTerminalidx());
        grid.VisualizeGrid(world_name + toString(), steps.stream().mapToInt(i -> i).toArray());
    }

    void calc(int start, int end) {
        boolean visited[] = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();
        LinkedList<Integer> searched = new LinkedList<>();
        int[] parents = new int[vertices];
        stack.push(start);

        int v = start;
        while (!stack.empty() && v != end) {
            v = stack.pop();
            if (visited[v])
                continue;
            visited[v] = true;
            searched.add(v);
            LinkedList<Integer> adj = graph.adjacentLists[v];
            for (int i = adj.size() - 1; i >= 0; i--) {
                int u = adj.get(i);
                if (!visited[u]){
                    stack.push(u);
                    parents[u] = v;
                }
            }
        }
        traceSteps(start, end, parents);
        steps.pollFirst(); // Remove first and last element
      //  steps.pollLast();
        System.out.println(world_name + toString() + " Finished with " + steps.size() + " steps, " + Util.calculateAlgoCost(steps, grid) + " cost and " + Util.calculateAlgoCost(searched, grid) + " search cost!");
    }

    @Override
    public String toString() {
        return ": DFS (" + Util.getCostType(Util.CELL_TYPES.GRASS) + ")";
    }

    private void traceSteps(int start, int end, int[] parents){    
        while(end != start){
            steps.add(end);
            end = parents[end];
        }
    }
}