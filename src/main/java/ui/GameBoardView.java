package ui;

import entities.Board;
import entities.Position;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameBoardView {
    private final PaneView[][] paneViews;
    private final List<PieceView> pieceViews;
    private final Pane root;
    private final Board board;

    public GameBoardView(Pane root, Board board) {
        this.root = root;
        this.board = board;

        this.pieceViews = new ArrayList<>();
        this.paneViews = new PaneView[9][9];
    }

    public List<PieceView> getPieceViews() {
        return pieceViews;
    }

    public PaneView getPaneView(int row, int column) {
        return paneViews[row][column];
    }

    public void renderStart() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                var pane = new PaneView(root, new Position(row, column));
                paneViews[row][column] = pane;
                pane.startRender();
            }
        }

        for (var piece : board.getPieces()) {
            var pieceView = new PieceView(root, piece);
            pieceViews.add(pieceView);
            pieceView.renderStart();
        }
    }

    public void resetView() {
        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++) {
                paneViews[row][column].resetView();
            }

        for (var pieceView : pieceViews) {
            var piece = pieceView.getPiece();
            var discardedPieces = board.getDiscardedPieces();
            var pieces = board.getPieces();

            if (pieces.contains(piece) && !pieceView.isVisible())
                pieceView.add();
            else if (discardedPieces.contains(piece) && pieceView.isVisible())
                pieceView.remove();
            else
                pieceView.resetView();
        }
    }

    public Pane getRoot() {
        return root;
    }
}
