package entities;

import factories.BoardFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestBoardFactory {
    @Test
    public void testInitializePieces() {
        var pieces = BoardFactory.initializePieces();

        assertEquals(pieces.size(), 9 * 9 - 1);

        for (var piece : pieces) {
            var position = piece.getPosition();

            if (position.getRow() > 4 || position.getRow() == 4 && position.getColumn() < 4) {
                assertEquals(PieceKind.BLACK, piece.getKind());
            } else if (position.getColumn() != 4 && position.getRow() != 4) {
                assertEquals(PieceKind.WHITE, piece.getKind());
            }
        }
    }

    @Test
    public void testInitializeBoard() {
        var board = BoardFactory.initializeBoard();

        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++) {
                var optionalPieces = board.getPieceOnPosition(row, column);

                if (row > 4 || row == 4 && column < 4) {
                    assertTrue(optionalPieces.isPresent());
                    assertEquals(PieceKind.BLACK, optionalPieces.get().getKind());
                } else if (row == 4 && column == 4) {
                    assertFalse(optionalPieces.isPresent());
                } else {
                    assertTrue(optionalPieces.isPresent());
                    assertEquals(PieceKind.WHITE, optionalPieces.get().getKind());
                }
            }
    }
}
