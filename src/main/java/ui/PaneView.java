package ui;

import entities.Position;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PaneView {
    private final Pane root;
    private final Position position;
    private final Rectangle pane;

    public PaneView(Pane root, Position position) {
        this.root = root;
        this.position = position;
        this.pane = new Rectangle();

        initializeView();
    }

    public void startRender() {
        root.getChildren().add(pane);
    }

    private void initializeView() {
        resetView();
    }

    public void resetView() {
        var size = Math.min(root.getWidth() / 9, root.getHeight() / 9);

        pane.setX(size * (position.getColumn()));
        pane.setY(size * (8 - position.getRow()));
        pane.setWidth(size);
        pane.setHeight(size);
        pane.setStrokeWidth(1);
        pane.setFill(((position.getRow() + position.getColumn()) % 2 == 0) ? Color.DARKGRAY : Color.BROWN);
        pane.setStroke(Color.BLACK);
    }

    public void highlight() {
        pane.setFill(Color.HOTPINK);
    }

    public void highlightPossibles() {
        pane.setFill(Color.LIGHTPINK);
    }

    public Rectangle getView() {
        return pane;
    }

    public Position getPosition() {
        return position;
    }
}
