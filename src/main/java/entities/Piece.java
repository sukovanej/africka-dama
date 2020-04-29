package entities;

import java.util.HashSet;
import java.util.Set;

public class Piece {
    private Position position;
    private PieceKind kind;
    private boolean isQueen;

    // queen support

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

    public void makeQueen() {
        isQueen = true;
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
}
