package algorithms;

import java.util.LinkedList;
import java.util.Stack;
import grid.Grid;
import util.*;

public class DFS extends Algorithms {

    public DFS(Grid grid, int grass_cost, String world_name) {
        super(grid, grass_cost);
        calc(grid.getStartidx(), grid.getTerminalidx());
        grid.VisualizeGrid(world_name + toString(), steps.stream().mapToInt(i -> i).toArray());
    }

    void calc(int start, int end) {
        boolean visited[] = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        int v = start;
        while (!stack.empty() && v != end) {
            v = stack.pop();
            if (visited[v])
                continue;
            visited[v] = true;
            steps.add(v);
            LinkedList<Integer> adj = graph.adjacentLists[v];
            for (int i = adj.size() - 1; i >= 0; i--) {
                int u = adj.get(i);
                if (!visited[u])
                    stack.push(u);
            }
        }
        steps.pollFirst(); // Remove first and last element
        steps.pollLast();
        System.out.println("DFS Finished with " + steps.size() + " steps and " + Util.calculateAlgoCost(steps, grid) + " cost!");
    }

    @Override
    public String toString() {
        return ": DFS (" + Util.getCostType(Util.CELL_TYPES.GRASS) + ")";
    }
}