package entrypoint;

import controller.GameController;
import entities.PieceKind;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var root = new BorderPane();
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 700, 700);
        scene.setFill(Color.WHITE);

        primaryStage.setTitle("Africka dama");
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

        var saveButton = createSaveButton(controller, primaryStage);
        var loadButton = createLoadButton(controller, primaryStage);
        var saveLoadButtons = new VBox();
        saveLoadButtons.setSpacing(20);
        saveLoadButtons.getChildren().addAll(saveButton, loadButton);
        controls.getChildren().add(saveLoadButtons);

        var whiteBlackSwitchButtons = switchPlayerComputerButtons(controller);
        var whiteBlackSwitchButtonsBox = new VBox();
        whiteBlackSwitchButtonsBox.setSpacing(20);
        whiteBlackSwitchButtonsBox.getChildren().addAll(whiteBlackSwitchButtons);
        controls.getChildren().add(whiteBlackSwitchButtonsBox);

        var depthBox = new VBox();
        depthBox.setSpacing(20);
        var depthSlider = createDepthSlider(controller);
        depthBox.getChildren().addAll(depthSlider);
        controls.getChildren().add(depthBox);

        var computerMovesBox = new VBox();
        var ratingLabel = createRatingLabel(controller);
        var hintButton = createComputerHintButton(controller);
        computerMovesBox.setSpacing(20);
        computerMovesBox.getChildren().addAll(ratingLabel, hintButton);
        controls.getChildren().add(computerMovesBox);

        var topBox = new HBox();
        topBox.setSpacing(20);
        var statusText = createStatusTextLabel(controller);
        var restartGameButton = createRestartGameButton(controller);
        var helpButton = createHelpButton();

        topBox.setPadding(new Insets(0, 0, 20, 0));
        topBox.getChildren().addAll(helpButton, restartGameButton, statusText);
        root.setTop(topBox);

        primaryStage.widthProperty().addListener((x, y, z) -> controller.state.resetViewCallable.run());
        primaryStage.heightProperty().addListener((x, y, z) -> controller.state.resetViewCallable.run());

        primaryStage.show();
    }

    private Node createStatusTextLabel(GameController controller) {
        var status = new Text();
        status.setText("Started");

        controller.onComputerCalculationStarted = () -> {
            status.setText("Computer move calculation started...");
        };
        controller.onComputerCalculationFinished = () -> {
            status.setText("Computer calculation finished");
        };
        controller.onEndOfGame = (Optional<PieceKind> winnerOpt) -> {
            if (winnerOpt.isEmpty()) {
                status.setText("Draw");
            } else {
                var winner = winnerOpt.get();

                if (winner == PieceKind.BLACK)
                    status.setText("Black won!");
                else
                    status.setText("White won!");
            }
        };

        return status;
    }

    private Control createSaveButton(GameController controller, Stage primaryStage) {
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
        return button;
    }

    private Control createLoadButton(GameController controller, Stage primaryStage) {
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
        return button;
    }

    private List<Control> switchPlayerComputerButtons(GameController controller) {
        var white = new Button();
        white.setStyle("-fx-background-color: #fff; -fx-text-fill: #000;");
        white.setText(controller.isWhiteComputer() ? "Computer" : "Player");

        var black = new Button();
        black.setStyle("-fx-background-color: #000; -fx-text-fill: #fff;");
        black.setText(controller.isBlackComputer() ? "Computer" : "Player");

        white.setOnMouseClicked(e -> {
            controller.switchWhite();
            white.setText(controller.isWhiteComputer() ? "Computer" : "Player");
        });
        black.setOnMouseClicked(e -> {
            controller.switchBlack();
            black.setText(controller.isBlackComputer() ? "Computer" : "Player");
        });
        return Arrays.asList(white, black);
    }

    private List<Node> createDepthSlider(GameController controller) {
        var label = new Text();
        var slider = new Slider(1, 10, controller.getDepth());
        label.setText("Depth: " + controller.getDepth());

        slider.valueProperty().addListener((obs, oldval, newVal) -> {
            slider.setValue(newVal.intValue());
            controller.setDepth(newVal.intValue());
            label.setText("Depth: " + newVal.intValue());
        });
        return Arrays.asList(slider, label);
    }

    private Node createRatingLabel(GameController controller) {
        var ratingLabel = new Text();
        ratingLabel.setText("Rating :" + controller.getCurrentRating());

        controller.onRatingChanged = () -> {
            ratingLabel.setText("Rating :" + controller.getCurrentRating());
        };

        return ratingLabel;
    }

    private Node createComputerHintButton(GameController controller) {
        var button = new Button();
        button.setText("Show best computer move");
        button.setOnMouseClicked(event -> {
            controller.showBestMove();
        });
        return button;
    }

    private EventHandler<KeyEvent> setupOnKeyPressedHandler(GameController controller) {
        return (KeyEvent e) -> {
            if (e.getCode() == KeyCode.RIGHT)
                controller.historyNext();
            else if (e.getCode() == KeyCode.LEFT)
                controller.historyPrevious();
        };
    }

    private Node createRestartGameButton(GameController controller) {
        var button = new Button();
        button.setText("New game");
        button.setOnMouseClicked(e -> {
            controller.restartGame();
        });
        return button;
    }

    private Node createHelpButton() {
        var button = new Button();
        button.setText("Help!");

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.setContentText("AFRICKA DAMA\n" +
                "\n(By Milan Suk, 2020)\n\n" +
                " - use LEFT / RIGHT ARROW for history\n" +
                " - use slider in the bottom to change depth of the computer move calculation\n" +
                " - click white / black button to change computer / player for a given color\n" +
                " - enjoy! :)");

        button.setOnMouseClicked(e -> alert.showAndWait());
        return button;
    }

    public static void main(String args[]) {
        launch();
    }
}