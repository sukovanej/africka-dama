package entities;

import factories.BoardFactory;
import logic.PossibleMovesLogic;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBoard {
    @Test
    public void testPlayMove() {
        var board = BoardFactory.initializeBoard();
        var move = new Move(new PieceMove(
                board.getPieceFromBoardPosition('f', 5).get(),
                Position.fromBoardPosition('e', 5))
        );

        assertTrue(board.getPieceFromBoardPosition('f', 5).isPresent());

        board.playMove(move);

        assertTrue(!board.getPieceFromBoardPosition('f', 5).isPresent());
        assertTrue(board.getPieceFromBoardPosition('e', 5).isPresent());
    }
}
