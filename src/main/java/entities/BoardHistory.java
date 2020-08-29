package entities;

import java.util.ArrayList;
import java.util.List;

public class BoardHistory {
    private final Board board;
    private final List<Move> movesHistory;
    private int pointer;

    public BoardHistory (Board board) {
        this.board = board;
        this.movesHistory = new ArrayList<>();
        this.pointer = -1;
    }

    public void movePlayed(Move move) {
        if (pointer < movesHistory.size() - 1)
            movesHistory.subList(pointer + 1, movesHistory.size()).clear();

        movesHistory.add(move);
        pointer++;
    }

    public void next() {
        if (pointer == movesHistory.size() - 1) return;
        pointer++;
        board.playMove(movesHistory.get(pointer));
    }

    public void previous() {
        if (pointer == -1) return;
        board.undoMove(movesHistory.get(pointer));
        pointer--;
    }

    public PieceKind getCurrentPlayer() {
        if (pointer == -1)
            return PieceKind.WHITE;
        else if (pointer == 0)
            return PieceKind.BLACK;

        var previousMove = movesHistory.get(pointer - 1);

        for (var move: previousMove.getMoves()) {
            if (move.getMoveKind() == PieceMoveKind.MOVE) {
                return move.getPiece().getKind();
            }
        }

        throw new UnknownError();
    }
}
