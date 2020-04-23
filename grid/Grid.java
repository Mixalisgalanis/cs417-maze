package grid;

/**
			INTELLIGENCE LAB
	course		: 	COMP 417 - Artificial Intelligence
	authors		:	A. Vogiatzis, N. Trigkas
	excercise	:	1st Programming
	term 		: 	Spring 2019-2020
	date 		:   March 2020
*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import java.awt.Canvas;

import java.io.*;
import util.*;
import util.Util.CELL_TYPES;
import swing.*;

public class Grid {
    private int N, M;
    private Cell[][] cells;

    private int[] walls;
    private int[] grass;

    private int start_idx;
    private int terminal_idx;

    // Constructors

    public Grid() {
        this.N = 13;
        this.M = 9;
        this.start_idx = 96;
        this.terminal_idx = 42;
        this.cells = new Cell[this.N][this.M];
        this.init();
        this.storeWorld();
    }

    public Grid(int N, int M) {
        this.N = N;
        this.M = M;
        this.cells = new Cell[this.N][this.M];
        this.init();
        this.storeWorld();
    }

    public Grid(String filename) {
        this.loadWold(filename);

        // Fill everything with LAND
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                this.cells[i][j] = new Cell(i, j, this.M, Util.CELL_TYPES.LAND, ((Util.getNumIndex(i, j, M) == this.start_idx) ? Util.CELL_CONDS.START : (Util.getNumIndex(i, j, M) == this.terminal_idx) ?  Util.CELL_CONDS.END : Util.CELL_CONDS.NORMAL));
            }
        }

        // Replace some LAND cells with WALLS
        for (int w = 0; w < this.walls.length; w++) {
            int[] arrInxed = Util.getArrIndex(walls[w], M);
            int i = arrInxed[0];
            int j = arrInxed[1];
            this.cells[i][j].changeCellType(Util.CELL_TYPES.WALL);
        }

        // Replace some LAND cells with GRASS
        for (int g = 0; g < this.grass.length; g++) {
            int[] arrInxed = Util.getArrIndex(grass[g], M);
            int i = arrInxed[0];
            int j = arrInxed[1];
            this.cells[i][j].changeCellType(Util.CELL_TYPES.GRASS);
        }
    }

    // Other Functions
    public Cell[][] getCells(){
        return cells;
    }

    public Cell getCell(int i, int j) {
        return this.cells[i][j];
    }

    public Cell getCell(int numIdx){
        int[] idx = Util.getArrIndex(numIdx, M);
        return this.cells[idx[0]][idx[1]];
    }

    public Cell getCell(int[] coordinates){
        return this.cells[coordinates[0]][coordinates[1]];
    }

    public Cell getStartCell(){
        return getCell(this.start_idx);
    }

    public Cell getTerminalCell(){
        return getCell(this.terminal_idx);
    }

    public int getTerminalidx() {
        return this.terminal_idx;
    }

    public int getStartidx() {
        return this.start_idx;
    }

    public int getNumOfRows() {
        return this.N;
    }

    public int getNumOfColumns() {
        return this.M;
    }

    public Cell[] getCellsOfType(Util.CELL_TYPES cell_type){
        ArrayList<Cell> specificCells = new ArrayList<>();
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                if (cells[i][j].getCellType() == cell_type) specificCells.add(cells[i][j]);
            }
        }
        return specificCells.toArray(new Cell[specificCells.size()]);
    }

    public int[] getIndexCellsOfType(Util.CELL_TYPES cell_type){
        Cell[] cells = getCellsOfType(cell_type);
        int[] indexCells = new int[cells.length];
        for (int i = 0; i < cells.length; i++) indexCells[i] = cells[i].getNumIndex();
        return indexCells;
    }

    public int getNumOfCells(){
        return getNumOfRows() * getNumOfColumns();
    }

    public void changeCostOfCellType(Util.CELL_TYPES cell_type, int cost){
        setCostOfCellType(cell_type, cost);
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                if (cells[i][j].getCellType() == cell_type) cells[i][j].setCost(cost);
            }
        }
    }

    private void setCostOfCellType(Util.CELL_TYPES cell_type, int cost){
        Util.CELL_COSTS.replace(cell_type, cost);
    }

    private void storeWorld() {
        try {
            File f = new File("newRandomLevel.world");
            FileOutputStream stream = new FileOutputStream(f);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));

            bw.write("dimensions:" + this.N + "x" + this.M);
            bw.newLine();

            String st2w = "";
            st2w += "walls:" + this.walls.length + ":";
            for (int w = 0; w < this.walls.length; w++) {
                st2w += Integer.toString(this.walls[w]) + ",";
            }
            st2w = st2w.substring(0, st2w.length() - 1);
            bw.write(st2w);
            bw.newLine();

            st2w = "grass:" + this.grass.length + ":";
            for (int g = 0; g < this.grass.length; g++) {
                st2w += Integer.toString(this.grass[g]) + ",";
            }
            st2w = st2w.substring(0, st2w.length() - 1);
            bw.write(st2w);
            bw.newLine();

            bw.write("start_idx:" + this.start_idx);
            bw.newLine();
            bw.write("terminal_idx:" + this.terminal_idx);
            bw.newLine();
            bw.write("grass_cost:" + Util.getCostType(Util.CELL_TYPES.GRASS));
            bw.close();

        } catch (IOException e) {
            System.out.println("error on writing");
        }
    }

    private void loadWold(String filename) {
        try {
            File f = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String param;

            param = br.readLine();
            while (param != null) {
                if (param.split(":")[0].equalsIgnoreCase("dimensions")) {
                    // read board dimensions
                    param = param.split(":")[1];
                    this.N = Integer.parseInt(param.split("x")[0]);
                    this.M = Integer.parseInt(param.split("x")[1]);
                } else if (param.split(":")[0].equalsIgnoreCase("walls")) {
                    // read wall-cells
                    this.walls = new int[Integer.parseInt(param.split(":")[1])];
                    param = param.split(":")[2];
                    for (int w = 0; w < this.walls.length; w++)
                        this.walls[w] = Integer.parseInt(param.split(",")[w]);

                } else if (param.split(":")[0].equalsIgnoreCase("grass")) {
                    // read grass-cells
                    this.grass = new int[Integer.parseInt(param.split(":")[1])];
                    param = param.split(":")[2];
                    for (int g = 0; g < this.grass.length; g++)
                        this.grass[g] = Integer.parseInt(param.split(",")[g]);
                } else if (param.split(":")[0].equalsIgnoreCase("start_idx")) {
                    // read start point
                    this.start_idx = Integer.parseInt(param.split(":")[1]);
                } else if (param.split(":")[0].equalsIgnoreCase("terminal_idx")) {
                    // read terminal point
                    this.terminal_idx = Integer.parseInt(param.split(":")[1]);
                } else if (param.split(":")[0].equalsIgnoreCase("grass_cost")) {
                    // read cost for grass
                    setCostOfCellType(Util.CELL_TYPES.GRASS, Integer.parseInt(param.split(":")[1]));
                }
                param = br.readLine();
            }
            br.close();
            this.cells = new Cell[this.N][this.M];
        } catch (IOException e) {
        }
    }

    private void init() {
        Random random = new Random();

        this.start_idx = random.nextInt(this.N * this.M - 1);
        int tmp = random.nextInt(this.N * this.M - 1);
        while (Math.abs(this.start_idx - tmp) < 45)
            tmp = random.nextInt(this.N * this.M - 1);

        this.terminal_idx = tmp;

        this.walls = new int[random.nextInt(4) + 14];
        int start_row = this.start_idx / this.M;
        int terminal_row = this.terminal_idx / this.M;
        int middle_row = (int) (Math.abs(start_row - terminal_row) + 1) / 2 + Math.min(start_row, terminal_row);
        int middle_column = random.nextInt(5);
        for (int w = 0; w < 4; w++)
            walls[w] = middle_row * M + middle_column + w;

        for (int w = 4; w < this.walls.length; w++) {
            tmp = random.nextInt(this.N * this.M - 1);
            while (tmp == this.start_idx || tmp == this.terminal_idx)
                tmp = random.nextInt(this.N * this.M - 1);
            this.walls[w] = tmp;
        }
        this.grass = new int[random.nextInt(5) + 37];

        for (int g = 0; g < this.grass.length; g++) {
            tmp = random.nextInt(this.N * this.M - 1);
            while (tmp == this.start_idx || tmp == this.terminal_idx)
                tmp = random.nextInt(this.N * this.M - 1);
            this.grass[g] = tmp;
        }

        // Generate Land Cells
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                this.cells[i][j] = new Cell(i, j, this.M, Util.CELL_TYPES.LAND, ((Util.getNumIndex(i, j, M) == this.start_idx) ? Util.CELL_CONDS.START : (Util.getNumIndex(i, j, M) == this.terminal_idx) ?  Util.CELL_CONDS.END : Util.CELL_CONDS.NORMAL));
            }
        }

        // Replace some Land Cells with Wall Cells
        for (int w = 0; w < this.walls.length; w++) {
            int[] arrInxed = Util.getArrIndex(walls[w], M);
            int i = arrInxed[0];
            int j = arrInxed[1];
            this.cells[i][j].changeCellType(Util.CELL_TYPES.WALL);
        }

        int count_g = 0;
        int[] tmp_g = new int[this.grass.length];

        // Replace some Land Cells with Grass Cells
        for (int g = 0; g < this.grass.length; g++) {
            int[] arrInxed = Util.getArrIndex(grass[g], M);
            int i = arrInxed[0];
            int j = arrInxed[1];
            if (!this.cells[i][j].isWall()) {
                this.cells[i][j].changeCellType(Util.CELL_TYPES.GRASS);
                tmp_g[count_g] = this.grass[g];
                count_g += 1;
            }
        }
        if (count_g < this.grass.length) {
            this.grass = new int[count_g - 1];
            for (int g = 0; g < this.grass.length; g++)
                this.grass[g] = tmp_g[g];
        }
    }

    public void VisualizeGrid(String frame_name) {
        JFrame frame = new JFrame(frame_name);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Drawing(N, M, walls, grass, start_idx, terminal_idx);
        canvas.setSize(M * 30, N * 30);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public void VisualizeGrid(String frame_name, int[] steps) {
        JFrame frame = new JFrame(frame_name);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Drawing(N, M, walls, grass, steps, start_idx,terminal_idx);
        canvas.setSize(M * 30, N * 30);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public void printInfo(){
        System.out.println("\n------------- GRID INFO -------------");
        System.out.println("Rows: " + N + " Columns: " + M + " Cells: " + N * M);
        System.out.println("Land Cells: " + getCellsOfType(CELL_TYPES.LAND).length + " -> " + Arrays.toString(getIndexCellsOfType(CELL_TYPES.LAND)));
        System.out.println("Grass Cells: " + getCellsOfType(CELL_TYPES.GRASS).length + " -> " + Arrays.toString(getIndexCellsOfType(CELL_TYPES.GRASS)));
        System.out.println("Wall Cells: " + getCellsOfType(CELL_TYPES.WALL).length + " -> " + Arrays.toString(getIndexCellsOfType(CELL_TYPES.WALL)));
        System.out.println("Do they add up: " + (getCellsOfType(CELL_TYPES.LAND).length + getCellsOfType(CELL_TYPES.GRASS).length + getCellsOfType(CELL_TYPES.WALL).length == N * M));
        System.out.println("-------------------------------------");
    }
}
