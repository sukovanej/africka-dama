package logic;

import entities.*;
import factories.BoardFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class TestPossibleMovesLogicWithQueen {
    @Test
    public void testGetPossibleMovesForPieceSimpleCase() {
        var pieces = new HashSet<Piece>(Arrays.asList(
                new Piece(Position.fromBoardPosition('b', 1), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('e', 6), PieceKind.BLACK)));

        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('b', 1).get();

        var expected = new HashSet<>(Collections.singletonList(new Move(
                new PieceMove(testingPiece, Position.fromBoardPosition('f', 7)),
                new PieceMove(board.getPieceFromBoardPosition('f', 6).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }
}
