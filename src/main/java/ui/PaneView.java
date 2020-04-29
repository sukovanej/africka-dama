package ui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PaneView {
    final private Rectangle pane;
    final private Scene scene;
    final private Integer row;
    final private Integer column;

    public PaneView(Scene scene, int row, int column) {
        this.scene = scene;
        this.row = row;
        this.column = column;

        this.pane = createView();
    }

    public void startRender() {
        var group = (Group)scene.getRoot();
        group.getChildren().add(pane);
    }

    private Rectangle createView() {
        var size = scene.getWidth() / 9;
        var pane = new Rectangle();

        pane.setX(size * column);
        pane.setY(size * row);
        pane.setWidth(size);
        pane.setHeight(size);
        pane.setFill(((row + column) % 2 == 0) ? Color.DARKGRAY : Color.BROWN);
        pane.setStroke(Color.BLACK);

        return pane;
    }
}
