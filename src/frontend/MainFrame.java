package frontend;

import backend.Layers;
import backend.Layer;
import javafx.scene.layout.VBox;

public class MainFrame extends VBox {

    public MainFrame(Layers canvasState) {
        getChildren().add(new AppMenuBar());
        StatusPane statusPane = new StatusPane();
        getChildren().add(new PaintPane(canvasState, statusPane));
        getChildren().add(statusPane);
    }

}
