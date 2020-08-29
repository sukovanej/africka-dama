package logic;

import entities.*;

import java.util.*;

public class PossibleMovesLogic {
    final private Board board;

    public PossibleMovesLogic(Board board) {
        this.board = board;
    }

    public Set<Move> getQueenPossibleMoves(Piece piece) {
        boolean jumpsOnly = false;
        var moves = new HashSet<Move>();
        var jumps = new HashSet<Move>();

        var movements = Arrays.asList(
                new PositionDiff(1, -1),
                new PositionDiff(1, 0),
                new PositionDiff(1, 1),
                new PositionDiff(0, -1),
                new PositionDiff(0, 1),
                new PositionDiff(-1, -1),
                new PositionDiff(-1, 0),
                new PositionDiff(-1, 1)
        );

        var position = piece.getPosition();

        for (var movement : movements) {
            var nextPosition = position.add(movement);

            while (nextPosition.isOnBoard() && !board.getPieceOnPosition(nextPosition).isPresent()) {
                if (!jumpsOnly)
                    moves.add(new Move(PieceMove.Move(piece, nextPosition)));

                nextPosition = nextPosition.add(movement);
            }

            if (nextPosition.isOnBoard()) {
                var possibleNextPiece = board.getPieceOnPosition(nextPosition);
                var secondNextPosition = nextPosition.add(movement);

                if (possibleNextPiece.isPresent()
                        && secondNextPosition.isOnBoard()
                        && !board.getPieceOnPosition(secondNextPosition).isPresent()) {
                    var nextPiece = possibleNextPiece.get();

                    if (nextPiece.getKind() != piece.getKind()) {
                        nextPosition = nextPosition.add(movement);

                        while (nextPosition.isOnBoard() && !board.getPieceOnPosition(nextPosition).isPresent()) {
                            jumpsOnly = true;
                            jumps.add(new Move(true, PieceMove.Move(piece, nextPosition), PieceMove.Discard(nextPiece)));
                            nextPosition = nextPosition.add(movement);
                        }
                    }
                }
            }
        }

        if (jumpsOnly)
            return jumps;
        else
            return moves;
    }

    public Set<Move> getPossibleMovesForPiece(Piece piece) {
        if (piece.isQueen()) {
            return getQueenPossibleMoves(piece);
        }

        var jumpsOnly = false;
        var moves = new HashSet<Move>();
        var jumps = new HashSet<Move>();

        var movements = new ArrayList<>(Arrays.asList(
                new PositionDiff(0, -1),
                new PositionDiff(0, 1)));

        if (piece.getKind() == PieceKind.WHITE) {
            movements.add(new PositionDiff(1, -1));
            movements.add(new PositionDiff(1, 0));
            movements.add(new PositionDiff(1, 1));
        } else {
            movements.add(new PositionDiff(-1, -1));
            movements.add(new PositionDiff(-1, 0));
            movements.add(new PositionDiff(-1, 1));
        }

        for (var movement : movements) {
            var nextPosition = piece.getPosition().add(movement);

            if (nextPosition.isOnBoard()) {
                var nextPossiblePiece = board.getPieceOnPosition(nextPosition);
                var secondNextPosition = nextPosition.add(movement);

                if (secondNextPosition.isOnBoard()
                        && !board.getPieceOnPosition(secondNextPosition).isPresent()
                        && nextPossiblePiece.isPresent()
                        && nextPossiblePiece.get().getKind() != piece.getKind()) {
                    var move = new Move(
                            true,
                            PieceMove.Move(piece, secondNextPosition),
                            PieceMove.Discard(nextPossiblePiece.get()));

                    if (secondNextPosition.getRow() == ((piece.getKind() == PieceKind.BLACK) ? 0 : 8))
                        move.addPieceMove(PieceMove.PromoteIntoQueen(secondNextPosition));

                    jumps.add(move);
                    jumpsOnly = true;
                } else if (!jumpsOnly && !board.getPieceOnPosition(nextPosition).isPresent()) {
                    var move = new Move(PieceMove.Move(piece, nextPosition));
                    if (nextPosition.getRow() == ((piece.getKind() == PieceKind.BLACK) ? 0 : 8))
                        move.addPieceMove(PieceMove.PromoteIntoQueen(nextPosition));

                    moves.add(move);
                }
            }
        }

        if (jumps.isEmpty())
            return moves;
        else
            return jumps;
    }

    public Set<Move> getAllPossibleMoves(PieceKind player) {
        var allMoves = new HashSet<Move>();

        board.getPieces().stream().filter(piece -> piece.getKind() == player)
                .forEach(piece -> allMoves.addAll(getPossibleMovesForPiece(piece)));

        var moves = new HashSet<Move>();
        var jumps = new HashSet<Move>();

        for (var move : allMoves) {
            if (move.isJumping())
                jumps.add(move);
            else
                moves.add(move);
        }

        if (jumps.isEmpty())
            return moves;
        else
            return jumps;
    }
}
