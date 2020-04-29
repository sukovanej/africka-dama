package factories;

import entities.Board;
import entities.Piece;
import entities.PieceKind;
import entities.Position;

import java.util.HashSet;
import java.util.Set;

public class BoardFactory {
    public static Set<Piece> initializePieces() {
        var pieces = new HashSet<Piece>();

        for (int row = 0; row < 9; row ++)
            for (int column = 0; column < 9; column++) {
                if (column == 4 && row == 4) continue; // skip the center which doesn't contain any piece

                var position = new Position(column, row);
                var kind = (row < 4 || row == 4 && column > 4) ? PieceKind.WHITE : PieceKind.BLACK;

                pieces.add(new Piece(position, kind));
            }

        return pieces;
    }

    public static Board initializeBoard() {
        return new Board(initializePieces());
    }
}
