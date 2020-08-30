package entrypoint;

import controller.GameController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var root = new BorderPane();
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root ,700, 700);
        scene.setFill(Color.WHITE);

        primaryStage.setTitle("Dama");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        var board = new Pane();
        board.setOnMouseClicked(e -> board.requestFocus());

        var controller = new GameController(board);
        controller.start();
        scene.setOnKeyPressed(setupOnKeyPressedHandler(controller));

        var controls = new HBox();
        controls.setSpacing(10);
        controls.setPadding(new Insets(30, 0, 30, 0));

        root.setBottom(controls);
        root.setCenter(board);

        setupSaveButton(controls, controller, primaryStage);
        setupLoadButton(controls, controller, primaryStage);

        primaryStage.widthProperty().addListener((x, y, z) -> controller.state.reset());
        primaryStage.heightProperty().addListener((x, y, z) -> controller.state.reset());

        primaryStage.show();
    }

    private void setupSaveButton(HBox root, GameController controller, Stage primaryStage) {
        var button = new Button();
        button.setText("Save game");
        button.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(primaryStage);
            var serializedGameHistory = controller.getBoardHistory().serialize();

            if (file != null) {
                try {
                    var writer = new FileWriter(file);
                    writer.write(serializedGameHistory);
                    writer.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        root.getChildren().add(button);
    }

    private void setupLoadButton(HBox root, GameController controller, Stage primaryStage) {
        var button = new Button();
        button.setText("Load game");
        button.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                try {
                    var scanner = new Scanner(file);
                    controller.getBoardHistory().load(scanner);
                    controller.setPlayer(controller.getBoardHistory().getCurrentPlayer());
                    controller.recalculatePossibleMoves();
                    controller.state.reset();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        root.getChildren().add(button);
    }

    private EventHandler<KeyEvent> setupOnKeyPressedHandler(GameController controller) {
        return (KeyEvent e) -> {
            if (e.getCode() == KeyCode.RIGHT)
                controller.historyNext();
            else if (e.getCode() == KeyCode.LEFT)
                controller.historyPrevious();
        };
    }

    public static void main(String args[]){
        launch();
    }
}