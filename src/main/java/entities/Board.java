package entities;

import event.BoardListener;

import java.util.*;

public class Board {
    public List<BoardListener> boardListeners;

    protected final Set<Piece> pieces;

    public Board(Set<Piece> pieces) {
        this.pieces = pieces;
        boardListeners = new ArrayList<>();
    }

    public void playMove(Move moves) {
        for (var move : moves.getMoves()) {
            var piece = getPieceOnPosition(move.getFrom()).get();

            if (move.getMoveKind() == PieceMoveKind.MOVE) {
                var position = move.getTo().get();
                piece.setPosition(position);

                for (var listener : boardListeners) {
                    listener.pieceMoved(piece);
                }
            } else if (move.getMoveKind() == PieceMoveKind.DISCARD) {
                for (var listener : boardListeners) {
                    listener.pieceRemoved(piece);
                }
                pieces.remove(piece);
            } else if (move.getMoveKind() == PieceMoveKind.PROMOTE_INTO_QUEEN) {
                for (var listener : boardListeners) {
                    listener.promoteIntoQueen(piece);
                }
                piece.promoteIntoQueen();
            }
        }
    }

    public Optional<Piece> getPieceFromBoardPosition(char row, int column) {
        return getPieceOnPosition(Position.fromBoardPosition(row, column));
    }

    public Optional<Piece> getPieceOnPosition(Position position) {
        for (var piece : pieces)
            if (piece.getPosition().equals(position))
                return Optional.of(piece);
        return Optional.empty();
    }

    public Optional<Piece> getPieceOnPosition(int row, int column) {
        return getPieceOnPosition(new Position(row, column));
    }

    public void addBoardListener(BoardListener listener) {
        boardListeners.add(listener);
    }

    public Set<Piece> getPieces() {
        return pieces;
    }

    @Override
    public String toString() {
        var result = "";

        for (int j = 9; j >= 1; j--) {
            for (char i = 'a'; i < 'j'; i++) {
                var piece = getPieceFromBoardPosition(i, j);

                if (piece.isEmpty())
                    result += " ";
                else if (piece.get().getKind() == PieceKind.WHITE)
                    result += "o";
                else
                    result += "x";
            }
            result += "\n";
        }

        return result;
    }
}
