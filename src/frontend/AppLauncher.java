package frontend;

import backend.Layers;
import backend.Layer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


//Cuando esta oculta la capa tire warning si se quiere dibujar.
public class AppLauncher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Layers canvasState = new Layers(); // BackEnd
		MainFrame frame = new MainFrame(canvasState);
		Scene scene = new Scene(frame);
		primaryStage.setResizable(false); 
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(event -> System.exit(0));
	}

}
