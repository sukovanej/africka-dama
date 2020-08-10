package entities;

import java.util.Objects;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public static Position fromBoardPosition(char column, int row) {
        return new Position( row - 1, column - 'a');
    }

    public PositionDiff diff(Position right) {
        return new PositionDiff(right.getRow() - getRow(), right.getColumn() - getColumn());
    }

    public Position add(PositionDiff diff) {
        return new Position(diff.Row + getRow(), diff.Column + getColumn());
    }

    public void addInPlace(PositionDiff diff) {
        setColumn(diff.Column + getColumn());
        setRow(diff.Row + getRow());
    }

    public boolean isOnBoard() {
        return getRow() >= 0 && getRow() < 9 && getColumn() >= 0 && getColumn() < 9;
    }

    @Override
    public String toString() {
        return "[" + (char) (getColumn() + 'a') + ", " + (int) (getRow() + 1) + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
