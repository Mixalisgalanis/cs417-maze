package algorithms;

import java.util.LinkedList;
import grid.*;
import util.*;

/**
 * Each of the algorithms below needs a graph in order to work. Therefore, 
 * the initial process consists of the graph representation of our maze.
 */
public abstract class Algorithms {
    // Graph related variables
    int rows;
    int columns;
    int vertices;
    Graph graph;
    Grid grid;

    LinkedList<Integer> steps;

    // Constructor
    Algorithms(Grid grid, int grass_cost){
        //Initiating graph
        this.grid = grid;
        this.rows = grid.getNumOfRows();
        this.columns = grid.getNumOfColumns();
        this.vertices = rows * columns;
        this.graph = new Graph(vertices);
        this.grid.changeCostOfCellType(Util.CELL_TYPES.GRASS, grass_cost);
        this.steps = new LinkedList<>();
    }

    class Graph {
        //Essential Variables
        protected LinkedList<Integer> adjacentLists[];

        Graph(int vertices){
            //initialize variables
            this.adjacentLists = new LinkedList[vertices];
            for (int i = 0; i < adjacentLists.length; i++) adjacentLists[i] = new LinkedList<>();

            for (int i = 0; i < rows; i++){
                for (int j = 0; j < columns; j++){
                    if (i - 1 >= 0 && !grid.getCell(i - 1, j).isWall())
                        addNewEdge(Util.getNumIndex(i, j, columns), Util.getNumIndex(i - 1, j, columns)); //check top
                    if (i + 1 < rows && !grid.getCell(i + 1, j).isWall())
                        addNewEdge(Util.getNumIndex(i, j, columns), Util.getNumIndex(i + 1, j, columns)); //check bottom
                    if (j - 1 >= 0 && !grid.getCell(i, j - 1).isWall())
                        addNewEdge(Util.getNumIndex(i, j, columns), Util.getNumIndex(i, j - 1, columns)); //check left
                    if (j + 1 < columns && !grid.getCell(i, j + 1).isWall())
                        addNewEdge(Util.getNumIndex(i, j, columns), Util.getNumIndex(i, j + 1, columns)); //check right 
                    //System.out.println("i:" + i + " j:" + j + " idx: " + Util.getNumIndex(i, j, columns) + ", " + ((i - 1 >= 0 && !grid.getCell(i - 1, j).isWall()) ? "T" : "") + ((i + 1 < rows && !grid.getCell(i + 1, j).isWall()) ? "B" : "") + ((j - 1 >= 0 && !grid.getCell(i, j - 1).isWall()) ? "L" : "") + ((j + 1 < columns && !grid.getCell(i, j + 1).isWall()) ? "R" : ""));
                }
            }
        }

        public void addNewEdge(int listIndex, int edgeIndex){
            //System.out.println(adjacentLists.length + ", " + listIndex + ", " + edgeIndex);
            adjacentLists[listIndex].add(edgeIndex);
        }
        
        public LinkedList<Integer>[] getAdjacentLists(){
            return adjacentLists;
        }
    }

    abstract void calc(int start, int end);

    @Override
    public abstract String toString();
}