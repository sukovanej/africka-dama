package entities;

import event.BoardListener;

import java.util.*;

public class Board {
    public List<BoardListener> boardListeners;

    private final Optional<Piece>[][] board; // TODO: add @NonNull annotation

    public Board(Set<Piece> pieces) {
        boardListeners = new ArrayList<>();
        board = new Optional[9][9];

        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++) {
                board[row][column] = Optional.empty();
            }

        for (var piece : pieces) {
            var position = piece.getPosition();
            board[position.getRow()][position.getColumn()] = Optional.of(piece);
        }
    }

    public void playMove(Move moveAggregate) {
        for (var move : moveAggregate.getMoves()) {
            var piece = move.getPiece();
            var piecePosition = move.getPiece().getPosition();

            board[piecePosition.getRow()][piecePosition.getColumn()] = Optional.empty();

            move.getTo().ifPresentOrElse((position) -> {
                board[position.getRow()][position.getColumn()] = Optional.of(move.getPiece());
                piece.setPosition(position);

                for (var listener : boardListeners) {
                    listener.pieceMoved(piece);
                }
            }, () -> {
                for (var listener : boardListeners) {
                    listener.pieceRemoved(piece);
                }
            });

            // TODO: resolve promotion into queen
        }
    }

    public Optional<Piece> getPieceFromBoardPosition(char row, int column) {
        var boardPosition = Position.fromBoardPosition(row, column);
        return board[boardPosition.getRow()][boardPosition.getColumn()];
    }

    public Optional<Piece> getPieceOnPosition(Position position) {
        return board[position.getRow()][position.getColumn()];
    }

    public Optional<Piece>[][] getBoard() {
        return board;
    }

    public void addBoardListerner(BoardListener listener) {
        boardListeners.add(listener);
    }
}
