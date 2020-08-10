package controller;

import entities.Piece;
import ui.GameBoardView;

public class BoardListener implements event.BoardListener {
    private final GameBoardView view;

    public BoardListener(GameBoardView view) {
        this.view = view;
    }

    @Override
    public void pieceMoved(Piece piece) {

    }

    @Override
    public void pieceRemoved(Piece piece) {
        for (var pieceView: view.getPieceViews()) {
            if (pieceView.getPiece() == piece) {
                pieceView.remove();
                break;
            }
        }
    }

    @Override
    public void promoteIntoQueen(Piece piece) {

    }
}
