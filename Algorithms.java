import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Each of the algorithms below needs a graph in order to work. Therefore, 
 * the initial process consists of the graph representation of our maze.
 * 
 */
public class Algorithms {
    //Graph related variables
    private int rows;
    private int columns;
    int vertices;
    private Graph graph;
    private Grid grid;

    public Algorithms(Grid grid){
        //Initiating graph
        rows = grid.getNumOfRows();
        columns = grid.getNumOfColumns();
        vertices = rows * columns;
        graph = new Graph(vertices);
        addNeighbours(grid);
    }

    private int getNumIdx(int r, int c){
        return r * columns + c;
    }

    private void addNeighbours(Grid grid){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (i - 1 >= 0 && !grid.getCell(i - 1, j).isWall()) graph.addNewEdge(getNumIdx(i, j), getNumIdx(i - 1, j)); //check top
                if (i + 1 < rows && !grid.getCell(i + 1, j).isWall()) graph.addNewEdge(getNumIdx(i, j), getNumIdx(i + 1, j)); //check bottom
                if (j - 1 >= 0 && !grid.getCell(i, j - 1).isWall()) graph.addNewEdge(getNumIdx(i, j), getNumIdx(i, j - 1)); //check left
                if (j + 1 < columns && !grid.getCell(i, j + 1).isWall()) graph.addNewEdge(getNumIdx(i, j), getNumIdx(i, j + 1)); //check right 
                //System.out.println("i:" + i + " j:" + j + " idx: " + getNumIdx(i, j) + ", " + ((i - 1 >= 0) ? "T" : "") + ((i + 1 < rows) ? "B" : "") + ((j - 1 >= 0) ? "L" : "") + ((j + 1 < columns) ? "R" : ""));
            }
        }
    }

    public int[] BFS(int start,int end) {
        // Mark all the vertices as not visited(By default 
        // set as false) 
        boolean visited[] = new boolean[vertices]; 
        LinkedList<Integer> steps = new LinkedList<Integer>();
        LinkedList<Integer> adjLists[] = graph.getAdjacentLists();
  
        // Create a queue for BFS 
        LinkedList<Integer> queue = new LinkedList<Integer>(); 
  
        // Mark the current node as visited and enqueue it 
        visited[start]=true; 
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
        System.out.println("BFS Finished with " + steps.size() + " steps and " + calculateCost(steps) + " cost!");
        return steps.stream().mapToInt(i->i).toArray();
    }

    public int[] DFS(int start, int end){
        boolean visited[] = new boolean[vertices];
        LinkedList<Integer> steps = new LinkedList<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        int v = start;
        while (!stack.empty() && v != end){
            v = stack.pop();
            if (visited[v]) continue;
            visited[v] = true;
            steps.add(v);
            LinkedList<Integer> adj = graph.adjacentLists[v];
            for (int i = adj.size() - 1; i >= 0; i--){
                int u = adj.get(i);
                if (!visited[u]) stack.push(u);
            }
        }
        System.out.println("DFS Finished with " + steps.size() + " steps and " + calculateCost(steps) + " cost!");
        return steps.stream().mapToInt(i->i).toArray();
    }

    public void AStar(){

    }

    public void LRTAStar(){
        
    }

    public void setGrassCost(int cost){
        for (int grassIdx : grid.getGrass())
            grid.getCell(grassIdx).setCost(cost);
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
        private final int vertices;
        protected LinkedList<Integer> adjacentLists[];

        Graph(int vertices, Grid grid){
            //initialize variables
            this.vertices = vertices;
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