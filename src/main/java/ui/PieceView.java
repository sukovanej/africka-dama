package ui;

import entities.Piece;
import entities.PieceKind;
import entities.Position;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieceView {
    final private Piece piece;
    final private Circle view;
    final private Scene scene;

    public PieceView (Scene scene, Piece piece) {
        this.piece = piece;
        this.scene = scene;

        this.view = createView();
    }

    public void renderStart() {
        var group = (Group)scene.getRoot();
        group.getChildren().add(view);
    }

    public Circle getView() {
        return view;
    }

    public Piece getPiece() {
        return piece;
    }

    private void render() {
        setViewPosition(this.view, this.piece.getPosition());
    }

    private void setViewPosition(Circle pieceView, Position position) {
        var boxSize = scene.getWidth() / 9;
        pieceView.setCenterX(boxSize / 2 + boxSize * position.getColumn());
        pieceView.setCenterY(boxSize / 2 + boxSize * (8 - position.getRow()));
    }

    private Circle createView() {
        var boxSize = scene.getWidth() / 9;
        var radius = boxSize * 0.8;

        Circle pieceView = new Circle();
        pieceView.setRadius(radius / 2);
        pieceView.setFill((piece.getKind() == PieceKind.BLACK) ? Color.BLACK : Color.WHITE);
        pieceView.setStroke((piece.getKind() == PieceKind.BLACK) ? Color.WHITE : Color.BLACK);
        pieceView.setStrokeWidth(boxSize * 0.1);

        setViewPosition(pieceView, piece.getPosition());

        return pieceView;
    }

    public void highlight() {
        this.view.setStroke(Color.YELLOW);
    }
}
