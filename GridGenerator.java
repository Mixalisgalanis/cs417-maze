
/**
			INTELLIGENCE LAB
	course		: 	COMP 417 - Artificial Intelligence
	authors		:	A. Vogiatzis, N. Trigkas
	excercise	:	1st Programming
	term 		: 	Spring 2019-2020
	date 		:   March 2020
*/
import javax.swing.*;
import java.awt.Canvas;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

class GridGenerator {
    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void VisualizeGrid(String frame_name, int N, int M, int[] walls, int[] grass, int start_idx, int terminal_idx) {
        JFrame frame = new JFrame(frame_name);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Drawing(N, M, walls, grass, start_idx, terminal_idx);
        canvas.setSize(M * 30, N * 30);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public static void VisualizeGrid(String frame_name, int N, int M, int[] walls, int[] grass, int[] steps, int start_idx, int terminal_idx) {
        JFrame frame = new JFrame(frame_name);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Drawing(N, M, walls, grass, steps, start_idx,terminal_idx);
        canvas.setSize(M * 30, N * 30);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Configuring World Stats
        String frame = getFrameName(args);
        Grid grid = configureWorld(args);

        //Menu System
        int action = -1;
        while (action != 0) {
            displayMainMenu();
            action = readInt("Action: ");
            Algorithms algorithms = new Algorithms(grid);
            switch (action) {
            case 0: return;
            case 1:  // BFS (Grass Cost: 2)
                algorithms.setGrassCost(2);
                int[] steps = algorithms.BFS(grid.getStartidx(), grid.getTerminalidx());
                //System.out.println(Arrays.toString(steps));
                VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), steps, grid.getStartidx(), grid.getTerminalidx());
                break;
            case 2:  // BFS (Grass Cost: 10)
                algorithms.setGrassCost(10);
                steps = algorithms.BFS(grid.getStartidx(), grid.getTerminalidx());
                //System.out.println(Arrays.toString(steps));
                VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), steps, grid.getStartidx(), grid.getTerminalidx());
                break;
            case 3:  // DFS (Grass Cost: 2)
                algorithms.setGrassCost(2);
                steps = algorithms.DFS(grid.getStartidx(), grid.getTerminalidx());
                //System.out.println(Arrays.toString(steps));
                VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), steps, grid.getStartidx(), grid.getTerminalidx());
                break;
            case 4:  // DFS (Grass Cost: 10)
                algorithms.setGrassCost(10);
                steps = algorithms.DFS(grid.getStartidx(), grid.getTerminalidx());
                //System.out.println(Arrays.toString(steps));
                VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), steps, grid.getStartidx(), grid.getTerminalidx());
                break;
            case 5:  // A* (Grass Cost: 2)
                algorithms.setGrassCost(2);
                algorithms.AStar();
                //VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), steps, grid.getStartidx(), grid.getTerminalidx());
                break;
            case 6:  // A* (Grass Cost: 10)
                algorithms.setGrassCost(10);
                algorithms.AStar();
                //VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), steps, grid.getStartidx(), grid.getTerminalidx());
                break;
            case 7:  // LRTA* (Grass Cost: 2)
                algorithms.setGrassCost(2);
                algorithms.LRTAStar();
                //VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), steps, grid.getStartidx(), grid.getTerminalidx());
                break;
            case 8:  // LRTA* (Grass Cost: 10)
                algorithms.setGrassCost(10);
                algorithms.LRTAStar();
                //VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), steps, grid.getStartidx(), grid.getTerminalidx());
                break;
            case 9:  // Visualize Original Grid
                VisualizeGrid(frame, grid.getNumOfRows(), grid.getNumOfColumns(), grid.getWalls(), grid.getGrass(), grid.getStartidx(), grid.getTerminalidx());
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
        if (args.length < 1 || args[0].equals("-d")) return "Random World";
        if (args[0].equals("-i")) return args[1].split("/")[1];
        return "default.world";
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
        System.out.println("7. LRTA* (Grass cost: 2)");
        System.out.println("8. LRTA* (Grass cost: 10)");
        System.out.println("---------------------------");
        System.out.println("9. Visualize Original World");
		System.out.println("===========================");
    }

    private static int readInt(String message) {
        System.out.print(message);
        try {
            String str = in.readLine();
            return Integer.parseInt(str);
        } catch (IOException | NumberFormatException ex) {
            return -1;
        }
    }
}