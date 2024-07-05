package frontend;

import backend.Layers;
import backend.Layer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


//Cuando esta oculta la capa tire warning si se quiere dibujar.
//La elipse se dibuja mal.
//Etiquetas las de arriba primero.
//Cuando toco una figura se tiene, poner las propiedades de esa misma.
//Fire anda mal, cuando se agrega una capa hay quilombos con el show.
//Se tiene que poder seleccionar la figura sin importar la capa.
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
