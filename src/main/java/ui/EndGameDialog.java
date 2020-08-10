package ui;

import entities.PieceKind;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Optional;

public class EndGameDialog {
    private final Optional<PieceKind> winner;
    private final Pane root;
    private final Pane dialog;

    public EndGameDialog(Optional<PieceKind> winner, Pane root) {
        this.winner = winner;
        this.root = root;
        this.dialog = new Pane();

        initializeView();
    }

    public void show() {
        root.getChildren().add(dialog);
    }

    public void close() {
        root.getChildren().remove(dialog);
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
            winnerText = "Remize!";
        else if (winner.get() == PieceKind.WHITE)
            winnerText = "Vyhrava bily!";
        else
            winnerText = "Vyhrava cerny!";

        var text = new Text();
        text.setText(winnerText);
        text.setFont(Font.font("Verdana", 20));
        text.setX((root.getHeight() - text.getWrappingWidth()) / 2);
        text.setY(root.getHeight() / 2);

        dialog.getChildren().add(box);
        dialog.getChildren().add(text);
    }
}
