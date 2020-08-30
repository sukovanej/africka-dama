package factories;

import entities.Board;
import entities.Piece;
import entities.PieceKind;
import entities.Position;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BoardFactory {
    public static Set<Piece> initializePieces() {
        var pieces = new HashSet<Piece>();

        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++) {
                if (column == 4 && row == 4) continue; // skip the center which doesn't contain any piece

                var position = new Position(row, column);
                var kind = (row < 4 || row == 4 && column > 4) ? PieceKind.WHITE : PieceKind.BLACK;

                pieces.add(new Piece(position, kind));
            }

        return pieces;
    }

    public static Board initializeBoard() {
        return new Board(initializePieces());
    }

    public static Board initializeTestingBoard() {
        var pieces = new HashSet<Piece>(Arrays.asList(
                new Piece(Position.fromBoardPosition('d', 4), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('e', 5), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('c', 7), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('d', 7), PieceKind.BLACK)
        ));

        return new Board(pieces);
    }

    public static Board initializeTestingBoardWithQueen() {
        var pieces = new HashSet<Piece>(Arrays.asList(
                new Piece(Position.fromBoardPosition('d', 9), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('c', 6), PieceKind.BLACK)
        ));

        return new Board(pieces);
    }
}
