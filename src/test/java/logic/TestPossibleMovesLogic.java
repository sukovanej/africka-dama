package logic;

import entities.*;
import factories.BoardFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestPossibleMovesLogic {
    @Test
    public void testGetPossibleMovesForPieceSimpleCase() {
        var board = BoardFactory.initializeBoard();
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 5).get();

        var possibleMoves = logic.getPossibleMovesForPiece(testingPiece);
        var expected = new HashSet<>(Collections.singletonList(new Move(
                new PieceMove(testingPiece, Position.fromBoardPosition('e', 5)))));

        assertEquals(expected, possibleMoves);
    }

    @Test
    public void testPossibleMovesLogicWithJump() {
        var pieces = new HashSet<Piece>(Arrays.asList(
                new Piece(Position.fromBoardPosition('f', 5), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('e', 6), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('d', 7), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('f', 6), PieceKind.BLACK)));
        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 5).get();

        var expected = new HashSet<>(Collections.singletonList(new Move(
                new PieceMove(testingPiece, Position.fromBoardPosition('f', 7)),
                new PieceMove(board.getPieceFromBoardPosition('f', 6).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testPossibleMovesLogicWithJumpFromTheBlackSide() {
        var pieces = new HashSet<Piece>(Arrays.asList(
                new Piece(Position.fromBoardPosition('f', 5), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('e', 6), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('d', 7), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('f', 6), PieceKind.BLACK)));
        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('e', 6).get();

        var expected = new HashSet<>(Collections.singletonList(new Move(
                new PieceMove(testingPiece, Position.fromBoardPosition('g', 4)),
                new PieceMove(board.getPieceFromBoardPosition('f', 5).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testPossibleMovesLogicWithTwoPossibleJumps() {
        var pieces = new HashSet<>(Arrays.asList(
                new Piece(Position.fromBoardPosition('f', 5), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('e', 6), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('f', 6), PieceKind.BLACK)));
        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 5).get();

        var expected = new HashSet<>(Arrays.asList(
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('d', 7)),
                        new PieceMove(board.getPieceFromBoardPosition('e', 6).get())),
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('f', 7)),
                        new PieceMove(board.getPieceFromBoardPosition('f', 6).get()))
        ));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testPossibleMovesLogicWithJumpAnotherCase() {
        var pieces = new HashSet<>(Arrays.asList(
                new Piece(Position.fromBoardPosition('f', 3), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('f', 4), PieceKind.BLACK)));
        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 3).get();

        var expected = new HashSet<>(Collections.singletonList(
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('f', 5)),
                        new PieceMove(board.getPieceFromBoardPosition('f', 4).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testPossibleMovesLogicWithConsecutiveJumps() {
        var pieces = new HashSet<>(Arrays.asList(
                new Piece(Position.fromBoardPosition('f', 3), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('f', 4), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('f', 6), PieceKind.BLACK)));
        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 3).get();

        var expected = new HashSet<>(Collections.singletonList(
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('f', 7)),
                        new PieceMove(board.getPieceFromBoardPosition('f', 4).get()),
                        new PieceMove(board.getPieceFromBoardPosition('f', 6).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testPossibleMovesLogicWithConsecutiveJumpsTwoLeafs() {
        var pieces = new HashSet<>(Arrays.asList(
                new Piece(Position.fromBoardPosition('f', 3), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('f', 4), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('e', 6), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('g', 6), PieceKind.BLACK)));
        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 3).get();

        var expected = new HashSet<>(Arrays.asList(
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('d', 7)),
                        new PieceMove(board.getPieceFromBoardPosition('f', 4).get()),
                        new PieceMove(board.getPieceFromBoardPosition('e', 6).get())),
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('h', 7)),
                        new PieceMove(board.getPieceFromBoardPosition('f', 4).get()),
                        new PieceMove(board.getPieceFromBoardPosition('g', 6).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testPossibleMovesLogicWithConsecutiveJumpsThreeLeafs() {
        var pieces = new HashSet<>(Arrays.asList(
                new Piece(Position.fromBoardPosition('f', 3), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('f', 4), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('e', 6), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('g', 6), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('c', 8), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('e', 8), PieceKind.BLACK)));
        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 3).get();

        var expected = new HashSet<>(Arrays.asList(
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('h', 7)),
                        new PieceMove(board.getPieceFromBoardPosition('f', 4).get()),
                        new PieceMove(board.getPieceFromBoardPosition('g', 6).get())),
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('b', 9)),
                        new PieceMove(board.getPieceFromBoardPosition('f', 4).get()),
                        new PieceMove(board.getPieceFromBoardPosition('c', 8).get()),
                        new PieceMove(board.getPieceFromBoardPosition('e', 6).get())),
                new Move(
                        new PieceMove(testingPiece, Position.fromBoardPosition('f', 9)),
                        new PieceMove(board.getPieceFromBoardPosition('f', 4).get()),
                        new PieceMove(board.getPieceFromBoardPosition('e', 8).get()),
                        new PieceMove(board.getPieceFromBoardPosition('e', 6).get()))));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }
}
