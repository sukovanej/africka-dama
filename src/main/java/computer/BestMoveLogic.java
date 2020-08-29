package computer;

import entities.Board;
import entities.Move;
import entities.PieceKind;
import logic.EndGameLogic;
import logic.PossibleMovesLogic;

import java.util.concurrent.atomic.AtomicInteger;

public class BestMoveLogic {
    public static Move getBestMove(Board board, int depth, PieceKind player) {
        var numberOfNodes = new AtomicInteger(0);

        var logic = new PossibleMovesLogic(board);
        var possibleMoves = logic.getAllPossibleMoves(player);

        var bestScore = player == PieceKind.WHITE ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
        Move bestMove = null;
        var alpha = Float.NEGATIVE_INFINITY;
        var beta = Float.POSITIVE_INFINITY;

        for (Move move : possibleMoves) {
            board.playMove(move);

            var nextPlayer = player == PieceKind.BLACK ? PieceKind.WHITE : PieceKind.BLACK;
            var newScore = getBestMove(board, alpha, beta, depth - 1, nextPlayer, numberOfNodes);

            if (isBetterMoveForPlayer(bestScore, newScore, player)) {
                bestScore = newScore;
                bestMove = move;
            }

            if (player == PieceKind.WHITE) {
                alpha = Math.max(alpha, bestScore);
            } else {
                beta = Math.min(beta, bestScore);
            }

            board.undoMove(move);

            if (alpha >= beta)
                break;
        }

        System.out.println("number of nodes: " + numberOfNodes.get());
        return bestMove;
    }

    private static float getBestMove(
            Board board,
            float alpha,
            float beta,
            int depth,
            PieceKind player,
            AtomicInteger numberOfNodes) {
        var endGameLogic = new EndGameLogic(board);

        if (endGameLogic.isEndOfGame()) {
            if (endGameLogic.getWinningPlayer().get() == PieceKind.WHITE)
                return Float.POSITIVE_INFINITY;
            else
                return Float.NEGATIVE_INFINITY;
        }

        if (depth == 0) {
            return calculateScore(board, player);
        }

        var logic = new PossibleMovesLogic(board);
        var possibleMoves = logic.getAllPossibleMoves(player);
        var bestScore = player == PieceKind.WHITE ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;

        for (Move move : possibleMoves) {
            board.playMove(move);
            numberOfNodes.incrementAndGet();

            var nextPlayer = player == PieceKind.BLACK ? PieceKind.WHITE : PieceKind.BLACK;
            var newScore = getBestMove(board, alpha, beta, depth - 1, nextPlayer, numberOfNodes);

            if (isBetterMoveForPlayer(bestScore, newScore, player))
                bestScore = newScore;

            if (player == PieceKind.WHITE) {
                alpha = Math.max(alpha, bestScore);
            } else {
                beta = Math.min(beta, bestScore);
            }

            board.undoMove(move);

            if (alpha >= beta)
                break;
        }

        return bestScore;
    }

    private static float calculateScore(Board board, PieceKind player) {
        float result = 0;
        for (var piece : board.getPieces()) {
            float pieceValue = piece.isQueen() ? 10 : 1;

            if (piece.getKind() == PieceKind.WHITE)
                result += pieceValue;
            else
                result -= pieceValue;
        }
        return result;
    }

    private static boolean isBetterMoveForPlayer(float currentRating, float newRating, PieceKind player) {
        return (player == PieceKind.WHITE && newRating > currentRating
                || player == PieceKind.BLACK && newRating < currentRating);
    }
}
