package controller;

import entities.Position;
import ui.PieceView;

import java.util.function.Consumer;

enum ViewStateEnum {
    NONE,
    START_MOVE
}

public class ViewState {
    private ViewStateEnum state;

    public Consumer<PieceView> startMoveCallable;
    public Runnable resetViewCallable;
    public Consumer<Position> makeMoveCallable;

    public ViewState() {
        state = ViewStateEnum.NONE;
    }

    public boolean isNone() {
        return state == ViewStateEnum.NONE;
    }

    public boolean isStartMove() {
        return state == ViewStateEnum.START_MOVE;
    }

    public void reset() {
        resetViewCallable.run();
        state = ViewStateEnum.NONE;
    }

    public void startMove(PieceView piece) {
        state = ViewStateEnum.START_MOVE;
        startMoveCallable.accept(piece);
    }

    public void makeMove(Position position) {
        state = ViewStateEnum.NONE;
        makeMoveCallable.accept(position);
    }
}
