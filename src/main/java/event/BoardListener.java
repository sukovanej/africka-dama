package event;

import entities.Piece;

public interface BoardListener {
    void pieceMoved(Piece piece);

    void pieceRemoved(Piece piece);

    void promoteIntoQueen(Piece piece);
}
