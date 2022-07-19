package sample.model;

import javafx.scene.transform.Translate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LearnWorld extends GoalWorld {

    protected HashMap<State,Double> values = new HashMap<State,Double>();
    protected double lr_rate = 0.0002;
    protected ArrayList<Robot> trainRobots = new ArrayList<Robot>();
    protected int sensorNum = 1;



    public LearnWorld(int carNum, int chargerNum, int robotNum, int energy) {
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
                ArrayList sensor = getSensor(robot,sensorNum);
                state.setNearGrid(sensor);
                robot.addState(state);
            }
            if (getGoal(x,y,dir)) {
                robot.setGoal(true);
            }
        }
    }

    public void storeTrainData() {
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 100; i++) {
                ArrayList<Robot> trainRobots1 = new ArrayList<Robot>();
                for (int k = 0; k<1000;k++) {
                    int robotX = ran.nextInt(25);
                    int robotY = ran.nextInt(23);
                    Robot robot = new Robot(90 + 30*robotX,30+ 30*robotY,energy);
                    trainRobots1.add(robot);
                }
                this.trainRobots = trainRobots1;
                trainMove();
            }
            for (Robot robot:trainRobots) {
                double reward = 0;
                if (robot.isGoal()) {

                    reward +=  lr_rate;

                }
                else {

                    reward -= lr_rate;

                }
                ArrayList<State> states = robot.getStates();
                for (State state:states) {
                    if (states.size() - states.indexOf(state) < 5){
                        reward = reward * 5;
                    }
                    else if (states.size() - states.indexOf(state) > 10){
                        reward = reward / 2;
                    }
                    if (values.containsKey(state)) {
                        values.put(state,reward +values.get(state));
                    }
                    else {
                        values.put(state,reward);
                    }
                }
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("track.txt"));
            writer.write(String.valueOf(values.size()));
            for (State state:values.keySet()) {
                writer.write(String.valueOf(state.getRobotX()));
                writer.write("/");
                writer.write(String.valueOf(state.getRobotY()));
                writer.write("/");
                writer.write(String.valueOf(state.getDir()));
                writer.write("/");
                writer.write(String.valueOf(values.get(state)));
                //writer.write("/");
                //writer.write(Arrays.toString(state.getNearGrid().toArray()));
                writer.newLine();
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
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
                dir = getDirBySensor(robot);
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

    //nearby sensor*2+1 * sensor*2+1
    public ArrayList<Integer> getSensor(Robot robot,int sensor) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        int x = robot.getPositionX();
        int y = robot.getPositionY();
        for (int i = 0; i < sensor; i++) {
            for (int j = 0; j < sensor; j++) {
                if (i ==0 && j==0) {
                    result.add(getPositionValue(x,y));
                }
                else {
                    result.add(getPositionValue(x-30*i,y+30*j));
                    result.add(getPositionValue(x+30*i,y-30*j));
                }
            }
        }
        /*
        result.add(getPositionValue(x-30,y+30));
        result.add(getPositionValue(x,y+30));
        result.add(getPositionValue(x+30,y+30));
        result.add(getPositionValue(x-30,y));
        result.add(getPositionValue(x,y));
        result.add(getPositionValue(x+30,y));
        result.add(getPositionValue(x-30,y-30));
        result.add(getPositionValue(x,y-30));
        result.add(getPositionValue(x+30,y-30));

         */
        return result;
    }

    public int getDirBySensor(Robot robot) {
        ArrayList<Integer> sensor = getSensor(robot,sensorNum);
        double maxValue = -10000;
        int dir = -1;
        for (State state: values.keySet()) {
            if (sensor.equals(state.getNearGrid())) {
                double value = values.get(state);
                if (value>maxValue) {
                    dir = state.getDir();
                    maxValue = value;
                }
            }
        }
        return dir;
    }

    public Integer getPositionValue(int x, int y) {
        if (x >= 870 || y>=750 || x<90 || y<30){
            return 1;
        }
        else if (!checkPosition(x,y)) {
            return 2;
        }
        else if (!checkCharger(x,y)) {
            return 3;
        }
        else if (checkGoal(x,y)) {
            return 4;
        }
        else {
            return 0;
        }
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

    public int getSensorNum() {
        return sensorNum;
    }

    public void setSensorNum(int sensorNum) {
        this.sensorNum = sensorNum;
    }

    public double getLr_rate() {
        return lr_rate;
    }

    public void setLr_rate(double lr_rate) {
        this.lr_rate = lr_rate;
    }

}
