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
        super(grid, grass_cost);
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
            if (action != null && action.action_type == Util.ACTION_TYPES.STOP) 
                return;
            if (currentState.equalsToState(endState)) 
                action.action_type = Util.ACTION_TYPES.STOP;
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
                ArrayList<State> visitedStates = new ArrayList<>();
                for (Action act : actions){
                    State newState = result.get(new Key(currentState, act));
                    int cost = calcCost(currentState, act, newState, endState);
                    visitedStates.add(act.destinationState);
                    if (cost < min){
                        action.destinationState = act.destinationState;
                        action.action_type = act.action_type;
                    }
                }
            }
            state = currentState;
            currentState = action.destinationState;
            steps.add(Util.getNumIndex(currentState.i, currentState.j, columns));
            System.out.println("State: " + state.toString() + ", current_state: " + currentState.toString() + ", action: " + action.toString());
        }
        steps.removeLast();
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
        int f;
        if (newState == null) 
            f = action.destinationState.calcHeuristic(endState);
        else{
            int g = heuristic.get(newState);
            int h = state.calcHeuristic(endState);
            f = g + h;
        }
        return f;
    }


    /**
     * This class is used to represent a state position [i,j] in our grid.
     */
    class State{
        private int i, j;

        State(int[] coordinates){
            assert coordinates.length > 2;
            this.i = coordinates[0];
            this.j = coordinates[1];
        }

        boolean equalsToState(State endState){
            return (endState.i == this.i && endState.j == this.j);
        }

        int calcHeuristic(State endState){
            return Math.abs(endState.i - this.i) + Math.abs(endState.j - this.j);
        }

        @Override
        public String toString(){
        return "State(i = " + i + ", j = " + j + ")";
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
            return "Action(type = " + action_type + ", destState = " + destinationState.toString() + ")";
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

    }

    @Override
    public String toString() {
        return ": LRTA-Star (" + Util.getCostType(Util.CELL_TYPES.GRASS) + ")";
    }
}