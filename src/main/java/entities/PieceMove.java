package entities;

import java.util.Objects;
import java.util.Optional;

public class PieceMove {
    private final Piece piece;
    private final Optional<Position> to;
    private final PieceMoveKind moveKind;

    private PieceMove(Piece piece, Optional<Position> to, PieceMoveKind moveKind) {
        this.piece = piece;
        this.to = to;
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

    public Piece getPiece() {
        return piece;
    }

    public Optional<Position> getTo() {
        return to;
    }

    public PieceMoveKind getMoveKind() {
        return moveKind;
    }

    @Override
    public String toString() {
        if (moveKind == PieceMoveKind.MOVE)
            return "(MOVE " + piece.getPosition() + " -> " + to.get() + ")";
        else if (moveKind == PieceMoveKind.DISCARD)
            return "(DISCARD " + piece.getPosition() + ")";
        else
            return "(PROMOTE " + piece.getPosition() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceMove move = (PieceMove) o;
        return piece.equals(move.piece) && to.equals(move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, to);
    }
}
