/**
 * INTELLIGENCE LAB course : COMP 417 - Artificial Intelligence authors : A.
 * Vogiatzis, N. Trigkas excercise : 1st Programming term : Spring 2019-2020
 * date : March 2020
 */
class Cell {

    // Array Index format
    private int i;
    private int j;
    
    // Numerical index format
    private int idx;

    // Cell information
    private int cost;
    private Util.CELL_TYPES cell_type;
    private Util.CELL_CONDS cell_cond;

    // Constructor: Default Cell (LAND)
    Cell(int i, int j, int columns) {
        this.i = i;
        this.j = j;
        this.idx = Util.getNumIndex(i, j, columns);

        this.cell_type = Util.CELL_TYPES.LAND;
        this.cell_cond = Util.CELL_CONDS.NORMAL;
        this.cost = Util.getCostType(this.cell_type);
    }

    // Constructor with specific type of Cell
    Cell(int i, int j, int columns, Util.CELL_TYPES cell_type, Util.CELL_CONDS cell_cond) {
        this.i = i;
        this.j = j;
        this.idx = Util.getNumIndex(i, j, columns);

        this.cell_type = cell_type;
        this.cell_cond = cell_cond;
        this.cost = Util.getCostType(this.cell_type);
    }

    // Cell index functions

    public int[] getArrIndex(){
        int[] arrIndex = new int[2];
        arrIndex[0] = i;
        arrIndex[1] = j;
        return arrIndex;
    }

    public int getNumIndex(){
        return idx;
    }

    // Cell information functions

    public boolean isWall() {
        return (this.cell_type == Util.CELL_TYPES.WALL);
    }

    public boolean isGrass() {
        return (this.cell_type == Util.CELL_TYPES.GRASS);
    }

    public boolean isLand() {
        return (this.cell_type == Util.CELL_TYPES.LAND);
    }

    public int getCost() {
        return this.cost;
    }

    /**
     * WARNING: Please avoid using this function manually as this changes the cost of the individual cell, not all the cells of this type.
     *  */
    public void setCost(int cost){
        this.cost = cost;
    }

    public void changeCellType(Util.CELL_TYPES cell_type) {
        this.cell_type = cell_type;
        this.cost = Util.getCostType(this.cell_type);
    }

    public Util.CELL_TYPES getCellType() {
        return this.cell_type;
    }

    public void setCellCond(Util.CELL_CONDS cell_cond){
        this.cell_cond = cell_cond;
    }

    // Cell conditions functions

    public boolean isStart() {
        return (this.cell_cond == Util.CELL_CONDS.START);
    }

    public boolean isTerminal() {
        return (this.cell_cond == Util.CELL_CONDS.END);
    }    
}