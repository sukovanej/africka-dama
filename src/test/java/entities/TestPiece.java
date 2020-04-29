package entities;

import factories.BoardFactory;
import logic.PossibleMovesLogic;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class TestPiece {
    @Test
    public void testPiecePossibleMoves() {
        var board = BoardFactory.initializeBoard();
        var logic = new PossibleMovesLogic(board);
        var piece = new Piece(Position.fromBoardPosition('d', 5), PieceKind.WHITE);

        var possibleMoves = logic.getPossibleMoves(piece, piece.getPosition());
        var expectedPossibleMoves = new HashSet<Position>(
                Arrays.asList(
                        Position.fromBoardPosition('c', 6),
                        Position.fromBoardPosition('c', 5),
                        Position.fromBoardPosition('e', 6),
                        Position.fromBoardPosition('d', 6),
                        Position.fromBoardPosition('e', 5)
                )
        );

        assertEquals(expectedPossibleMoves, possibleMoves);
    }

    @Test
    public void testPiecePossibleMovesCornerCase() {
        var board = BoardFactory.initializeBoard();
        var logic = new PossibleMovesLogic(board);
        var piece = new Piece(Position.fromBoardPosition('a', 3), PieceKind.WHITE);

        var possibleMoves = logic.getPossibleMoves(piece, piece.getPosition());
        var expectedPossibleMoves = new HashSet<Position>(
                Arrays.asList(
                        Position.fromBoardPosition('a', 4),
                        Position.fromBoardPosition('b', 4),
                        Position.fromBoardPosition('b', 3)
                )
        );

        assertEquals(expectedPossibleMoves, possibleMoves);
    }

    @Test
    public void testPiecePossibleMovesSecondCornerCase() {
        var board = BoardFactory.initializeBoard();
        var logic = new PossibleMovesLogic(board);
        var piece = new Piece(Position.fromBoardPosition('i', 3), PieceKind.WHITE);

        var possibleMoves = logic.getPossibleMoves(piece, piece.getPosition());
        var expectedPossibleMoves = new HashSet<Position>(
                Arrays.asList(
                        Position.fromBoardPosition('i', 4),
                        Position.fromBoardPosition('h', 4),
                        Position.fromBoardPosition('h', 3)
                )
        );

        assertEquals(expectedPossibleMoves, possibleMoves);
    }
}
