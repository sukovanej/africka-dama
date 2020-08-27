package entrypoint;

import controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var root = new Pane();

        Scene scene = new Scene(root ,700, 700);
        scene.setFill(Color.WHITE);

        var controller = new GameController(root);
        controller.start();

        primaryStage.setTitle("Dama");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        primaryStage.widthProperty().addListener((x, y, z) -> { controller.state.resetViewCallable.run(); });
        primaryStage.heightProperty().addListener((x, y, z) -> { controller.state.resetViewCallable.run(); });

        primaryStage.show();
    }

    public static void main(String args[]){
        launch();
    }
}