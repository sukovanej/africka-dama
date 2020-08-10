package logic;

import entities.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class TestEndGameLogic {
    @Test
    public void testEndGameSimpleCase() {
        var pieces = new HashSet<Piece>(List.of(
                new Piece(Position.fromBoardPosition('e', 5), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('e', 6), PieceKind.BLACK)));
        var board = new Board(pieces);
        var endGameLogic = new EndGameLogic(board);

        var move = new Move(
                PieceMove.Move(
                        board.getPieceFromBoardPosition('e', 5).get(),
                        Position.fromBoardPosition('e', 7)),
                PieceMove.Discard(board.getPieceFromBoardPosition('e', 6).get()));

        assertFalse(endGameLogic.isEndOfGame());

        board.playMove(move);
        assertTrue(endGameLogic.isEndOfGame());
        assertEquals(endGameLogic.getWinningPlayer().get(), PieceKind.WHITE);
    }
}
