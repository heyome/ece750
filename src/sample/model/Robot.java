package sample.model;

import java.util.ArrayList;

public class Robot {
    private int positionX;
    private int positionY;
    private int energyLevel;
    private ArrayList<State> states = new ArrayList<State>();
    private int step = 0;
    private boolean goal = false;

    public Robot(int positionX, int positionY,int energyLevel) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.energyLevel = energyLevel;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void addState(State state) {
        states.add(state);
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean isGoal() {
        return goal;
    }

    public void setGoal(boolean goal) {
        this.goal = goal;
    }
}
