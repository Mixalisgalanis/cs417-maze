import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Each of the algorithms below needs a graph in order to work. Therefore, 
 * the initial process consists of the graph representation of our maze.
 */
public class Algorithms {
    // Graph related variables
    private int rows;
    private int columns;
    int vertices;
    private Graph graph;
    private Grid grid;
    private static final int STANDARD_COST = 10_000;

    // Constructor

    public Algorithms(Grid grid){
        //Initiating graph
        this.grid = grid;
        rows = grid.getNumOfRows();
        columns = grid.getNumOfColumns();
        vertices = rows * columns;
        graph = new Graph(vertices);
        addNeighbours(grid);
    }

    private void addNeighbours(Grid grid){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (i - 1 >= 0 && !grid.getCell(i - 1, j).isWall()) graph.addNewEdge(Util.getNumIndex(i, j, columns), Util.getNumIndex(i - 1, j, columns)); //check top
                if (i + 1 < rows && !grid.getCell(i + 1, j).isWall()) graph.addNewEdge(Util.getNumIndex(i, j, columns), Util.getNumIndex(i + 1, j, columns)); //check bottom
                if (j - 1 >= 0 && !grid.getCell(i, j - 1).isWall()) graph.addNewEdge(Util.getNumIndex(i, j, columns), Util.getNumIndex(i, j - 1, columns)); //check left
                if (j + 1 < columns && !grid.getCell(i, j + 1).isWall()) graph.addNewEdge(Util.getNumIndex(i, j, columns), Util.getNumIndex(i, j + 1, columns)); //check right 
                //System.out.println("i:" + i + " j:" + j + " idx: " + Util.getNumIndex(i, j, columns) + ", " + ((i - 1 >= 0) ? "T" : "") + ((i + 1 < rows) ? "B" : "") + ((j - 1 >= 0) ? "L" : "") + ((j + 1 < columns) ? "R" : ""));
            }
        }
    }

    public int[] BFS(int start, int end) {
        // Mark all the vertices as not visited(By default
        // set as false)
        boolean visited[] = new boolean[vertices];
        LinkedList<Integer> steps = new LinkedList<Integer>();
        LinkedList<Integer> adjLists[] = graph.getAdjacentLists();

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark the current node as visited and enqueue it
        visited[start] = true;
        queue.add(start);

        int current_vertex;
        while (queue.size() != 0) {
            // Dequeue a vertex from queue and print it
            current_vertex = queue.poll();
            steps.add(current_vertex);
            if (current_vertex == end)
                break;
            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            Iterator<Integer> i = adjLists[current_vertex].listIterator();
            while (i.hasNext()) {
                int next = i.next();
                if (!visited[next]) {
                    visited[next] = true;
                    queue.add(next);
                }
            }
        }
        steps.pollFirst(); // Remove first and last element
        steps.pollLast();
        System.out.println("BFS Finished with " + steps.size() + " steps and " + calculateCost(steps) + " cost!");
        return steps.stream().mapToInt(i -> i).toArray();
    }

    public int[] DFS(int start, int end) {
        boolean visited[] = new boolean[vertices];
        LinkedList<Integer> steps = new LinkedList<>();
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
        System.out.println("DFS Finished with " + steps.size() + " steps and " + calculateCost(steps) + " cost!");
        return steps.stream().mapToInt(i -> i).toArray();
    }

    private int calculateHValue(int start, int end) {
        int[] indexStart = getRowCol(start);
        int[] indexEnd = getRowCol(end);
        return Math.abs(indexStart[0] - indexEnd[0]) + Math.abs(indexStart[1] - indexEnd[1]); // Manhatan heuristic
    }


    private int[] getRowCol(int index) {
        int[] idx = new int[2];
        idx[0] = index / columns;
        idx[1] = index % columns;
        return idx;
    }

    public class Pair {
        int index;
        int cost;

        public Pair(int index, int cost) {
            this.index = index;
            this.cost = cost;
        }
    }

    public class CellDetail{
        int f, g, h;
        int parent;

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

    private int[] traceSteps(int end, CellDetail[] cellDetails){
        LinkedList<Integer> steps = new LinkedList<>();
        
        while(end != -1){
            steps.add(end);
            end = cellDetails[end].parent;
        }
        steps.pollFirst(); // Remove first and last element
        steps.pollLast();
        System.out.println("A* Finished with " + steps.size() + " steps and " + calculateCost(steps) + " cost!");
        return steps.stream().mapToInt(i -> i).toArray();
    }

    public int[] AStar(int start, int end){
        boolean closedList [] = new boolean[vertices];
        LinkedList<Pair> openList = new LinkedList<>();
        LinkedList<Integer> steps = new LinkedList<>();
        CellDetail cellDetails [] = new CellDetail[vertices];

        // Comparator 
        final Comparator<Pair> COST_COMPARATOR = new Comparator<Pair>() {
            @Override
            public int compare(Pair p1, Pair p2) {
                return p1.cost - p2.cost; // increasing order
            }
        };

        // Initialize cellDetails
        for(int i=0; i<vertices; i++){       
            cellDetails[i] = new CellDetail(STANDARD_COST, STANDARD_COST, STANDARD_COST, -1); 
        }
        
        cellDetails[start].g = 0;
        cellDetails[start].h = 0;
        cellDetails[start].f = 0;
        cellDetails[start].parent = -1;

        openList.add(new Pair(start, cellDetails[start].f));

        while(!openList.isEmpty()){
            Collections.sort(openList, COST_COMPARATOR); // Sort open List
            Pair current = openList.pop();
            closedList[current.index] = true;

            if(current.index == end){
                return traceSteps(current.index, cellDetails);
            }

            Iterator<Integer> i = graph.adjacentLists[current.index].listIterator(); 
            while (i.hasNext()) { 
                int next = i.next(); 
                if(closedList[next] == false){
                    int gNew = cellDetails[current.index].g + grid.getCell(next).getCost();
                    int hNew = calculateHValue(next, end);
                    int fNew = gNew + hNew;

                    if(cellDetails[next].f == STANDARD_COST || cellDetails[next].f > fNew){
                        openList.add(new Pair(next,fNew)); // Add to open list

                        // Update details of cell
                        cellDetails[next].setDetails(fNew, gNew, hNew, current.index);
                    }
                }
                
            }
        }
        return null;
    }

    public void LRTAStar(int start, int end){
        
    }

    public void LRTAStarHelper(int[] result, int[] H, int s, int a){

    }

    public int LRTACost(int s, int a, int sAlt, int[] H){
        return 0;
    }

    private int calculateCost(LinkedList<Integer> steps){
        int totalCost = 0;
        for (int step : steps){
            totalCost += grid.getCell(step).getCost();
        }
        return totalCost;
    }
    
    class Graph {
        //Essential Variables
        protected LinkedList<Integer> adjacentLists[];

        Graph(int vertices){
            //initialize variables
            this.adjacentLists = new LinkedList[vertices];
            for (int i = 0; i < adjacentLists.length; i++) adjacentLists[i] = new LinkedList<>();
        }

        public void addNewEdge(int listIndex, int edgeIndex){
            //System.out.println(adjacentLists.length + ", " + listIndex + ", " + edgeIndex);
            adjacentLists[listIndex].add(edgeIndex);
        }
        
        public LinkedList<Integer>[] getAdjacentLists(){
            return adjacentLists;
        }
    }
}