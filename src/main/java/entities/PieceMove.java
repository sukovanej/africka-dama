package entities;

import java.util.Objects;
import java.util.Optional;

public class PieceMove {
    private final Piece piece;
    private final Optional<Position> to;

    public PieceMove(Piece piece, Position to) {
        this.piece = piece;
        this.to = Optional.of(to);
    }

    public PieceMove(Piece piece) {
        this.piece = piece;
        this.to = Optional.empty();
    }

    public Piece getPiece() {
        return piece;
    }

    public Optional<Position> getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "PieceMove{piece=" + piece + ", to=" + to + "}";
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
