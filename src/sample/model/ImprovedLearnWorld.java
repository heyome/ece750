package sample.model;

import javafx.scene.transform.Translate;
import java.util.ArrayList;

public class ImprovedLearnWorld extends LearnWorld {


    public ImprovedLearnWorld(int carNum, int chargerNum, int robotNum, int energy) {
        super(carNum, chargerNum, robotNum, energy);
    }

    public void trainMove() {

        for (Robot robot: trainRobots) {
            int dir;
            int x = robot.getPositionX();
            int y = robot.getPositionY();
            if (robot.getEnergyLevel() <= 0 || robot.isGoal()) {
                dir = -1;
            }
            else if (robot.getEnergyLevel() > charge_level) {
                dir = moveToGoal(robot);
                robot.setEnergyLevel(robot.getEnergyLevel()-1);
                robot.setStep(robot.getStep()+1);
            }
            else {
                dir = moveToCharger(robot);
                robot.setEnergyLevel(robot.getEnergyLevel()-1);
                robot.setStep(robot.getStep()+1);
            }


            /* 0 = down
                1 = up
                2 = right
                3 = left
                 */
            if (dir == 0) {
                if ((y + 30 <= 720) && checkPosition(x,y+30)) {
                    robot.setPositionY(y+30);
                }
            }
            else if(dir == 1) {
                if ((y - 30 >= 30) && checkPosition(x,y-30)) {
                    robot.setPositionY(y-30);
                }
            }
            else if(dir == 2) {
                if ((x + 30 <= 840) && checkPosition(x+30,y)) {
                    robot.setPositionX(x+30);
                }
            }
            else if(dir >= 3) {
                if ((x - 30 >= 90) && checkPosition(x-30,y)) {
                    robot.setPositionX(x-30);
                }
            }

            if (getInCharger(x,y,dir)) {
                robot.setEnergyLevel(energy);
            }
            if (!robot.isGoal()) {
                State state = new State();
                state.setGoalX(goalX);
                state.setGoalY(goalY);
                state.setDir(dir);
                state.setRobotX(x);
                state.setRobotY(y);
                robot.addState(state);
            }
            if (getGoal(x,y,dir)) {
                robot.setGoal(true);
            }
        }

    }


    @Override
    public ArrayList<Translate> play() {
        ArrayList<Translate> translates = new ArrayList<Translate>();

        for (Robot robot: robots) {
            boolean hasTranslate = false;
            int dir = ran.nextInt(4);
            int x = robot.getPositionX();
            int y = robot.getPositionY();
            if (robot.getEnergyLevel() <= 0 || robot.isGoal()) {
                dir = -1;
            }
            else {
                dir = getDirByPosState(robot);
                robot.setEnergyLevel(robot.getEnergyLevel()-1);
            }


            /* 0 = down
                1 = up
                2 = right
                3 = left
                 */
            if (dir == 0) {
                if ((y + 30 <= 720) && checkPosition(x,y+30)) {
                    Translate translate = new Translate();
                    translate.setX(0);
                    translate.setY(30);
                    translates.add(translate);
                    robot.setPositionY(y+30);
                    hasTranslate = true;
                }
            }
            else if(dir == 1) {
                if ((y - 30 >= 30) && checkPosition(x,y-30)) {
                    Translate translate = new Translate();
                    translate.setX(0);
                    translate.setY(-30);
                    translates.add(translate);
                    robot.setPositionY(y-30);
                    hasTranslate = true;
                }
            }
            else if(dir == 2) {
                if ((x + 30 <= 840) && checkPosition(x+30,y)) {
                    Translate translate = new Translate();
                    translate.setX(30);
                    translate.setY(0);
                    translates.add(translate);
                    robot.setPositionX(x+30);
                    hasTranslate = true;
                }
            }
            else if(dir >= 3) {
                if ((x - 30 >= 90) && checkPosition(x-30,y)) {
                    Translate translate = new Translate();
                    translate.setX(-30);
                    translate.setY(0);
                    translates.add(translate);
                    robot.setPositionX(x-30);
                    hasTranslate = true;
                }
            }



            if (!hasTranslate) {
                Translate translate = new Translate();
                translate.setX(0);
                translate.setY(0);
                translates.add(translate);
            }
            if (getInCharger(x,y,dir)) {
                robot.setEnergyLevel(energy);
            }
            if(!robot.isGoal()) {
                robot.setStep(robot.getStep()+1);
            }
            if (getGoal(x,y,dir)) {
                robot.setGoal(true);
            }
        }

        return translates;
    }



    public int getDirByPosState(Robot robot) {
        int x = robot.getPositionX();
        int y = robot.getPositionY();
        double maxValue = -10000;
        int dir = -1;
        for (State state: values.keySet()) {
            if (x == state.getRobotX() && y == state.getRobotY()) {
                double value = values.get(state);
                if (value>maxValue) {
                    dir = state.getDir();
                    maxValue = value;
                }
            }
        }
        return dir;
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
}
