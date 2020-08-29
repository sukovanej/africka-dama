package entities;

import factories.BoardFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestBoard {
    @Test
    public void testPlayMove() {
        var board = BoardFactory.initializeBoard();
        var move = new Move(PieceMove.Move(
                board.getPieceFromBoardPosition('f', 5).get(),
                Position.fromBoardPosition('e', 5))
        );

        assertTrue(board.getPieceFromBoardPosition('f', 5).isPresent());

        board.playMove(move);

        assertFalse(board.getPieceFromBoardPosition('f', 5).isPresent());
        assertTrue(board.getPieceFromBoardPosition('e', 5).isPresent());
    }

    @Test
    public void testPlayMoveDiscard() {
        var board = BoardFactory.initializeBoard();

        assertTrue(board.getPieceFromBoardPosition('e', 6).isPresent());
        assertTrue(board.getPieceFromBoardPosition('e', 4).isPresent());

        var move = new Move(
                PieceMove.Move(
                        board.getPieceFromBoardPosition('e', 6).get(),
                        Position.fromBoardPosition('e', 4)),
                PieceMove.Discard(board.getPieceFromBoardPosition('e', 4).get()));
        board.playMove(move);

        assertTrue(board.getPieceFromBoardPosition('e', 6).isEmpty());
        assertTrue(board.getPieceFromBoardPosition('e', 5).isEmpty());
        assertTrue(board.getPieceFromBoardPosition('e', 4).isPresent());
    }
}
