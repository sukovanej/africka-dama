package ui;

import entities.PieceKind;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

public class EndGameDialog {
    private final Optional<PieceKind> winner;
    private final Pane root;
    private final Pane dialog;
    private boolean isVisible;

    public EndGameDialog(Optional<PieceKind> winner, Pane root) {
        this.winner = winner;
        this.root = root;
        this.dialog = new Pane();
        this.isVisible = false;

        initializeView();
    }

    public void show() {
        isVisible = true;
        Platform.runLater(() -> root.getChildren().add(dialog));
    }

    public void close() {
        isVisible = false;
        root.getChildren().remove(dialog);
    }

    public boolean isVisible() {
        return isVisible;
    }

    private void initializeView() {
        var box = new Rectangle();

        box.setX((root.getWidth() - root.getWidth() / 3) / 2);
        box.setY((root.getHeight() - root.getWidth() / 3) / 2);
        box.setWidth(root.getWidth() / 3);
        box.setHeight(root.getHeight() / 3);
        box.setFill(Color.WHITE);
        box.setStroke(Color.BLACK);

        String winnerText;
        if (!winner.isPresent())
            winnerText = "Draw";
        else if (winner.get() == PieceKind.WHITE)
            winnerText = "White won!";
        else
            winnerText = "Black won!";

        var text = new Text();
        text.setText(winnerText);
        text.setFont(Font.font("Verdana", 20));
        text.setTextAlignment(TextAlignment.CENTER);
        text.resize(box.getWidth(), box.getHeight());

        dialog.getChildren().add(box);
        dialog.getChildren().add(text);
    }
}
