package controller;

import entities.Position;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import ui.PieceView;

public class PaneClickHandler implements EventHandler<MouseEvent> {
    private final ViewState state;
    private final Position position;

    public PaneClickHandler(ViewState state, Position position) {
        this.state = state;
        this.position = position;
    }

    @Override
    public void handle(MouseEvent event) {
        if (state.isStartMove()) {
            state.makeMove(position);
            state.reset();
        } else {
            System.out.println(position);
            state.reset();
        }
    }
}
