package computer;

import entities.Board;
import entities.Piece;
import entities.PieceKind;
import entities.Position;
import factories.BoardFactory;
import logic.EndGameLogic;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void TestEndOfGameBestMoveLogic() {
        var pieces = new HashSet<Piece>(List.of(
                new Piece(Position.fromBoardPosition('c', 1), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('c', 2), PieceKind.BLACK)));
        var board = new Board(pieces);

        var move = BestMoveLogic.getBestMove(board, 3, PieceKind.BLACK);
        board.playMove(move);

        move = BestMoveLogic.getBestMove(board, 3, PieceKind.WHITE);
        board.playMove(move);

        var endGameLogic = new EndGameLogic(board);

        assertTrue(endGameLogic.isEndOfGame());
        assertEquals(endGameLogic.getWinningPlayer().get(), PieceKind.WHITE);
    }
}
