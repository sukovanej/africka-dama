package computer;

import entities.Board;
import entities.Move;
import entities.PieceKind;
import logic.EndGameLogic;
import logic.PossibleMovesLogic;

public class Minimax {
    public static Move getBestMove(ClonableBoard board, int depth, PieceKind player) {
        var logic = new PossibleMovesLogic(board);
        var endGameLogic = new EndGameLogic(board);

        var possibleMoves = logic.getAllPossibleMoves(player);
        for (Move move: possibleMoves) {
            var newBoard = board.playMoveAndDuplicate(move);
        }

        return null;
    }

    private static int minimax(ClonableBoard board, PieceKind player) {
        int result = 0;

        for (var piece: board.getPieces()) {
            if (piece.getKind() == player)
                result += 1;
            else
                result -= 1;
        }
        return result;
    }
}
