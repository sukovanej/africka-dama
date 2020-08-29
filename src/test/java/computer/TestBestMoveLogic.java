package computer;

import entities.PieceKind;
import factories.BoardFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBestMoveLogic {
    @Test
    public void TestBestMoveLogicTheGameStartWhitePlayer() {
        var board = BoardFactory.initializeBoard();

        BestMoveLogic.getBestMove(board, 1, PieceKind.WHITE);
        BestMoveLogic.getBestMove(board, 2, PieceKind.WHITE);
        BestMoveLogic.getBestMove(board, 3, PieceKind.WHITE);
        BestMoveLogic.getBestMove(board, 4, PieceKind.WHITE);
        BestMoveLogic.getBestMove(board, 5, PieceKind.WHITE);

        // check that the logic didn't break the original board
        assertEquals(board.getPieces().size(), 80);
    }

    @Test
    public void TestSampleGame() {
        var board = BoardFactory.initializeBoard();
    }
}
