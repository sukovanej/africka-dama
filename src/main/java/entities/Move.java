package entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Move {
    final private Set<PieceMove> moves;

    public Move(Set<PieceMove> moves) {
        this.moves = moves;
    }

    public Move(PieceMove... moves) {
        this.moves = new HashSet<>(Arrays.asList(moves));
    }

    public Set<PieceMove> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return "Move{" + "moves=" + moves + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return moves.equals(move.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moves);
    }
}
