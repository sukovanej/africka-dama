package computer;

import entities.PieceKind;
import factories.BoardFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBestMoveLogic {
    @Test
    public void TestBestMoveLogicTheGameStartWhitePlayer() {
        var board = BoardFactory.initializeBoard();

        BestMoveLogic.getBestMove(CloneableBoard.fromBoard(board), 15, PieceKind.WHITE);

        BestMoveLogic.getBestMove(CloneableBoard.fromBoard(board), 1, PieceKind.WHITE);
        assertEquals(board.getPieces().size(), 80);

        BestMoveLogic.getBestMove(CloneableBoard.fromBoard(board), 2, PieceKind.WHITE);
        assertEquals(board.getPieces().size(), 80);

        BestMoveLogic.getBestMove(CloneableBoard.fromBoard(board), 3, PieceKind.WHITE);
        assertEquals(board.getPieces().size(), 80);

        BestMoveLogic.getBestMove(CloneableBoard.fromBoard(board), 4, PieceKind.WHITE);
        assertEquals(board.getPieces().size(), 80);

        BestMoveLogic.getBestMove(CloneableBoard.fromBoard(board), 5, PieceKind.WHITE);
        assertEquals(board.getPieces().size(), 80);
    }

    @Test
    public void TestSampleGame() {
        var board = BoardFactory.initializeBoard();
    }
}
