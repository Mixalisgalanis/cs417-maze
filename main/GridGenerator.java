package main;

/**
			INTELLIGENCE LAB
	course		: 	COMP 417 - Artificial Intelligence
	authors		:	A. Vogiatzis, N. Trigkas
	excercise	:	1st Programming
	term 		: 	Spring 2019-2020
	date 		:   March 2020
*/

import grid.*;
import algorithms.*;
import util.*;

class GridGenerator {

    public static void main(String[] args) {
        //Configuring World Stats
        Grid grid = configureWorld(args);
        String world_name = getFrameName(args);

        //Menu System
        int action = -1;
        while (action != 0) {
            displayMainMenu();
            action = Util.readInt("Action: ");
            switch (action) {
            case 0: System.exit(0);
            case 1:  // BFS (Grass Cost: 2)
                new BFS(grid, Util.FIRST_GRASS_COST_OPTION, world_name);
                break;
            case 2:  // BFS (Grass Cost: 10)
                new BFS(grid, Util.SECOND_GRASS_COST_OPTION, world_name);
                break;
            case 3:  // DFS (Grass Cost: 2)
                new DFS(grid, Util.FIRST_GRASS_COST_OPTION, world_name);
                break;
            case 4:  // DFS (Grass Cost: 10)
                new DFS(grid, Util.SECOND_GRASS_COST_OPTION, world_name);
                break;
            case 5:  // A* (Grass Cost: 2)
                new AStar(grid, Util.FIRST_GRASS_COST_OPTION, world_name);
                break; 
            case 6:  // A* (Grass Cost: 10)
                new AStar(grid, Util.SECOND_GRASS_COST_OPTION, world_name);
                break;
            case 7:  // LRTA* (Grass Cost: 10)
                new LRTAStar(grid, Util.SECOND_GRASS_COST_OPTION, world_name);
                break;
            case 8:  // Visualize Original Grid
                grid.printInfo();
                grid.VisualizeGrid(world_name);
                break;
            default: System.out.println("Action not found!"); break;
            }
        }
    }

    private static Grid configureWorld(String[] args) {
        if (args.length < 1) return new Grid();
        if (args[0].equals("-i")) return new Grid(args[1]);
        if (args[0].equals("-d")) return new Grid(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return new Grid("world_examples/default.world");
    }

    private static String getFrameName(String[] args) {
        if (args.length < 1 || args[0].equals("-d")) return "Random";
        if (args[0].equals("-i")) return (args[1].substring(args[1].indexOf("/") + 1, args[1].indexOf(".")));
        return "default";
    }

    private static void displayMainMenu(){
        System.out.println("\n==== Main Menu Options ====");
        System.out.println("0. Exit");
        System.out.println("---------------------------");
        System.out.println("1. BFS (Grass cost: 2)");
        System.out.println("2. BFS (Grass cost: 10)");
        System.out.println("3. DFS (Grass cost: 2)");
        System.out.println("4. DFS (Grass cost: 10)");
        System.out.println("5. A* (Grass cost: 2)");
        System.out.println("6. A* (Grass cost: 10)");
        System.out.println("7. LRTA* (Grass cost: 10)");
        System.out.println("---------------------------");
        System.out.println("8. Visualize Original World");
		System.out.println("===========================");
    }
}