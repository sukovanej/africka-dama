package entities;

import java.util.*;

public class Move {
    final private Set<PieceMove> moves;
    final private boolean isJumping;

    public Move(Set<PieceMove> moves) {
        this.moves = moves;
        this.isJumping = false;
    }

    public Move(Set<PieceMove> moves, boolean isJumping) {
        this.moves = moves;
        this.isJumping = isJumping;
    }

    public Move(PieceMove... moves) {
        this.moves = new HashSet<>(Arrays.asList(moves));
        this.isJumping = false;
    }

    public Move(boolean isJumping, PieceMove... moves) {
        this.moves = new HashSet<>(Arrays.asList(moves));
        this.isJumping = isJumping;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void addPieceMove(PieceMove pieceMove) {
        moves.add(pieceMove);
    }

    public Set<PieceMove> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return "Move{" + "moves=" + moves + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return moves.equals(move.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moves);
    }

    public PieceKind getPlayer() {
        return getPiece().getKind();
    }

    public PieceMove getPieceMove() {
        // TODO: optimize by saving the piece, is it worth?
        return moves.stream().filter(move -> move.getMoveKind() == PieceMoveKind.MOVE).findFirst().get();
    }

    public Piece getPiece() {
        return getPieceMove().getPiece();
    }

    public Move convertToOtherBoard(Board board) {
        var moves = new Move();
        for (var move : this.moves) {
            moves.addPieceMove(move.convertToOtherBoard(board));
        }
        return moves;
    }

    public String serialize() {
        var joiner = new StringJoiner(";");

        for (var move : moves) {
            joiner.add(move.serialize());
        }

        return joiner.toString();
    }

    public static Move deserialize(String movesSerialized, Board board) {
        var pieceMovesSerialized = movesSerialized.split(";");
        var move = new Move();

        for (var pieceMoveSerialized : pieceMovesSerialized)
            move.addPieceMove(PieceMove.deserialize(pieceMoveSerialized, board));

        return move;
    }

    public boolean isQueenPromotion() {
        return moves.stream().anyMatch(m -> m.getMoveKind() == PieceMoveKind.PROMOTE_INTO_QUEEN);
    }
}
