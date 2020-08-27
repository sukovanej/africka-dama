package logic;

import entities.*;
import factories.BoardFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class TestPossibleMovesLogic {
    @Test
    public void testGetPossibleMovesForPieceSimpleCase() {
        var board = BoardFactory.initializeBoard();
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 5).get();

        var possibleMoves = logic.getPossibleMovesForPiece(testingPiece);
        var expected = new HashSet<>(Collections.singletonList(new Move(
                PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 5)))));

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
                PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 7)),
                PieceMove.Discard(board.getPieceFromBoardPosition('f', 6).get()))));

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
        var testingPiece = board.getPieceFromBoardPosition('f', 5).get();

        var expected = new HashSet<>(Collections.singletonList(new Move(
                PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 7)),
                PieceMove.Discard(board.getPieceFromBoardPosition('f', 6).get()))));

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
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 7)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('e', 6).get())),
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 7)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('f', 6).get()))
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
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 5)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('f', 4).get()))));

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

        var move = new Move(
                PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 5)),
                PieceMove.Discard(board.getPieceFromBoardPosition('f', 4).get()));
        var expected = new HashSet<>(Collections.singletonList(move));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
        board.playMove(move);
        testingPiece = board.getPieceFromBoardPosition('f', 5).get();
        move = new Move(
                PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 7)),
                PieceMove.Discard(board.getPieceFromBoardPosition('f', 6).get()));
        expected = new HashSet<>(Collections.singletonList(move));

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testPossibleMovesLogicWithConsecutiveJumpsTwoLeafs() {
        var pieces = new HashSet<>(Arrays.asList(
                new Piece(Position.fromBoardPosition('f', 5), PieceKind.WHITE),
                new Piece(Position.fromBoardPosition('e', 6), PieceKind.BLACK),
                new Piece(Position.fromBoardPosition('g', 6), PieceKind.BLACK)));
        var board = new Board(pieces);
        var logic = new PossibleMovesLogic(board);
        var testingPiece = board.getPieceFromBoardPosition('f', 5).get();

        var expected = new HashSet<>(Arrays.asList(
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('d', 7)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('e', 6).get())),
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('h', 7)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('g', 6).get()))));

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

        var expected = new HashSet<>(Collections.singletonList(
                new Move(
                        PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 5)),
                        PieceMove.Discard(board.getPieceFromBoardPosition('f', 4).get()))));

        // TODO: add more tests

        assertEquals(expected, logic.getPossibleMovesForPiece(testingPiece));
    }

    @Test
    public void testGetPossibleMovesForPieceSimpleCaseMultipleMoves() {
        var board = BoardFactory.initializeBoard();
        var logic = new PossibleMovesLogic(board);

        var testingPiece = board.getPieceFromBoardPosition('f', 5).get();
        var possibleMoves = logic.getPossibleMovesForPiece(testingPiece);
        var move = new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 5)));
        var expected = new HashSet<>(Collections.singletonList(move));
        assertEquals(expected, possibleMoves);

        board.playMove(move);

        testingPiece = board.getPieceFromBoardPosition('d', 5).get();
        possibleMoves = logic.getPossibleMovesForPiece(testingPiece);
        move = new Move(
                PieceMove.Move(testingPiece, Position.fromBoardPosition('f', 5)),
                PieceMove.Discard(board.getPieceFromBoardPosition('e', 5).get()));
        expected = new HashSet<>(Collections.singletonList(move));

        assertEquals(expected, possibleMoves);
    }

    @Test
    public void testGetPossibleMovesForPieceSimpleCaseMultipleMovesSecondCase() {
        var board = BoardFactory.initializeBoard();
        var logic = new PossibleMovesLogic(board);

        var testingPiece = board.getPieceFromBoardPosition('e', 4).get();
        var possibleMoves = logic.getPossibleMovesForPiece(testingPiece);
        var move = new Move(PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 5)));
        var expected = new HashSet<>(Collections.singletonList(move));
        assertEquals(expected, possibleMoves);

        board.playMove(move);

        testingPiece = board.getPieceFromBoardPosition('e', 6).get();
        possibleMoves = logic.getPossibleMovesForPiece(testingPiece);
        move = new Move(
                PieceMove.Move(testingPiece, Position.fromBoardPosition('e', 4)),
                PieceMove.Discard(board.getPieceFromBoardPosition('e', 5).get()));
        expected = new HashSet<>(Collections.singletonList(move));
        assertEquals(expected, possibleMoves);
    }

    @Test
    public void testStartOfTheGame() {
        var board = BoardFactory.initializeBoard();
        var logic = new PossibleMovesLogic(board);

        var possibleMoves = logic.getAllPossibleMoves(PieceKind.WHITE);
        assertEquals(possibleMoves.size(), 4);
        System.out.println(board);


        board.playMove(
                new Move(
                        PieceMove.Move(
                                board.getPieceFromBoardPosition('e', 4).get(),
                                Position.fromBoardPosition('e', 5))));
        System.out.println(board);

        possibleMoves = logic.getAllPossibleMoves(PieceKind.BLACK);
        assertEquals(possibleMoves.size(), 2);

        var move = new Move(
                PieceMove.Move(
                        board.getPieceFromBoardPosition('e', 6).get(),
                        Position.fromBoardPosition('e', 4)),
                PieceMove.Discard(
                        board.getPieceFromBoardPosition('e', 5).get()));
        board.playMove(move);

        System.out.println(board);

        possibleMoves = logic.getAllPossibleMoves(PieceKind.WHITE);
        assertEquals(possibleMoves.size(), 2);
    }
}
