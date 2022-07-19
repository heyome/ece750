package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.GoalWorld;
import sample.model.ImprovedLearnWorld;
import sample.model.LearnWorld;
import sample.model.StillWorld;
import sample.view.GoalWorldView;
import sample.view.StillWorldView;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        /*

        StillWorld stillWorld = new StillWorld(30,5,1000,20);
        stillWorld.setCharge_level(10);
        StillWorldView stillWorldView = new StillWorldView(stillWorld);
        Group root = stillWorldView.show();

         */



/*
        GoalWorld goalWorld = new GoalWorld(30,5,1000,60);
        goalWorld.setCharge_level(30);
        GoalWorldView goalWorldView = new GoalWorldView(goalWorld);
        Group root = goalWorldView.show();


 */





/*
        LearnWorld learnWorld = new LearnWorld(30,5,10,40);
        learnWorld.setCharge_level(20);
        learnWorld.setSensorNum(10);
        learnWorld.storeTrainData();
        GoalWorldView goalWorldView = new GoalWorldView(learnWorld);
        Group root = goalWorldView.show();
*/


        ImprovedLearnWorld improvedLearnWorld = new ImprovedLearnWorld(30,5,1000,60);
        improvedLearnWorld.setCharge_level(30);
        improvedLearnWorld.setLr_rate(0.001);
        improvedLearnWorld.storeTrainData();
        GoalWorldView goalWorldView = new GoalWorldView(improvedLearnWorld);
        Group root = goalWorldView.show();





        primaryStage.setTitle("Embodiment Intelligence");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
