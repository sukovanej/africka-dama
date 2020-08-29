package logic;

import entities.Board;
import entities.PieceKind;

import java.util.Optional;

public class EndGameLogic {
    private Integer movesWithoutJump;
    private final Board board;

    public EndGameLogic(Board board) {
        this.board = board;
        movesWithoutJump = 0;
    }

    private boolean isDraw() {
        return movesWithoutJump >= 30;
    }

    public boolean isEndOfGame() {
        var allPieces = board.getPieces().size();
        var whitePieces = board
                .getPieces()
                .stream()
                .filter(piece -> piece.getKind() == PieceKind.WHITE)
                .count();

        return isDraw() || whitePieces == 0 || allPieces == whitePieces;
    }

    public Optional<PieceKind> getWinningPlayer() {
        if (isDraw())
            return Optional.empty();

        return Optional.of(board.getPieces().stream().findAny().get().getKind());
    }
}
