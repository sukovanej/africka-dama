package entities;

public class Piece {
    private Position position;
    private PieceKind kind;
    private boolean isQueen;

    public Piece(Position position, PieceKind kind) {
        this.position = position;
        this.kind = kind;
        this.isQueen = false;
    }

    public Piece(Position position, PieceKind kind, boolean isQueen) {
        this.position = position;
        this.kind = kind;
        this.isQueen = isQueen;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public void promoteIntoQueen() {
        isQueen = true;
    }

    public void queenToPiece() {
        isQueen = false;
    }

    public PieceKind getKind() {
        return kind;
    }

    public void setKind(PieceKind kind) {
        this.kind = kind;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Piece{position=" + position + ", kind=" + kind + "}";
    }

    public Piece copy() {
        return new Piece(position, kind);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return isQueen == piece.isQueen() &&
                position.equals(piece.getPosition()) &&
                kind == piece.kind;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    public void unpromote() {
        isQueen = false;
    }
}
