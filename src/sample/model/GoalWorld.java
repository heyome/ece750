package sample.model;

import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.Random;

public class GoalWorld extends StillWorld {

    protected Random ran = new Random();
    protected int goalX;
    protected int goalY;

    public GoalWorld(int carNum, int chargerNum, int robotNum, int energy) {
        super(carNum, chargerNum, robotNum, energy);
        int x = ran.nextInt(25);
        int y = ran.nextInt(23);
        while (!checkPosition(90 + 30*x,30+ 30*y)) {
            x = ran.nextInt(25);
            y = ran.nextInt(23);
        }
        this.goalX = 90 + 30*x;
        this.goalY = 30+ 30*y;
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
            else if (robot.getEnergyLevel() > charge_level) {
                dir = moveToGoal(robot);
                robot.setEnergyLevel(robot.getEnergyLevel()-1);
            }
            else {
                dir = moveToCharger(robot);
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

    public boolean getGoal(int x, int y, int dir) {
        boolean inGoal = false;
        switch(dir) {
            case 0:
                if (x == goalX && y+30 == goalY) {
                    inGoal = true;
                }
                break;
            case 1:
                if (x == goalX && y-30 == goalY) {
                    inGoal = true;
                }
                break;
            case 2:
                if (x+30 == goalX && y == goalY) {
                    inGoal = true;
                }
                break;
            case 3:
                if (x-30 == goalX && y == goalY) {
                    inGoal = true;
                }
                break;
        }
        return inGoal;
    }

    public boolean checkGoal(int x, int y) {
        return x == goalX && y == goalY;
    }

    public int moveToGoal(Robot robot) {
        int x1 = robot.getPositionX();
        int x2 = goalX;
        int y1 = robot.getPositionY();
        int y2 = goalY;
        ArrayList<Integer> dirs = new ArrayList<Integer>();
        ArrayList<String> dirs1 = new ArrayList<String>();
        dirs1.add("0");
        dirs1.add("1");
        dirs1.add("2");
        dirs1.add("3");
        int index = ran.nextInt(2);
        if (x1<x2) {
            dirs.add(2);
        }
        else if (x1 == x2) {
            dirs.add(Integer.valueOf(dirs1.get(index)) + 2);
            index = 1;
        }
        else {
            dirs.add(3);
        }
        if (y1<y2) {
            dirs.add(0);
        }
        else if(y1 == y2) {
            dirs.add(Integer.valueOf(dirs1.get(index)));
            index = 0;
        }
        else {
            dirs.add(1);
        }

        Integer dir = dirs.get(index);
        if (canMove(dir,x1,y1)) {
            return dir;
        }
        else {
            dirs.remove(index);
            dirs1.remove(dir.toString());
            dir = dirs.get(0);
            if (canMove(dir,x1,y1)) {
                return dir;
            }
            else {
                dirs1.remove(dir.toString());
                return Integer.valueOf(dirs1.get(index));

            }
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
}
