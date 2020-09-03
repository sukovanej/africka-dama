package controller;

import computer.BestMoveLogic;
import entities.*;
import factories.BoardFactory;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import logic.EndGameLogic;
import logic.PossibleMovesLogic;
import ui.GameBoardView;
import ui.PieceView;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class GameController {
    private final Board board;
    private final PossibleMovesLogic logic;
    private final GameBoardView view;
    private final EndGameLogic endGameLogic;
    private final BoardHistory boardHistory;

    private boolean isWhiteComputer = false;
    private boolean isBlackComputer = true;
    private int depth = 4;

    private PieceKind currentPlayer;
    private Piece selectedPiece;
    private Set<Move> possibleMoves;
    private float currentRating;

    public final ViewState state;

    // callbacks
    public Runnable onRatingChanged;
    public Runnable onComputerCalculationStarted;
    public Runnable onComputerCalculationFinished;
    public Consumer<Optional<PieceKind>> onEndOfGame;
    public Consumer<Optional<PieceKind>> onEndOfGameMovementAttempt;

    public GameController(Pane root) {
        board = BoardFactory.initializeBoard();
        logic = new PossibleMovesLogic(board);
        endGameLogic = new EndGameLogic(board);
        view = new GameBoardView(root, board);
        boardHistory = new BoardHistory(board);

        currentPlayer = PieceKind.WHITE;
        possibleMoves = logic.getAllPossibleMoves(currentPlayer);
        currentRating = BestMoveLogic.calculateScore(board);

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
        if (endGameLogic.isEndOfGame()) {
            Platform.runLater(state::reset);
            return;
        }

        selectedPiece = pieceView.getPiece();
        var piece = pieceView.getPiece();

        if (piece.getKind() != currentPlayer) {
            Platform.runLater(state::reset);
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
        currentRating = BestMoveLogic.calculateScore(board);
        onRatingChanged.run();

        if (isCurrentPlayerComputer())
            playComputerMove();
    }

    public void reset() {
        view.resetView();

        if (!endGameLogic.isEndOfGame())
            highlightPossibleMoves();
        else
            onEndOfGameMovementAttempt.accept(endGameLogic.getWinningPlayer());
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
        for (var move : possibleMoves) {
            var pieceMove = move.getPieceMove();
            if (pieceMove.getNewPosition().get().equals(position)
                    && pieceMove.getPosition().equals(selectedPiece.getPosition())) {
                board.playMove(move);
                boardHistory.movePlayed(move);

                if (endGameLogic.isEndOfGame()) {
                    onEndOfGame.accept(endGameLogic.getWinningPlayer());
                    reset();
                } else {
                    switchPlayer(move);
                }
                return;
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
            onComputerCalculationStarted.run();
            var bestMove = BestMoveLogic.getBestMove(board, depth, currentPlayer, possibleMoves);

            for (var move : bestMove.getMoves()) {
                if (move.getMoveKind() == PieceMoveKind.MOVE) {
                    PieceView pieceView = null;

                    for (var _pieceView : view.getPieceViews())
                        if (_pieceView.getPiece() == move.getPiece())
                            pieceView = _pieceView;

                    sleep(100);
                    state.startMove(pieceView);

                    sleep(300);
                    state.makeMove(move.getNewPosition().get());
                    state.reset();
                }
            }

            if (!endGameLogic.isEndOfGame())
                onComputerCalculationFinished.run();

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

    public void showBestMove() {
        new Thread(() -> {
            onComputerCalculationStarted.run();

            var bestMove = BestMoveLogic.getBestMove(board, depth, currentPlayer, possibleMoves);
            var pieceMove = bestMove.getPieceMove();

            var fromLocation = pieceMove.getPosition();
            var toLocation = pieceMove.getNewPosition().get();

            Platform.runLater(() -> {
                view.getPaneView(fromLocation.getRow(), fromLocation.getColumn()).highlightComputerMove();
                view.getPaneView(toLocation.getRow(), toLocation.getColumn()).highlightComputerMove();
            });

            onComputerCalculationFinished.run();
        }).start();
    }

    public void restartGame() {
        boardHistory.restart();
        currentPlayer = boardHistory.getCurrentPlayer();
        possibleMoves = logic.getAllPossibleMoves(currentPlayer);
        reset();
    }

    public BoardHistory getBoardHistory() {
        return boardHistory;
    }

    public void setPlayer(PieceKind currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void recalculatePossibleMoves() {
        possibleMoves = logic.getAllPossibleMoves(currentPlayer);
    }

    public boolean isWhiteComputer() {
        return isWhiteComputer;
    }

    public void switchWhite() {
        isWhiteComputer = !isWhiteComputer;
        if (currentPlayer == PieceKind.WHITE && isCurrentPlayerComputer() && !state.isComputerPlaying())
            playComputerMove();

    }

    public boolean isBlackComputer() {
        return isBlackComputer;
    }

    public void switchBlack() {
        isBlackComputer = !isBlackComputer;
        if (currentPlayer == PieceKind.BLACK && isCurrentPlayerComputer() && !state.isComputerPlaying())
            playComputerMove();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public float getCurrentRating() {
        return currentRating;
    }
}
