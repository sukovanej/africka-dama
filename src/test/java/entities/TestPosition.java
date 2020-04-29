package entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestPosition {
    @Test
    public void testGetTestPosition() {
        var position = new Position(0, 0);
        var expected = Position.fromBoardPosition('a', 1);

        assertEquals(expected, position);
    }

    @Test
    public void testPositionTransition() {
        var position1 = Position.fromBoardPosition('a', 1);
        var position2 = Position.fromBoardPosition('b', 2);

        var expected = Position.fromBoardPosition('c', 3);
        var position = position2.add(position1.diff(position2));

        assertEquals(expected, position);
    }

    @Test
    public void testPositionTransitionSecondCase() {
        var position1 = Position.fromBoardPosition('f', 5);
        var position2 = Position.fromBoardPosition('e', 6);

        var diff = position1.diff(position2);
        var expected = Position.fromBoardPosition('d', 7);
        var position = position2.add(diff);

        assertEquals(expected, position);
    }
}
