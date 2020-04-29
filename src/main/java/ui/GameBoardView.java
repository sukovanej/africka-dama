package ui;

import entities.Board;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class GameBoardView {
    private List<PaneView> paneViews;
    private List<PieceView> pieceViews;

    private Scene scene;
    private Board board;

    public GameBoardView(Scene scene, Board board) {
        this.scene = scene;
        this.board = board;

        this.pieceViews = new ArrayList<>();
        this.paneViews = new ArrayList<>();
    }

    public List<PieceView> getPieceViews() {
        return pieceViews;
    }

    public List<PaneView> getPaneViews() {
        return paneViews;
    }

    public void renderStart() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                var pane = new PaneView(scene, row, column);
                this.paneViews.add(pane);
                pane.startRender();
            }
        }

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                var possiblePiece = board.getBoard()[row][column];
                if (possiblePiece.isPresent()) {
                    var piece = new PieceView(scene, possiblePiece.get());
                    this.pieceViews.add(piece);
                    piece.renderStart();
                }
            }
        }
    }
}
