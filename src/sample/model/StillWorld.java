package sample.model;

import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.Random;

public class StillWorld implements World{

    protected ArrayList<Robot> robots;
    protected ArrayList<Car> cars;
    protected RoadMap roadMap;
    protected ArrayList<Charger> chargers;
    protected int energy;
    protected int charge_level = 20;
    protected Random ran = new Random();

    public StillWorld(int carNum, int chargerNum,int robotNum,int energy) {

        ArrayList<Car> cars = new ArrayList<Car>();
        for (int i = 0; i<carNum;i++) {
            int x = ran.nextInt(25);
            int y = ran.nextInt(23);
            Car car = new Car(90 + 30*x,30+ 30*y,0);
            cars.add(car);
        }
        ArrayList<Charger> chargers = new ArrayList<Charger>();
        this.cars = cars;
        for (int j = 0; j<chargerNum;j++) {

            int x = ran.nextInt(25);
            int y = ran.nextInt(23);
            while (!checkPosition(90 + 30*x,30+ 30*y)) {
                x = ran.nextInt(25);
                y = ran.nextInt(23);
            }
            Charger charger = new Charger(90 + 30*x,30+ 30*y);
            chargers.add(charger);
        }
        this.chargers = chargers;
        ArrayList<Robot> robots = new ArrayList<Robot>();
        for (int j = 0; j<robotNum;j++) {
            int x = ran.nextInt(25);
            int y = ran.nextInt(23);
            Robot robot = new Robot(90 + 30*x,30+ 30*y,energy);
            robots.add(robot);
        }
        this.robots = robots;
        this.energy = energy;

    }

    @Override
    public ArrayList<Translate> play() {
        ArrayList<Translate> translates = new ArrayList<Translate>();

        for (Robot robot: robots) {
            boolean hasTranslate = false;
            int dir = ran.nextInt(4);
            int x = robot.getPositionX();
            int y = robot.getPositionY();
            if (robot.getEnergyLevel() <= 0) {
                dir = -1;
            }
            else if (robot.getEnergyLevel() > charge_level) {
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

            robot.setStep(robot.getStep()+1);
            if (!hasTranslate) {
                Translate translate = new Translate();
                translate.setX(0);
                translate.setY(0);
                translates.add(translate);
            }
            if (getInCharger(x,y,dir)) {
                robot.setEnergyLevel(energy);
            }
        }

        return translates;
    }

    public Charger findNearestCharger(Robot robot) {
        Charger charger = new Charger(0,0);
        int distance = 100000000;
        int x = robot.getPositionX();
        int y = robot.getPositionY();
        for (Charger charger1: chargers) {
            int dist = Math.abs(x - charger1.getPositionX()) + Math.abs(y - charger1.getPositionY());
            if (dist < distance) {
                distance = dist;
                charger = charger1;
            }
        }
        return charger;
    }

    public int moveToCharger(Robot robot) {
        Charger charger = findNearestCharger(robot);
        int x1 = robot.getPositionX();
        int x2 = charger.getPositionX();
        int y1 = robot.getPositionY();
        int y2 = charger.getPositionY();
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

    public boolean canMove(int dir, int x, int y) {
        switch (dir){
            case 0:
                return (y + 30 <= 720) && checkPosition(x,y+30);
            case 1:
                return (y - 30 >= 30) && checkPosition(x,y-30);
            case 2:
                return (x + 30 <= 840) && checkPosition(x+30,y);
            case 3:
                return (x - 30 >= 90) && checkPosition(x-30,y);
            default:
                return checkPosition(x,y);
        }
    }

    public int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public boolean getInCharger(int x, int y, int dir) {
        boolean inCharger = false;
        for (Charger charger: chargers) {
            switch(dir) {
                case 0:
                    if (x == charger.getPositionX() && y+30 == charger.getPositionY()) {
                        inCharger = true;
                    }
                    break;
                case 1:
                    if (x == charger.getPositionX() && y-30 == charger.getPositionY()) {
                        inCharger = true;
                    }
                    break;
                case 2:
                    if (x+30 == charger.getPositionX() && y == charger.getPositionY()) {
                        inCharger = true;
                    }
                    break;
                case 3:
                    if (x-30 == charger.getPositionX() && y == charger.getPositionY()) {
                        inCharger = true;
                    }
                    break;
            }
            if (inCharger) {
                break;
            }
        }
        return inCharger;
    }

    public boolean checkPosition(int x, int y) {
        boolean noCar = true;
        for (Car car: cars) {
            if (x == car.getPositionX() && y == car.getPositionY()) {
                noCar = false;
                break;
            }
        }
        return noCar;
    }

    public boolean checkCharger(int x, int y) {
        boolean noCharger = true;
        for (Charger charger: chargers) {
            if (x == charger.getPositionX() && y == charger.getPositionY()) {
                noCharger = false;
                break;
            }
        }
        return noCharger;
    }


    public ArrayList<Robot> getRobots() {
        return robots;
    }

    public void setRobots(ArrayList<Robot> robots) {
        this.robots = robots;
    }


    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    public RoadMap getRoadMap() {
        return roadMap;
    }

    public void setRoadMap(RoadMap roadMap) {
        this.roadMap = roadMap;
    }

    public ArrayList<Charger> getChargers() {
        return chargers;
    }

    public void setChargers(ArrayList<Charger> chargers) {
        this.chargers = chargers;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getCharge_level() {
        return charge_level;
    }

    public void setCharge_level(int charge_level) {
        this.charge_level = charge_level;
    }
}
