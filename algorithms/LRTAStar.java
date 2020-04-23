package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import grid.Grid;
import util.*;

public class LRTAStar extends Algorithms {

    HashMap<Key, State> result = new HashMap<>();
    HashMap<State, Integer> heuristic = new HashMap<>();

    public LRTAStar(Grid grid, int grass_cost, String world_name) {
        super(grid, grass_cost, world_name);
        calc(grid.getStartidx(), grid.getTerminalidx());
        grid.VisualizeGrid(world_name + toString(), steps.stream().mapToInt(i -> i).toArray());
    }

    void calc(int start, int end) {
        //Initializations
        State startState = new State(Util.getArrIndex(start, columns));
        State endState = new State(Util.getArrIndex(end, columns));
        State state = null;
        State currentState  = startState;
        Action action = null;
        steps = new LinkedList<>();

        while(currentState != null){
            if (currentState.equalsToState(endState)) break;
            else {
                if (!heuristic.containsKey(currentState)) heuristic.put(currentState, currentState.calcHeuristic(endState));
                if (state != null){
                    Key key = new Key(state, action);
                    result.put(key, currentState);
                    int min = Integer.MAX_VALUE;
                    ArrayList<Action> actions = generateActions(state);
                    for (Action act : actions){
                        State newState = result.get(new Key(state, act));
                        int cost = calcCost(state, act, newState, endState);
                        min = Math.min(cost, min);
                    }
                    heuristic.put(state, min);
                }

                int min = Integer.MAX_VALUE;
                action = new Action(Util.ACTION_TYPES.STOP, null);
                ArrayList<Action> actions = generateActions(currentState);
                ArrayList<Action> visited = new ArrayList<>();
                //HashMap<Action, Integer> actionWithCosts = new HashMap<>();
                for (Action act : actions){
                    Key tempKey = new Key(currentState, act);
                    if(result.containsKey(tempKey) && visited.contains(act))
                        continue;
                    visited.add(act);
                    State newState = result.get(new Key(currentState, act));
                    int cost = calcCost(currentState, act, newState, endState);
                    //actionWithCosts.put(act, cost);
                    if (cost < min){
                        min = cost;
                        action.destinationState = act.destinationState;
                        action.action_type = act.action_type;
                    }
                }
                //actionWithCosts.forEach((key, value) -> System.out.print("[" + key.toString() + ", " + value + "]  "));
            }
            state = currentState;
            currentState = action.destinationState;
            steps.add(Util.getNumIndex(currentState.i, currentState.j, columns));
            //System.out.println("Action chosen: " + action.toString() + ": prev=" + state.toString() + " -> curr=" + currentState.toString());
        }
        steps.removeLast();
        System.out.println(world_name + toString() + " Finished with " + steps.size() + " steps, " + Util.calculateAlgoCost(steps, grid) + " cost and " + Util.calculateAlgoCost(steps, grid) + " search cost!");
    }

    private ArrayList<Action> generateActions(State state){
        LinkedList<Integer> tempAdjLists = graph.adjacentLists[Util.getNumIndex(state.i, state.j, columns)];
        ArrayList<Action> actions = new ArrayList<>();
        for (int i=0; i < tempAdjLists.size(); i++){
            int[] arrIndex = Util.getArrIndex(tempAdjLists.get(i), columns);
            actions.add(new Action(Util.ACTION_TYPES.MOVE, new State(arrIndex)));
        }
        return actions;
    }

    private int calcCost(State state, Action action, State newState, State endState){
        if (!heuristic.containsKey(newState)) return action.destinationState.calcHeuristic(endState);
        else return grid.getCell(newState.getCoordinates()).getCost() + heuristic.get(newState);
    }

    /**
     * This class is used to represent a state position [i,j] in our grid.
     */
    class State{
        private int i = 0, j = 0;

        State(int[] coordinates){
            assert coordinates.length > 2;
            this.i = coordinates[0];
            this.j = coordinates[1];
        }

        int[] getCoordinates(){
            int[] coordinates = new int[2];
            coordinates[0] = this.i;
            coordinates[1] = this.j;
            return  coordinates;
        }

        boolean equalsToState(State endState){
            return (endState.i == this.i && endState.j == this.j);
        }

        int calcHeuristic(State endState){
            return Math.abs(endState.i - this.i) + Math.abs(endState.j - this.j);
        }

        @Override
        public String toString(){
        return "[" + i + "," + j + "]";
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = 1;
            result = prime * result + i;
            return prime * result + j;
        }

        @Override
        public boolean equals(Object object){
            return (object == this || ((State) object).i == this.i && ((State) object).j == this.j);
        }
    }

    class Action{
        public Util.ACTION_TYPES action_type;
        public State destinationState;

        Action(Util.ACTION_TYPES action_type, State destinationState){
            this.action_type = action_type;
            this.destinationState = destinationState;
        }

        @Override
        public String toString(){
            return "[" + action_type + ", " + destinationState.toString() + "]";
        }


        @Override
        public int hashCode(){
            final int prime = 31;
            int result = 1;
            result = prime * result + (action_type == null ? 0 : action_type.hashCode());
            return prime * result + (destinationState == null ? 0 : destinationState.hashCode());
        }

        @Override 
        public boolean equals(Object object){
            return (object == this || (((Action) object).action_type.equals(this.action_type) && ((Action) object).destinationState.equals(this.destinationState)));
        }
    }

    class Key{
        private State state;
        private Action action;

        Key(State state, Action action){
            this.state = state;
            this.action = action;
        }

        @Override
        public String toString(){
            return "Key(state = " + state.toString() + ", action = " + action.toString() + ")";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (state == null ? 0 : state.hashCode());
            return prime * result + (action == null ? 0 : state.hashCode());
        }

        @Override
        public boolean equals(Object object){
            return (object == this || (((Key) object).state.equals(this.state) && ((Key) object).action.equals(this.action)));
        }
    }

    @Override
    public String toString() {
        return ": LRTA-Star (" + Util.getCostType(Util.CELL_TYPES.GRASS) + ")";
    }
}