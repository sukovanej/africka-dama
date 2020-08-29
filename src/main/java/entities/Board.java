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

    public void playMove(Move moves) throws UnknownError {
        var sortedMoves = new ArrayList<>(moves.getMoves());

        // do movements first, then discards and promotions
        sortedMoves.sort((left, right) -> {
            if (left.getMoveKind() == PieceMoveKind.MOVE)
                return -1;
            else if (right.getMoveKind() == PieceMoveKind.MOVE)
                return 1;
            return 0;
        });

        for (var move : sortedMoves) {
            var optPiece = getPieceOnPosition(move.getPosition());

            if (optPiece.isEmpty())
                throw new UnknownError();

            var piece = optPiece.get();

            if (move.getMoveKind() == PieceMoveKind.MOVE) {
                var position = move.getNewPosition().get();
                piece.setPosition(position);

                for (var listener : boardListeners) {
                    listener.pieceMoved(piece);
                }
            } else if (move.getMoveKind() == PieceMoveKind.DISCARD) {
                for (var listener : boardListeners) {
                    listener.pieceRemoved(piece);
                }

                var previous = pieces.size();
                pieces.remove(piece);
                // piece removal check
                if (pieces.size() + 1 != previous) {
                    throw new UnknownError();
                }
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
            result += j + " ";
            for (char i = 'a'; i < 'j'; i++) {
                var piece = getPieceFromBoardPosition(i, j);

                if (piece.isEmpty())
                    result += "  ";
                else if (piece.get().getKind() == PieceKind.WHITE)
                    result += "o ";
                else
                    result += "x ";
            }
            result += "\n";
        }

        result += "  ";

        for (char i = 'a'; i < 'j'; i++) {
            result += i + " ";
        }

        return result;
    }
}
