package entities;

import java.util.Objects;
import java.util.Optional;

public class PieceMove {
    private final Piece piece;
    private final Position position;
    private final Optional<Position> newPosition;
    private final PieceMoveKind moveKind;

    private PieceMove(Piece piece, Optional<Position> newPosition, PieceMoveKind moveKind) {
        this.piece = piece;
        this.position = piece.getPosition();
        this.newPosition = newPosition;
        this.moveKind = moveKind;
    }

    public static PieceMove Move(Piece piece, Position to) {
        return new PieceMove(piece, Optional.of(to), PieceMoveKind.MOVE);
    }

    public static PieceMove Discard(Piece piece) {
        return new PieceMove(piece, Optional.empty(), PieceMoveKind.DISCARD);
    }

    public static PieceMove PromoteIntoQueen(Piece piece) {
        return new PieceMove(piece, Optional.empty(), PieceMoveKind.PROMOTE_INTO_QUEEN);
    }

    public Optional<Position> getNewPosition() {
        return newPosition;
    }

    public PieceMoveKind getMoveKind() {
        return moveKind;
    }

    @Override
    public String toString() {
        if (moveKind == PieceMoveKind.MOVE)
            return "(MOVE " + position + " -> " + newPosition.get() + ")";
        else if (moveKind == PieceMoveKind.DISCARD)
            return "(DISCARD " + getPosition() + ")";
        else
            return "(PROMOTE " + getPosition() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceMove move = (PieceMove) o;
        return position.equals(move.getPosition()) && newPosition.equals(move.newPosition) && moveKind.equals(move.getMoveKind());
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, newPosition, moveKind);
    }

    public Position getPosition() {
        return position;
    }

    public Piece getPiece() {
        return piece;
    }
}
