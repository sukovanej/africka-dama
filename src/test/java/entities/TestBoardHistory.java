package entities;

import factories.BoardFactory;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestBoardHistory {
    @Test
    public void testGameStart() {
        var board = BoardFactory.initializeBoard();
        var boardHistory = new BoardHistory(board);

        var move = new Move(
                PieceMove.Move(
                        board.getPieceFromBoardPosition('e', 4).get(),
                        Position.fromBoardPosition('e', 5)));

        board.playMove(move);
        boardHistory.movePlayed(move);

        boardHistory.previous();

        assertTrue(board.getPieceFromBoardPosition('e', 4).isPresent());
        assertTrue(board.getPieceFromBoardPosition('e', 5).isEmpty());

        boardHistory.next();
        assertTrue(board.getPieceFromBoardPosition('e', 4).isEmpty());
        assertTrue(board.getPieceFromBoardPosition('e', 5).isPresent());

        move = new Move(
                PieceMove.Move(
                        board.getPieceFromBoardPosition('e', 6).get(),
                        Position.fromBoardPosition('e', 4)),
                PieceMove.Discard(
                        board.getPieceFromBoardPosition('e', 5).get()));

        board.playMove(move);
        boardHistory.movePlayed(move);

        assertTrue(board.getPieceFromBoardPosition('e', 6).isEmpty());
        assertTrue(board.getPieceFromBoardPosition('e', 5).isEmpty());
        assertTrue(board.getPieceFromBoardPosition('e', 4).isPresent());

        boardHistory.previous();

        assertTrue(board.getPieceFromBoardPosition('e', 6).isPresent());
        assertTrue(board.getPieceFromBoardPosition('e', 5).isPresent());
        assertTrue(board.getPieceFromBoardPosition('e', 4).isEmpty());
    }
}
