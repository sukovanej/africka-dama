package computer;

import entities.Board;
import entities.Move;
import entities.Piece;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClonableBoard extends Board {
    public ClonableBoard(Set<Piece> pieces) {
        super(pieces);
    }

    public ClonableBoard playMoveAndDuplicate(Move moves) {
        var newBoard = copy();
        for (var move: moves.getMoves()) {
            var piece = move.getPiece();
            var position = piece.getPosition();
            var newPiece = new Piece(position, piece.getKind(), piece.isQueen());

            newBoard.getBoard()[position.getRow()][position.getColumn()] = Optional.of(newPiece);
            newBoard.getPieces().remove(piece);
            newBoard.getPieces().add(newPiece);
        }
        newBoard.playMove(moves);
        return newBoard;
    }

    private ClonableBoard copy() {
        return new ClonableBoard(new HashSet<>(pieces));
    }

    public static ClonableBoard fromBoard(Board board) {
        return new ClonableBoard(board.getPieces());
    }
}
