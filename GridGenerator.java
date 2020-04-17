
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
import java.util.HashMap;

class GridGenerator {
    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void VisualizeGrid(String frame_name, int N, int M, int[] walls, int[] grass, int start_idx, int terminal_idx) {
        JFrame frame = new JFrame(frame_name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Drawing(N, M, walls, grass, start_idx, terminal_idx);
        canvas.setSize(M * 30, N * 30);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public static void VisualizeGrid(String frame_name, int N, int M, int[] walls, int[] grass, int[] steps, int start_idx, int terminal_idx) {
        JFrame frame = new JFrame(frame_name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Drawing(N, M, walls, grass, steps, start_idx, terminal_idx);
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
        while (action != 5) {
            displayMainMenu();
            action = readInt("Action: ");
            switch (action) {
            case 0: return;
            case 1:  break;
            case 2:  break;
            case 3:  break;
            case 4:  break;
            case 5:  
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
		System.out.println("1. BFS");
		System.out.println("2. DFS");
		System.out.println("3. A*");
        System.out.println("4. LRTA*");
        System.out.println("---------------------------");
        System.out.println("5. Visualize Original World");
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