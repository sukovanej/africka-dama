package entities;

import event.BoardListener;

import java.util.*;

public class Board {
    public List<BoardListener> boardListeners;

    protected final Optional<Piece>[][] board; // TODO: add @NonNull annotation
    protected final Set<Piece> pieces;

    public Board(Set<Piece> pieces) {
        this.pieces = pieces;
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

    public void playMove(Move moves) {
        for (var move : moves.getMoves()) {
            var piece = move.getPiece();
            var piecePosition = move.getPiece().getPosition();

            board[piecePosition.getRow()][piecePosition.getColumn()] = Optional.empty();

            if (move.getMoveKind() == PieceMoveKind.MOVE) {
                var position = move.getTo().get();
                board[position.getRow()][position.getColumn()] = Optional.of(move.getPiece());
                piece.setPosition(position);

                for (var listener : boardListeners) {
                    listener.pieceMoved(piece);
                }
            } else if (move.getMoveKind() == PieceMoveKind.DISCARD) {
                for (var listener : boardListeners) {
                    listener.pieceRemoved(piece);
                    pieces.remove(piece);
                }
            } else if (move.getMoveKind() == PieceMoveKind.PROMOTE_INTO_QUEEN) {
                for (var listener : boardListeners) {
                    listener.promoteIntoQueen(piece);
                    piece.promoteIntoQueen();
                }
            }
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

    public Set<Piece> getPieces() {
        return pieces;
    }
}
