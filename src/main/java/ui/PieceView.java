package ui;

import entities.Piece;
import entities.PieceKind;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieceView {
    final private Piece piece;
    final private Pane root;
    final private Circle view;

    public PieceView (Pane root, Piece piece) {
        this.piece = piece;
        this.root = root;
        this.view = new Circle();

        initializeView();
    }

    public void renderStart() {
        root.getChildren().add(view);
    }

    public Circle getView() {
        return view;
    }

    public Piece getPiece() {
        return piece;
    }

    private void setViewPosition() {
        var boxSize = Math.min(root.getWidth() / 9, root.getHeight() / 9);
        view.setCenterX(boxSize / 2 + boxSize * piece.getPosition().getColumn());
        view.setCenterY(boxSize / 2 + boxSize * (8 - piece.getPosition().getRow()));
    }

    private void initializeView() {
        resetView();
        setViewPosition();
    }

    public void resetView() {
        var boxSize = Math.min(root.getWidth() / 9, root.getHeight() / 9);
        var radius = boxSize * 0.7;

        view.setRadius(radius / 2);
        view.setStrokeWidth(radius * 0.1);

        if (piece.isQueen()) {
            view.setFill((piece.getKind() == PieceKind.BLACK) ? Color.DARKGREEN : Color.LIGHTGREEN);
        } else {
            view.setFill((piece.getKind() == PieceKind.BLACK) ? Color.BLACK : Color.WHITE);
        }

        view.setStroke((piece.getKind() == PieceKind.BLACK) ? Color.WHITE : Color.BLACK);

        setViewPosition();
    }

    public void highlight() {
        view.setStroke(Color.DEEPPINK);
        view.setFill(Color.LIGHTPINK);
    }

    public void remove() {
        Platform.runLater(() -> root.getChildren().remove(view));
    }
}
