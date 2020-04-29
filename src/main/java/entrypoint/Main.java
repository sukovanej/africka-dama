package entrypoint;

import controller.PlayerPlayerController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group mainGroup = new Group();

        Scene scene = new Scene(mainGroup ,600, 600);
        scene.setFill(Color.WHITE);

        new PlayerPlayerController(scene).start();

        primaryStage.setTitle("Dama");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String args[]){
        launch();
    }
}