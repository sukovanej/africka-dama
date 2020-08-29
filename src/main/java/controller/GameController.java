package controller;

import computer.BestMoveLogic;
import computer.CloneableBoard;
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

    private boolean isWhiteComputer = true;
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
        System.out.println("move started");
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
                System.out.println("Must continue");
                possibleMoves = possibleMovesPreviousPiece;

                if (isCurrentPlayerComputer())
                    playComputerMove();

                return;
            }
        }

        currentPlayer = (currentPlayer == PieceKind.BLACK) ? PieceKind.WHITE : PieceKind.BLACK;
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
        System.out.println("move made");
        for (var move : possibleMoves)
            for (var pieceMove : move.getMoves()) {
                if (pieceMove.getMoveKind() == PieceMoveKind.MOVE
                        && pieceMove.getNewPosition().get().equals(position)
                        && pieceMove.getPosition() == selectedPiece.getPosition()) {
                    System.out.println("actually made move");
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

        System.out.println("didn't do a move, position = " + position + ", possible moves = " + possibleMoves);
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
            System.out.println("Computer move...");
            var bestMove = BestMoveLogic.getBestMove(CloneableBoard.fromBoard(board), depth, currentPlayer);

            for (var move : bestMove.getMoves()) {
                if (move.getMoveKind() == PieceMoveKind.MOVE) {
                    PieceView pieceView = null;

                    for (var _pieceView : view.getPieceViews()) {
                        if (_pieceView.getPiece().getPosition().equals(move.getPosition()))
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
            System.out.println("Computer finished");
            state.stopComputerMove();
        }).start();
    }

    private boolean isCurrentPlayerComputer() {
        return currentPlayer == PieceKind.WHITE && isWhiteComputer || currentPlayer == PieceKind.BLACK && isBlackComputer;
    }
}
