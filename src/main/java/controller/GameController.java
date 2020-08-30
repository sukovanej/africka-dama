package controller;

import computer.BestMoveLogic;
import entities.*;
import factories.BoardFactory;
import javafx.application.Platform;
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
    private final BoardHistory boardHistory;

    private boolean isWhiteComputer = false;
    private boolean isBlackComputer = true;
    private int depth = 5;

    private PieceKind currentPlayer;
    private Piece selectedPiece;
    private Set<Move> possibleMoves;

    public final ViewState state;

    public GameController(Pane root) {
        board = BoardFactory.initializeBoard();
        logic = new PossibleMovesLogic(board);
        endGameLogic = new EndGameLogic(board);
        view = new GameBoardView(root, board);
        boardHistory = new BoardHistory(board);

        currentPlayer = PieceKind.WHITE;
        possibleMoves = logic.getAllPossibleMoves(currentPlayer);

        state = new ViewState();
        state.startMoveCallable = this::startMove;
        state.resetViewCallable = this::reset;
        state.makeMoveCallable = this::makeMove;
    }

    public void start() {
        view.renderStart();

        for (var pieceView : view.getPieceViews())
            pieceView.getView().setOnMouseClicked(new PieceClickHandler(state, pieceView));

        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++) {
                var pane = view.getPaneView(row, column);
                pane.getView().setOnMouseClicked(new PaneClickHandler(state, pane.getPosition()));
            }

        highlightPossibleMoves();

        if (isWhiteComputer) {
            playComputerMove();
        }
    }

    private void startMove(PieceView pieceView) {
        selectedPiece = pieceView.getPiece();
        var piece = pieceView.getPiece();

        if (piece.getKind() != currentPlayer) {
            Platform.runLater(state::reset);
            System.out.println("this piece is not yours");
            return;
        }

        pieceView.highlight();

        for (var possibleMove : possibleMoves) {
            var pieceMove = possibleMove.getMoves().stream().filter(
                    (move) -> move.getPosition() == piece.getPosition() && move.getMoveKind() == PieceMoveKind.MOVE).findFirst();

            if (pieceMove.isPresent()) {
                var position = pieceMove.get().getNewPosition().get();
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

                if (isCurrentPlayerComputer())
                    playComputerMove();

                return;
            }
        }

        currentPlayer = currentPlayer == PieceKind.BLACK ? PieceKind.WHITE : PieceKind.BLACK;
        possibleMoves = logic.getAllPossibleMoves(currentPlayer);

        if (isCurrentPlayerComputer())
            playComputerMove();
    }

    private void reset() {
        view.resetView();
        highlightPossibleMoves();
    }

    private void highlightPossibleMoves() {
        for (var possibleMove : possibleMoves) {
            for (var pieceMove : possibleMove.getMoves()) {
                if (pieceMove.getMoveKind() == PieceMoveKind.MOVE) {
                    var position = pieceMove.getPosition();
                    view.getPaneView(position.getRow(), position.getColumn()).highlightPossibles();
                }
            }
        }
    }

    private void makeMove(Position position) {
        for (var move : possibleMoves)
            for (var pieceMove : move.getMoves()) {
                if (pieceMove.getMoveKind() == PieceMoveKind.MOVE
                        && pieceMove.getNewPosition().get().equals(position)
                        && pieceMove.getPosition().equals(selectedPiece.getPosition())) {
                    board.playMove(move);
                    boardHistory.movePlayed(move);

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

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void playComputerMove() {
        new Thread(() -> {
            state.startComputerMove();
            var bestMove = BestMoveLogic.getBestMove(board, depth, currentPlayer, possibleMoves);

            for (var move : bestMove.getMoves()) {
                if (move.getMoveKind() == PieceMoveKind.MOVE) {
                    PieceView pieceView = null;

                    for (var _pieceView : view.getPieceViews()) {
                        if (_pieceView.getPiece() == move.getPiece())
                            pieceView = _pieceView;
                    }

                    sleep(100);
                    state.startMove(pieceView);
                    System.out.println("[start] Computer clicked on " + pieceView.getPiece().getPosition().toString());

                    sleep(300);
                    state.makeMove(move.getNewPosition().get());
                    state.reset();

                    System.out.println("[confirm] Computer clicked on " + move.getNewPosition().toString());
                }
            }
            state.stopComputerMove();
        }).start();
    }

    private boolean isCurrentPlayerComputer() {
        return currentPlayer == PieceKind.WHITE && isWhiteComputer || currentPlayer == PieceKind.BLACK && isBlackComputer;
    }

    public void historyNext() {
        boardHistory.next();
        historySync();
    }

    public void historyPrevious() {
        boardHistory.previous();
        historySync();
    }

    private void historySync() {
        currentPlayer = boardHistory.getCurrentPlayer();
        var previousMoveOpt = boardHistory.getPreviousMove();

        if (previousMoveOpt.isPresent()) {
            var previousMove = previousMoveOpt.get();

            if (previousMove.getPlayer() == currentPlayer)
                possibleMoves = logic.getPossibleMovesForPiece(previousMove.getPiece());
            else
                possibleMoves = logic.getAllPossibleMoves(currentPlayer);
        } else {
            possibleMoves = logic.getAllPossibleMoves(currentPlayer);
        }

        state.reset();
    }
}
