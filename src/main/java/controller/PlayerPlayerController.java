package controller;

import entities.Board;
import entities.Piece;
import factories.BoardFactory;
import javafx.scene.Scene;
import logic.PossibleMovesLogic;
import ui.GameBoardView;

public class PlayerPlayerController {
    final private Board board;
    final private PossibleMovesLogic logic;
    final private GameBoardView view;
    final private ViewState state;

    public PlayerPlayerController(Scene scene) {
        board = BoardFactory.initializeBoard();
        logic = new PossibleMovesLogic(board);
        view = new GameBoardView(scene, board);

        state = new ViewState();
        state.startMoveRunnable = this::startMove;
    }

    public void start() {
        view.renderStart();

        for (var pieceView: view.getPieceViews()) {
            pieceView.getView().setOnMouseClicked(new PieceClickHandler(state, pieceView));
        }
    }

    private void startMove(Piece piece) {
        var possibleMoves = logic.getPossibleMovesForPiece(piece);

        for (var possibleMove: possibleMoves) {
            var pieceMove = possibleMove.getMoves().stream().filter((move) -> move.getPiece() == piece).findFirst();
            var position = pieceMove.get().getTo().get();
            
        }
    }
}
