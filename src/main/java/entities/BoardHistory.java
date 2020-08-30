package entities;

import logic.PossibleMovesLogic;

import java.util.*;

public class BoardHistory {
    private final Board board;
    private final List<Move> movesHistory;
    private int pointer;

    public BoardHistory(Board board) {
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
        System.out.println("[History:Next] Playing " + movesHistory.get(pointer));
    }

    public void previous() {
        if (pointer == -1) return;
        board.undoMove(movesHistory.get(pointer));
        pointer--;
    }

    public PieceKind getCurrentPlayer() {
        if (pointer == -1)
            return PieceKind.WHITE;

        if (pointer < movesHistory.size() - 1) {
            return movesHistory.get(pointer + 1).getPlayer();
        }

        var lastMove = movesHistory.get(pointer);
        var lastMovePiece = lastMove.getPiece();

        if (lastMove.isJumping()) {
            var logic = new PossibleMovesLogic(board);
            var possibleMovesPreviousPiece = logic.getPossibleMovesForPiece(lastMovePiece);
            var possibleJumpingMove = possibleMovesPreviousPiece.stream().filter(Move::isJumping).findAny();

            if (possibleJumpingMove.isPresent()) {
                return lastMovePiece.getKind();
            }
        }

        return lastMovePiece.getKind() == PieceKind.BLACK ? PieceKind.WHITE : PieceKind.BLACK;
    }

    public Optional<Move> getPreviousMove() {
        if (pointer <= 0) return Optional.empty();
        return Optional.of(movesHistory.get(pointer));
    }

    public String serialize() {
        var joiner = new StringJoiner("\n");

        for (var moves : movesHistory)
            joiner.add(moves.serialize());

        return joiner.toString();
    }

    public void load(Scanner scanner) {
        // reset game
        while (pointer > -1) previous();

        while (scanner.hasNextLine()) {
            var moveSerialized = scanner.nextLine();
            var move = Move.deserialize(moveSerialized, board);
            board.playMove(move);
            movePlayed(move);
        }
    }
}
