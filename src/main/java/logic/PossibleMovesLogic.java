package logic;

import entities.*;

import java.util.*;

public class PossibleMovesLogic {
    final private Board board;

    public PossibleMovesLogic(Board board) {
        this.board = board;
    }

    public Set<Position> getQueenPossibleMoves(Piece piece, Position position) {
        var positions = new HashSet<Position>();

        var movements = Arrays.asList(
                new PositionDiff(1 ,-1),
                new PositionDiff(1 ,0),
                new PositionDiff(1 ,1),
                new PositionDiff(0 ,-1),
                new PositionDiff(0 ,1),
                new PositionDiff(-1 ,-1),
                new PositionDiff(-1 ,0),
                new PositionDiff(-1 ,1)
        );

        for (var movement: movements) {
            var nextPosition = position.add(movement);

            while (nextPosition.isOnBoard() && !board.getPieceOnPosition(nextPosition).isPresent()) {
                positions.add(nextPosition);
                nextPosition.addInPlace(movement);
            }
        }

        return positions;
    }

    public Set<Position> getPossibleMoves(Piece piece, Position position) {
        if (piece.isQueen()) {
            return getQueenPossibleMoves(piece, position);
        }

        var topThreshold = 8;
        var movement = 1;

        if (piece.getKind() == PieceKind.BLACK) {
            topThreshold = 0;
            movement = - 1;
        }

        var positions = new HashSet<Position>();

        if (position.getRow() != topThreshold) {
            positions.add(new Position(position.getColumn(), position.getRow() + movement));

            if (position.getColumn() > 0) {
                positions.add(new Position(position.getColumn() - 1, position.getRow() + movement));
            }

            if (position.getColumn() < 8) {
                positions.add(new Position(position.getColumn() + 1, position.getRow() + movement));
            }
        }

        if (position.getColumn() > 0) {
            positions.add(new Position(position.getColumn() - 1, position.getRow()));
        }

        if (position.getColumn() < 8) {
            positions.add(new Position(position.getColumn() + 1, position.getRow()));
        }

        return positions;
    }

    public Set<Move> getJumpingMoves(Piece piece, Position piecePosition) {
        var moves = new HashSet<Move>();

        for (var position : getPossibleMoves(piece, piecePosition)) {
            var nextPossiblePiece = board.getPieceOnPosition(position);

            if (nextPossiblePiece.isPresent()) {
                var nextPiece = nextPossiblePiece.get();
                if (nextPossiblePiece.get().getKind() == piece.getKind()) continue;

                var secondNextPosition = position.add(piecePosition.diff(position));
                var secondNextPiece = board.getPieceOnPosition(secondNextPosition);

                if (secondNextPiece.isPresent()) continue;

                var otherJumpingMoves = getJumpingMoves(piece, secondNextPosition);

                if (otherJumpingMoves.isEmpty()) {
                    var move = new Move(
                            new PieceMove(piece, secondNextPosition),
                            new PieceMove(nextPiece));
                    moves.add(move);
                } else {
                    for (var nextJumping : otherJumpingMoves) {
                        var innerMoves = nextJumping.getMoves();
                        innerMoves.add(new PieceMove(nextPiece));
                        moves.add(new Move(innerMoves));
                    }
                }
            }
        }

        return moves;
    }

    public Set<Move> getPossibleMovesForPiece(Piece piece) {
        var moves = getJumpingMoves(piece, piece.getPosition());

        if (!moves.isEmpty()) {
            return moves;
        }

        for (var position : getPossibleMoves(piece, piece.getPosition())) {
            if (!board.getPieceOnPosition(position).isPresent())
                moves.add(new Move(new PieceMove(piece, position)));
        }

        return moves;
    }
}
