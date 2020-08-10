package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import ui.PieceView;

public class PieceClickHandler implements EventHandler<MouseEvent> {
    final private ViewState state;
    final private PieceView pieceView;

    public PieceClickHandler(ViewState state, PieceView pieceView) {
        this.state = state;
        this.pieceView = pieceView;
    }

    @Override
    public void handle(MouseEvent event) {
        if (state.isNone()) {
            state.startMove(pieceView);
        } else {
            state.reset();
        }
    }
}
