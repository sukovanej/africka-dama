package logic;

import entities.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestPossibleMovesLogicWithQueen {
    @Test
    public void testGetPossibleMovesForPieceSimpleCasePromoteToQueen() {
        var pieces = new HashSet<Piece>(Collections.singletonList(
                new Piece(Position.fromBoardPosition('e', 8), PieceKind.WHITE)));

        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('e', 8).get();

        var expected = new HashSet<>(List.of(
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 8))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 8))),
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 9)),
                        PieceMove.PromoteIntoQueen(testingPiece)),
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 9)),
                        PieceMove.PromoteIntoQueen(testingPiece)),
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 9)),
                        PieceMove.PromoteIntoQueen(testingPiece))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testGetPossibleMovesForPieceSimpleCase() {
        var pieces = new HashSet<Piece>(Arrays.asList(
                new Piece(Position.fromBoardPosition('b', 1), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('g', 6), PieceKind.BLACK)));

        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('b', 1).get();

        var expected = new HashSet<>(List.of(
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('h', 7)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('g', 6).get())),
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('i', 8)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('g', 6).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testGetPossibleMovesForQueenSimpleCase() {
        var pieces = new HashSet<Piece>(List.of(
                new Piece(Position.fromBoardPosition('d', 9), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('c', 5), PieceKind.WHITE, true)));

        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('d', 9).get();

        var expected = new HashSet<>(List.of(
                // diagonals moves
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('c', 8))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('b', 7))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('a', 6))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 8))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 7))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('g', 6))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('h', 5))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('i', 4))),
                // column moves
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 8))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 7))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 6))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 5))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 4))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 3))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 2))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 1))),
                // row moves
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('a', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('b', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('c', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('g', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('h', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('i', 9)))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testGetPossibleMovesForQueenSimpleCaseWithoutJump() {
        var pieces = new HashSet<Piece>(List.of(
                new Piece(Position.fromBoardPosition('d', 9), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('d', 5), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('d', 4), PieceKind.WHITE, true)));

        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('d', 9).get();

        var expected = new HashSet<>(List.of(
                // diagonals moves
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('c', 8))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('b', 7))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('a', 6))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 8))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 7))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('g', 6))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('h', 5))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('i', 4))),
                // column moves
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 8))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 7))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 6))),
                // row moves
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('a', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('b', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('c', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('g', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('h', 9))),
                new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('i', 9)))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testGetPossibleMovesQueenJumpWithSinglePossibility() {
        var pieces = new HashSet<Piece>(List.of(
                new Piece(Position.fromBoardPosition('e', 1), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('g', 1), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('i', 1), PieceKind.BLACK)));

        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('e', 1).get();

        var expected = new HashSet<>(Collections.singletonList(
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('h', 1)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('g', 1).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }


    @Test
    public void testGetPossibleMovesQueenJumpWithTwoPossibilities() {
        var pieces = new HashSet<Piece>(List.of(
                new Piece(Position.fromBoardPosition('e', 1), PieceKind.WHITE, true),
                new Piece(Position.fromBoardPosition('f', 1), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('i', 1), PieceKind.BLACK)));

        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('e', 1).get();

        var expected = new HashSet<>(List.of(
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('g', 1)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('f', 1).get())),
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('h', 1)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('f', 1).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }
}
