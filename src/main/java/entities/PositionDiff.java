package entities;

public class PositionDiff {
    final public int Row;
    final public int Column;

    public PositionDiff(int row, int column) {
        Row = row;
        Column = column;
    }

    @Override
    public String toString() {
        return "Diff(" + Row + ", " + Column + ")";
    }
}
