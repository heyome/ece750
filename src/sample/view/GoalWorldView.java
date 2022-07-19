package sample.view;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import sample.model.*;

import java.util.ArrayList;

public class GoalWorldView {

    private GoalWorld goalWorld;
    private double WIDTH = 30;
    private double HEIGHT = 30;
    private boolean PLAY = false;

    public GoalWorldView(GoalWorld goalWorld) {
        this.goalWorld = goalWorld;
    }

    public Group show() {
        Group root = new Group();
        for (int i = 90;i <= 870;i += 30) {
            Line line = new Line();
            line.setStartX(i);
            line.setStartY(30);
            line.setEndX(i);
            line.setEndY(750);
            root.getChildren().add(line);
        }

        for (int j = 30;j <= 750; j += 30) {
            Line line = new Line();
            line.setStartX(90);
            line.setStartY(j);
            line.setEndX(870);
            line.setEndY(j);
            root.getChildren().add(line);
        }

        ArrayList<Car> cars = goalWorld.getCars();
        ArrayList<Charger> chargers = goalWorld.getChargers();
        for (Car car:cars) {
            Rectangle rectangle = new Rectangle(car.getPositionX(), car.getPositionY(), WIDTH, HEIGHT);
            rectangle.setFill(Color.BLACK);
            root.getChildren().add(rectangle);
        }

        for (Charger charger:chargers) {
            Rectangle rectangle = new Rectangle(charger.getPositionX(), charger.getPositionY(), WIDTH, HEIGHT);
            rectangle.setFill(Color.BLUE);
            root.getChildren().add(rectangle);
        }

        Rectangle goal = new Rectangle(goalWorld.getGoalX(), goalWorld.getGoalY(), WIDTH, HEIGHT);
        goal.setFill(Color.YELLOW);
        root.getChildren().add(goal);


        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
        ArrayList<Robot> robots = goalWorld.getRobots();
        for (Robot robot: robots) {
            Rectangle rectangle = new Rectangle(robot.getPositionX(), robot.getPositionY(), WIDTH, HEIGHT);
            rectangle.setFill(Color.RED);
            rectangles.add(rectangle);
            root.getChildren().add(rectangle);
        }

        //play button
        Button playButton = new Button("Play");
        playButton.setLayoutX(900);
        playButton.setLayoutY(50);
        root.getChildren().add(playButton);

        Button printButton = new Button("Print");
        printButton.setLayoutX(900);
        printButton.setLayoutY(120);
        root.getChildren().add(printButton);

        playButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //move
                PLAY =true;
                int j = 100;
                while (PLAY && j>0) {
                    ArrayList<Translate> translates = goalWorld.play();
                    for (int i = 0; i < rectangles.size(); i++) {
                        if (robots.get(i).isGoal()) {
                            rectangles.get(i).setFill(Color.YELLOW);
                        }
                        rectangles.get(i).getTransforms().addAll(translates.get(i));
                    }
                    j -= 1;
                }

            }
        }));

        printButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                int step = 0;
                int death = 0;
                int goal = 0;
                for (Robot robot:robots) {
                    if(robot.isGoal()) {
                        step += robot.getStep();

                    }
                    if (robot.isGoal()) {
                        goal += 1;
                    }
                    if (robot.getEnergyLevel() <= 0) {
                        death += 1;
                    }
                }
                System.out.print("Average steps:");
                System.out.println(step/goal);
                System.out.print("Death:");
                System.out.println(death);
                System.out.print("Goal:");
                System.out.println(goal);
            }
        }));

        return root;
    }
}
