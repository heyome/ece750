package sample.model;

import java.util.ArrayList;
import java.util.Objects;

public class State {
    private int goalX;
    private int goalY;
    private ArrayList<Integer> nearGrid;
    private int dir;

    private int robotX;
    private int robotY;

    public State() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return goalX == state.goalX && goalY == state.goalY && dir == state.dir && robotX == state.robotX && robotY == state.robotY
                && Objects.equals(nearGrid, state.nearGrid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalX, goalY, nearGrid, dir, robotX, robotY);
    }

    public int getRobotX() {
        return robotX;
    }

    public void setRobotX(int robotX) {
        this.robotX = robotX;
    }

    public int getRobotY() {
        return robotY;
    }

    public void setRobotY(int robotY) {
        this.robotY = robotY;
    }




    public int getGoalX() {
        return goalX;
    }

    public void setGoalX(int goalX) {
        this.goalX = goalX;
    }

    public int getGoalY() {
        return goalY;
    }

    public void setGoalY(int goalY) {
        this.goalY = goalY;
    }

    public ArrayList<Integer> getNearGrid() {
        return nearGrid;
    }

    public void setNearGrid(ArrayList<Integer> nearGrid) {
        this.nearGrid = nearGrid;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }
}
