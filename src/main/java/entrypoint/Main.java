package entrypoint;

import controller.GameController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var root = new StackPane();
        root.setPadding(new Insets(30));
        Scene scene = new Scene(root ,700, 700);
        scene.setFill(Color.WHITE);

        primaryStage.setTitle("Dama");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        var board = new Pane();
        root.getChildren().add(board);

        var controller = new GameController(board);
        controller.start();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT)
                controller.historyNext();
            else if (e.getCode() == KeyCode.LEFT)
                controller.historyPrevious();
        });

        primaryStage.widthProperty().addListener((x, y, z) -> {
            System.out.println("Resized");
            controller.state.resetViewCallable.run();
        });
        primaryStage.heightProperty().addListener((x, y, z) -> {
            System.out.println("Resized");
            controller.state.resetViewCallable.run();
        });

        primaryStage.show();
    }

    public static void main(String args[]){
        launch();
    }
}