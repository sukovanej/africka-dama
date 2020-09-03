package entities;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Board {
    protected final Set<Piece> pieces;
    protected final Set<Piece> discardedPieces;
    protected int numberOfMovesWithoutJump;

    public Board(Set<Piece> pieces) {
        this.pieces = pieces;
        this.discardedPieces = new HashSet<>();
        this.numberOfMovesWithoutJump = 0;
    }

    public void playMove(Move moves) throws UnknownError {
        if (moves.isJumping()) {
            numberOfMovesWithoutJump = 0;
        } else {
            numberOfMovesWithoutJump++;
        }

        for (var move : moves.getMoves()) {
            var piece = move.getPiece();

            if (move.getMoveKind() == PieceMoveKind.MOVE) {
                var position = move.getNewPosition().get();
                piece.setPosition(position);
            } else if (move.getMoveKind() == PieceMoveKind.DISCARD) {
                var previousPieces = pieces.size();
                var previousDiscardedPieces = discardedPieces.size();

                pieces.remove(piece);
                discardedPieces.add(piece);

                // piece removal check
                if (pieces.size() + 1 != previousPieces)
                    throw new UnknownError();
                else if (discardedPieces.size() - 1 != previousDiscardedPieces)
                    throw new UnknownError();

            } else if (move.getMoveKind() == PieceMoveKind.PROMOTE_INTO_QUEEN) {
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

    public void undoMove(Move moves) {
        numberOfMovesWithoutJump--;

        for (var move : moves.getMoves()) {
            var piece = move.getPiece();

            if (move.getMoveKind() == PieceMoveKind.MOVE) {
                var position = move.getPosition();
                piece.setPosition(position);
            } else if (move.getMoveKind() == PieceMoveKind.DISCARD) {
                var previousPieces = pieces.size();
                var previousDiscardedPieces = discardedPieces.size();

                pieces.add(piece);
                discardedPieces.remove(piece);

                // piece return check
                if (pieces.size() - 1 != previousPieces)
                    throw new UnknownError();
                else if (discardedPieces.size() + 1 != previousDiscardedPieces)
                    throw new UnknownError();

            } else if (move.getMoveKind() == PieceMoveKind.PROMOTE_INTO_QUEEN) {
                piece.unpromote();
            }
        }
    }

    public Set<Piece> getDiscardedPieces() {
        return discardedPieces;
    }

    public Board copy() {
        var newPieces = new HashSet<Piece>();

        for (var piece : pieces) {
            newPieces.add(piece.copy());
        }

        return new Board(newPieces);
    }

    public int getNumberOfMovesWithoutJump() {
        return numberOfMovesWithoutJump;
    }

    public void resetNumberOfMovesWithoutJump() {
        numberOfMovesWithoutJump = 0;
    }
}
