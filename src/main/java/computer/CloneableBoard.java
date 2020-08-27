package computer;

import entities.Board;
import entities.Piece;

import java.util.HashSet;
import java.util.Set;

public class CloneableBoard extends Board {
    public CloneableBoard(Set<Piece> pieces) {
        super(pieces);
    }

    public CloneableBoard copy() {
        var newPieces = new HashSet<Piece>();
        for (var piece: pieces) {
            newPieces.add(piece.copy());
        }
        return new CloneableBoard(newPieces);
    }

    public static CloneableBoard fromBoard(Board board) {
        return new CloneableBoard(board.getPieces());
    }
}
