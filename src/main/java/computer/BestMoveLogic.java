package computer;

import entities.Board;
import entities.Move;
import entities.PieceKind;
import logic.EndGameLogic;
import logic.PossibleMovesLogic;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class BestMoveLogic {
    public static Move getBestMove(Board board, int depth, PieceKind player) {
        var logic = new PossibleMovesLogic(board);
        var possibleMoves = logic.getAllPossibleMoves(player);
        return getBestMove(board, depth, player, possibleMoves);
    }

    public static Move getBestMove(Board board, int depth, PieceKind player, Set<Move> possibleMoves) {
        if (possibleMoves.size() == 1)
            return possibleMoves.stream().findFirst().get();

        var numberOfNodes = new AtomicInteger(0);

        var bestScore = player == PieceKind.WHITE ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
        Move bestMove = null;
        var alpha = Float.NEGATIVE_INFINITY;
        var beta = Float.POSITIVE_INFINITY;
        var newBoard = board.copy();

        for (Move move : possibleMoves) {
            newBoard.playMove(move.convertToOtherBoard(newBoard));

            var nextPlayer = player == PieceKind.BLACK ? PieceKind.WHITE : PieceKind.BLACK;
            var newScore = getBestMove(newBoard, alpha, beta, depth - 1, nextPlayer, numberOfNodes, move);

            if (isBetterMoveForPlayer(bestScore, newScore, player) || bestMove == null) {
                bestScore = newScore;
                bestMove = move;
            }

            if (player == PieceKind.WHITE)
                alpha = Math.max(alpha, bestScore);
            else
                beta = Math.min(beta, bestScore);

            newBoard.undoMove(move.convertToOtherBoard(newBoard));

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
            AtomicInteger numberOfNodes,
            Move previousMove) {
        var endGameLogic = new EndGameLogic(board);

        if (endGameLogic.isEndOfGame()) {
            if (endGameLogic.isDraw())
                return 0;
            else if (endGameLogic.getWinningPlayer().get() == PieceKind.WHITE)
                return Float.POSITIVE_INFINITY;
            else
                return Float.NEGATIVE_INFINITY;
        }

        if (depth == 0)
            return calculateScore(board);

        var logic = new PossibleMovesLogic(board);
        var possibleMoves = logic.getAllPossibleMoves(previousMove);
        var bestScore = player == PieceKind.WHITE ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;

        for (Move move : possibleMoves) {
            board.playMove(move);
            numberOfNodes.incrementAndGet();

            var nextPlayer = player == PieceKind.BLACK ? PieceKind.WHITE : PieceKind.BLACK;
            var newScore = getBestMove(board, alpha, beta, depth - 1, nextPlayer, numberOfNodes, move);

            if (isBetterMoveForPlayer(bestScore, newScore, player))
                bestScore = newScore;

            if (player == PieceKind.WHITE)
                alpha = Math.max(alpha, bestScore);
            else
                beta = Math.min(beta, bestScore);

            board.undoMove(move);

            if (alpha >= beta)
                break;
        }

        return bestScore;
    }

    public static float calculateScore(Board board) {
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
