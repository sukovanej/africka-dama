package controller;

import computer.CloneableBoard;
import computer.BestMoveLogic;
import entities.*;
import factories.BoardFactory;
import javafx.scene.layout.Pane;
import logic.EndGameLogic;
import logic.PossibleMovesLogic;
import ui.EndGameDialog;
import ui.GameBoardView;
import ui.PieceView;

import java.util.Set;

public class GameController {
    private final Board board;
    private final PossibleMovesLogic logic;
    private final GameBoardView view;
    private final EndGameLogic endGameLogic;

    private boolean isWhiteComputer = false;
    private boolean isBlackComputer = true;
    private int depth = 6;

    private PieceKind currentPlayer;
    private Piece selectedPiece;
    private Set<Move> possibleMoves;

    public final ViewState state;

    public GameController(Pane root) {
        board = BoardFactory.initializeBoard();
        logic = new PossibleMovesLogic(board);
        endGameLogic = new EndGameLogic(board);
        view = new GameBoardView(root, board);

        currentPlayer = PieceKind.WHITE;
        possibleMoves = logic.getAllPossibleMoves(currentPlayer);

        board.addBoardListener(new BoardListener(view));

        state = new ViewState();
        state.startMoveCallable = this::startMove;
        state.resetViewCallable = this::reset;
        state.makeMoveCallable = this::makeMove;
    }

    public void start() {
        view.renderStart();

        for (var pieceView: view.getPieceViews())
            pieceView.getView().setOnMouseClicked(new PieceClickHandler(state, pieceView));

        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++) {
                var pane = view.getPaneView(row, column);
                pane.getView().setOnMouseClicked(new PaneClickHandler(state, pane.getPosition()));
            }

        highlightPossibleMoves();
    }

    private void startMove(PieceView pieceView) {
        selectedPiece = pieceView.getPiece();
        var piece = pieceView.getPiece();

        if (piece.getKind() != currentPlayer) {
            state.reset();
            return;
        }

        pieceView.highlight();

        for (var possibleMove: possibleMoves) {
            var pieceMove = possibleMove.getMoves().stream().filter(
                    (move) -> move.getPiece() == piece && move.getMoveKind() == PieceMoveKind.MOVE).findFirst();

            if (pieceMove.isPresent()) {
                var position = pieceMove.get().getTo().get();
                view.getPaneView(position.getRow(), position.getColumn()).highlight();
            }
        }
    }

    private void switchPlayer(Move previousMove) {
        if (previousMove.isJumping()) {
            var possibleMovesPreviousPiece = logic.getPossibleMovesForPiece(selectedPiece);
            var possibleJumpingMove = possibleMovesPreviousPiece.stream().filter(Move::isJumping).findAny();

            if (possibleJumpingMove.isPresent()) {
                possibleMoves = possibleMovesPreviousPiece;
                return;
            }
        }

        currentPlayer = (currentPlayer == PieceKind.BLACK) ? PieceKind.WHITE : PieceKind.BLACK;
        possibleMoves = logic.getAllPossibleMoves(currentPlayer);
    }

    private void reset() {
        view.resetView();
        highlightPossibleMoves();
    }

    private void highlightPossibleMoves() {
        for (var possibleMove: possibleMoves) {
            for (var pieceMove: possibleMove.getMoves()) {
                if (pieceMove.getMoveKind() == PieceMoveKind.MOVE) {
                    var position = pieceMove.getPiece().getPosition();
                    view.getPaneView(position.getRow(), position.getColumn()).highlightPossibles();
                }
            }
        }
    }

    private void makeMove(Position position) {
        for (var move: possibleMoves)
            for (var pieceMove: move.getMoves()) {
                if (pieceMove.getMoveKind() == PieceMoveKind.MOVE
                        && pieceMove.getTo().get().equals(position)
                        && pieceMove.getPiece() == selectedPiece) {
                    board.playMove(move);

                    if (endGameLogic.isEndOfGame()) {
                        var dialog = new EndGameDialog(endGameLogic.getWinningPlayer(), view.getRoot());
                        dialog.show();
                    } else {
                        switchPlayer(move);
                        return;
                    }
                }
            }
    }

    private void playComputerMove() {
        var bestMove = BestMoveLogic.getBestMove(CloneableBoard.fromBoard(board), depth, currentPlayer);

        for (var move: bestMove.getMoves()) {
            if (move.getMoveKind() == PieceMoveKind.MOVE) {
                PieceView pieceView = null;

                for (var _pieceView: view.getPieceViews()) {
                    if (_pieceView.getPiece().getPosition().equals(move.getFrom()))
                        pieceView = _pieceView;
                }

                new PieceClickHandler(state, pieceView).handle(null);
                new PaneClickHandler(state, move.getTo().get()).handle(null);
            }
        }
    }

    private boolean isCurrentPlayerComputer() {
        return currentPlayer == PieceKind.WHITE && isWhiteComputer || currentPlayer == PieceKind.BLACK && isBlackComputer;
    }
}
