package controller;

import entities.Piece;

import java.util.function.Consumer;
import java.util.function.Function;

enum ViewStateEnum {
    NONE,
    START_MOVE
}

public class ViewState {
    private ViewStateEnum state;

    public Consumer<Piece> startMoveRunnable;

    public ViewState() {
        state = ViewStateEnum.NONE;
    }

    public boolean isNone() {
        return state == ViewStateEnum.NONE;
    }

    public void startMove(Piece piece) {
        state = ViewStateEnum.START_MOVE;
        startMoveRunnable.accept(piece);
    }
}
