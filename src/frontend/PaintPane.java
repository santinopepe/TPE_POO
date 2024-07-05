package frontend;

import javafx.scene.control.Alert.AlertType;
import backend.*;
import backend.model.*;
import frontend.Buttons.*;
import frontend.DrawFigures.*;
import frontend.DrawFigures.DrawFigure;
import frontend.DrawFigures.DrawRect;
import frontend.Exceptions.HiddenLayerException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import backend.Layer;
import java.util.*;


public class PaintPane extends BorderPane {

    // Canvas y relacionados
	private final Canvas canvas = new Canvas(800, 600);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();
	private final Color lineColor = Color.BLACK;
	private final Color defaultFillColor = Color.YELLOW;

	// Botones Barra Izquierda
	private final ToggleButton selectionButton = new ToggleButton("Seleccionar");


	private final CustomButton rectangleButton = new RectangleButton("Rectángulo");
	private final CustomButton circleButton = new CircleButton("Círculo");
	private final CustomButton squareButton = new SquareButton("Cuadrado");
	private final CustomButton ellipseButton = new EllipseButton("Elipse");


	private final ToggleButton deleteButton = new ToggleButton("Borrar");
	private final ToggleButton duplicateButton = new ToggleButton("Duplicar");
	private final ToggleButton divideButton = new ToggleButton("Dividir");
	private final ToggleButton moveButton = new ToggleButton("Mov. Centro");
	private final ToggleButton addLayer = new ToggleButton("Agregar capa");
	private final ToggleButton removeLayer = new ToggleButton("Eliminar capa");
	private final RadioButton showLayer = new RadioButton("Mostrar");
	private final RadioButton hideLayer = new RadioButton("Ocultar");

	// Selector de color de relleno
	private final ColorPicker fillColorPicker = new ColorPicker(defaultFillColor);

	//Selector del color secundario.
	private final ColorPicker fillSecondaryColorPicker = new ColorPicker(Color.AQUA);

	//slider para el borde
	private final Slider borderSlider = new Slider(0,10, 0);

	// Dibujar una figura
	private Point startPoint;

	private Layer currentLayer;

	// Seleccionar una figura
	private Figure selectedFigure;

	//seleccionar un tipo de sombra
	private ShadowType shadow = ShadowType.NONE;

	//seleccionar un tipo de borde
	private EdgeType edge = EdgeType.NORMAL;

	private int cantLayer=0;
	// Colores de relleno de cada figura

	private final Map<Figure, DrawFigure> figurePropertiesMap = new HashMap<>();

	private final Map<Figure,CustomButton> figureButtonMap = new HashMap<>();

	private final SortedMap<Integer, Layer> layerFigureMap = new TreeMap<>();

	private final ChoiceBox<ShadowType> shadowsBox = new ChoiceBox<>();
	private final ChoiceBox<EdgeType> edgeBox = new ChoiceBox<>();
	private final ChoiceBox<Layer> layerBox = new ChoiceBox<>();

	public PaintPane(Layers canvasState, StatusPane statusPane) {
 		Label shadowLable = new Label("Sombras");
		Label borderLable = new Label("Borde");
		Label fillingLable = new Label("Relleno");
		Label actionLable = new Label("Acciones");


		shadowsBox.getItems().addAll(ShadowType.NONE, ShadowType.SIMPLE, ShadowType.COLOURED, ShadowType.SIMPLE_INVERSED, ShadowType.COLOURED_INVERSED);
		edgeBox.getItems().addAll(EdgeType.NORMAL,EdgeType.SIMPLE_DOTTED, EdgeType.COMPLEX_DOTTED);

		shadowsBox.setValue(ShadowType.NONE);
		edgeBox.setValue(EdgeType.NORMAL);

		borderSlider.setMin(0);
		borderSlider.setMax(10);
		borderSlider.setValue(5); // Valor inicial
		borderSlider.setShowTickLabels(true);

		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton};
		CustomButton[] customButtons = {rectangleButton, circleButton, squareButton, ellipseButton};

		ToggleGroup tools = new ToggleGroup();

		//Barra vertical
		setButtons(toolsArr,tools);

		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.getChildren().add(shadowLable);
		buttonsBox.getChildren().add(shadowsBox);
		buttonsBox.getChildren().add(fillingLable);
		buttonsBox.getChildren().add(fillColorPicker);
		buttonsBox.getChildren().add(fillSecondaryColorPicker);
		buttonsBox.getChildren().add(borderLable);
		buttonsBox.getChildren().add(edgeBox);
		buttonsBox.getChildren().add(borderSlider);
		buttonsBox.getChildren().add(actionLable);


		ToggleButton[] actionsArray = {duplicateButton, divideButton, moveButton};
		ToggleGroup arrTools = new ToggleGroup();
		setButtons(actionsArray,arrTools);
		buttonsBox.getChildren().addAll(actionsArray);

		ToggleButton[] layersArr = {showLayer,hideLayer,addLayer,removeLayer};
		ToggleGroup arrLayers = new ToggleGroup();
		setButtons(layersArr,arrLayers);

		//Seteamos que inicialmente el boton de mostrar capa este seleccionado.
		showLayer.fire();

		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		//Barra Horizontal
		HBox horizontalBox = new HBox(10);
		horizontalBox.getChildren().add(layerBox);
		horizontalBox.getChildren().addAll(layersArr);
		horizontalBox.setAlignment(Pos.BASELINE_CENTER);

		horizontalBox.setPadding(new Insets(5));
		horizontalBox.setStyle("-fx-background-color: #999");
		horizontalBox.setPrefHeight(50);
		setTop(horizontalBox);

		for (int i = 0; i < 3; i++) {
			layerFigureMap.put(cantLayer, new Layer(cantLayer++));
			layerFigureMap.get(i).cannotEliminate();
			layerBox.getItems().add(layerFigureMap.get(i));
		}

		layerBox.setValue(layerFigureMap.get(0));
		currentLayer = layerFigureMap.get(0);

		fillColorPicker.setOnAction(event -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				DrawFigure df = figurePropertiesMap.get(selectedFigure);
				df.getFigureProperties().setColor(fillColorPicker.getValue());
				redrawCanvas();
			}
		});

		fillSecondaryColorPicker.setOnAction(event -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				DrawFigure df = figurePropertiesMap.get(selectedFigure);
				df.getFigureProperties().setSecondaryColor(fillSecondaryColorPicker.getValue());
				redrawCanvas();
			}
		});

		borderSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				DrawFigure df = figurePropertiesMap.get(selectedFigure);
				df.getFigureProperties().setWidth(borderSlider.getValue());
				redrawCanvas();
			}
		});

		shadowsBox.setOnAction(event -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				DrawFigure df = figurePropertiesMap.get(selectedFigure);
				df.getFigureProperties().setShadowType(shadowsBox.getValue());
				redrawCanvas();
			}
		});

		edgeBox.setOnAction(event -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				DrawFigure df = figurePropertiesMap.get(selectedFigure);
				df.getFigureProperties().setEdge(edgeBox.getValue());
				redrawCanvas();
			}
		});


		selectionButton.setOnAction(event -> {
			if (selectionButton.isSelected()) {
				if (selectedFigure != null) {
					setPreviousProp();
					redrawCanvas();
				}
			}
		});

		addLayer.setOnAction(event -> {
			if(addLayer.isSelected()){
				layerFigureMap.put(cantLayer,new Layer(cantLayer++));
				layerBox.getItems().add(layerFigureMap.get(cantLayer-1));
				currentLayer = layerFigureMap.get(cantLayer-1);
				layerBox.setValue(currentLayer);
			}
		});

		moveButton.setOnAction(event -> {
			if (moveButton.isSelected()){
				if (selectedFigure != null) {
					selectedFigure.centerFigure(canvas.getWidth(), canvas.getHeight());
					redrawCanvas();
				}
			}
		});

		divideButton.setOnAction(event -> {
			if (selectedFigure != null && divideButton.isSelected()) {
				double midX = (selectedFigure.getPoint1().getX() + selectedFigure.getPoint2().getX()) / 2;
				double midY = (selectedFigure.getPoint1().getY() + selectedFigure.getPoint2().getY()) / 2;
				double diff = (selectedFigure.getPoint2().getY() - selectedFigure.getPoint1().getY()) / 4;

				Figure figureLeft = selectedFigure.createDividedFigure(
						new Point (selectedFigure.getPoint1().getX(),midY - diff),
						new Point(midX, selectedFigure.getPoint2().getY() - diff),
						new Point(selectedFigure.getPoint1().getX() - selectedFigure.getAxis1() / 3.93, selectedFigure.getPoint1().getY()),
						selectedFigure.getAxis1() / 2,
						selectedFigure.getAxis2() / 2
				);

				Figure figureRight = selectedFigure.createDividedFigure(
						new Point(midX, midY - diff),
						new Point (selectedFigure.getPoint2().getX(), midY + diff),
						new Point(selectedFigure.getPoint1().getX() + selectedFigure.getAxis1() / 3.93, selectedFigure.getPoint1().getY()),
						selectedFigure.getAxis1() / 2,
						selectedFigure.getAxis2() / 2
				);

				FigureProperties figureProperties = figurePropertiesMap.get(selectedFigure).getFigureProperties();

				FigureProperties leftProperties = new FigureProperties(
						figureProperties.getColor(),
						figureProperties.getShadowType(),
						figureProperties.getSecondaryColor(),
						figureProperties.getEdge(),
						figureProperties.getWidth()
				);
				FigureProperties rightProperties = new FigureProperties(
						figureProperties.getColor(),
						figureProperties.getShadowType(),
						figureProperties.getSecondaryColor(),
						figureProperties.getEdge(),
						figureProperties.getWidth()
				);

				DrawFigure dfL = figureButtonMap.get(selectedFigure).createDrawfigure(gc,leftProperties,figureLeft);
				DrawFigure dfR = figureButtonMap.get(selectedFigure).createDrawfigure(gc,rightProperties,figureRight);

				figureButtonMap.put(figureLeft,figureButtonMap.get(selectedFigure));
				figureButtonMap.put(figureRight,figureButtonMap.get(selectedFigure));


				figureButtonMap.remove(selectedFigure);
				figurePropertiesMap.remove(selectedFigure);

				figurePropertiesMap.put(figureLeft, dfL);
				figurePropertiesMap.put(figureRight, dfR);

				layerFigureMap.get(currentLayer.getLayerNum()).add(figureLeft);
				layerFigureMap.get(currentLayer.getLayerNum()).add(figureRight);
				layerFigureMap.get(currentLayer.getLayerNum()).remove(selectedFigure);

				selectedFigure=null;
				redrawCanvas();
			}
		});

		//Set on action para saber en que layer va.
		layerBox.setOnAction(event -> {
			currentLayer = layerBox.getValue();
			//Pongo en null por si habia antes un figura seleccionada
			selectedFigure = null;
			//VER SI PODEMOS HACER UN METODO QUE HAGA ESTO.
			if (currentLayer.getIsHidden()){
				hideLayer.fire();
			} else {
				showLayer.fire();
			}
			redrawCanvas();
		});

		showLayer.setOnAction(event -> {
			layerFigureMap.get(layerBox.getValue().getLayerNum()).unHide();
			redrawCanvas();
		});

		hideLayer.setOnAction(event -> {
			layerFigureMap.get(layerBox.getValue().getLayerNum()).hide();
			redrawCanvas();
		});

		deleteButton.setOnAction(event -> {
			if (selectedFigure != null) {
				layerFigureMap.get(currentLayer.getLayerNum()).remove(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});


		removeLayer.setOnAction(event -> {
			try {
				if (currentLayer.getCanEliminate() && removeLayer.isSelected()) {
					layerFigureMap.remove(currentLayer.getLayerNum());
					int size = layerBox.getItems().size();
					layerBox.getItems().remove(currentLayer);
					currentLayer = layerFigureMap.get(size - 1);

					//Agregue esto porque cuando eliminabamos aparecia en choicebox como si
					//estuviesemos en la capa q eliminamos.
					layerBox.setValue(layerFigureMap.get(0));
				}
			}catch (Exception ex){

				showAlert("La capa no existe");
			}
		});

		duplicateButton.setOnAction(event -> {
			Figure figure;
			if(selectedFigure != null && duplicateButton.isSelected()){
				figure = selectedFigure;
				FigureProperties figureProperties = figurePropertiesMap.get(figure).getFigureProperties();

				//ES MUY FEO, VER SI SE PUEDE CAMBIAR. !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

				Figure duplicateFigure = figureButtonMap.get(figure).createNewFigure(figure.getPoint1().displacePoint(),
						figure.getPoint2().displacePoint(),
						figure.getAxis1(),
						figure.getAxis2(),
						figure.getAxis1());

				FigureProperties dupProperties = new FigureProperties(
						figureProperties.getColor(),
						figureProperties.getShadowType(),
						figureProperties.getSecondaryColor(),
						figureProperties.getEdge(),
						figureProperties.getWidth()
				);
				DrawFigure dupDrawFigure = figureButtonMap.get(figure).createDrawfigure(gc,dupProperties,duplicateFigure);
				figurePropertiesMap.put(duplicateFigure, dupDrawFigure);
				layerFigureMap.get(currentLayer.getLayerNum()).add(duplicateFigure);
				figureButtonMap.put(duplicateFigure,figureButtonMap.get(figure));
				selectedFigure = null;
				redrawCanvas();
			}
		});



		canvas.setOnMousePressed(event -> startPoint = new Point(event.getX(), event.getY()));

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null || endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return ;
			}
			Figure newFigure = null;
			if(currentLayer.getIsHidden()){
				System.out.println("esta oculta");
				return;
			}
			for(CustomButton button : customButtons){
				if(button.isSelected()){
					newFigure = button.createNewFigure(startPoint,endPoint,Math.abs(endPoint.getX()
							- startPoint.getX()),Math.abs(endPoint.getY() - startPoint.getY()), Math.abs(endPoint.getX() - startPoint.getX()));
					if (newFigure != null) {
						FigureProperties fp = new FigureProperties(fillColorPicker.getValue(),
								shadowsBox.getValue(),
								fillSecondaryColorPicker.getValue(),
								edgeBox.getValue(), borderSlider.getValue());
						//VER DE SACAR EL PARAMETRO NEWFIGURE.
						figurePropertiesMap.put(newFigure, button.createDrawfigure(gc,fp,newFigure));
						layerFigureMap.get(currentLayer.getLayerNum()).add(newFigure);
						figureButtonMap.put(newFigure,button);
					}

				}

			}
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			List<Layer> layersReversed = new ArrayList<>(layerFigureMap.values());
			Collections.reverse(layersReversed);
			for (Layer layer : layersReversed) {
				List<Figure> figuresReversed = new ArrayList<>((Collection) layer.figures());
				Collections.reverse(figuresReversed);
				for (Figure figure : figuresReversed) {
					if (figure.belongs(eventPoint)) {
						found = true;
						label.append(figure);
					}
				}
			}
			if (found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Layer layer : layerFigureMap.values()) {
					for (Figure figure : layer.figures()) {
						if (figure.belongs(eventPoint)) {
							found = true;
							selectedFigure = figure;
							label.append(figure);
						}
					}
				}
				if (found) {
					setPreviousProp();
					statusPane.updateStatus(label.toString());
					/*FigureProperties fp = figurePropertiesMap.get(selectedFigure).getFigureProperties();
					shadowsBox.setValue(fp.getShadowType());
					fillColorPicker.setValue(fp.getColor());
					borderSlider.setValue(fp.getWidth());
					edgeBox.setValue(fp.getEdge());
					fillSecondaryColorPicker.setValue(fp.getSecondaryColor());*/
				} else {
					selectedFigure = null;
					statusPane.updateStatus("Ninguna figura encontrada");

				}
				redrawCanvas();
			}

		});

		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				try {
					if (selectedFigure != null) {
						Point eventPoint = new Point(event.getX(), event.getY());
						double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
						double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
						selectedFigure.moveCoordX(diffX);
						selectedFigure.moveCoordY(diffY);
						redrawCanvas();
					}
				}catch(Exception e){
					showHiddenLayerAlert(e.getMessage());
					//System.out.println("No hay ninguna figura seleccionada");
				}
			}

		});




		setLeft(buttonsBox);
		setRight(canvas);


	}

	private void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (Layer layer : layerFigureMap.values()) {
			if (!layer.getIsHidden()) {
				for (Figure figure : layer.figures()) {
					if (figure.equals(selectedFigure)) {
						gc.setStroke(Color.RED);
					} else {
						gc.setStroke(lineColor);
					}
					DrawFigure drawFigure = figurePropertiesMap.get(figure);
					drawFigure.drawFigure();
				}
			}
		}
	}
	private void updateFigureProperties(){
		FigureProperties figureProperties = figurePropertiesMap.get(selectedFigure).getFigureProperties();
		figureProperties.setColor(fillColorPicker.getValue());
		figureProperties.setShadowType(shadow);
		figureProperties.setSecondaryColor(fillSecondaryColorPicker.getValue());
		figureProperties.setEdge(edge);
		figureProperties.setWidth(borderSlider.getValue());
	}

	private void setPreviousProp(){
		FigureProperties fp = figurePropertiesMap.get(selectedFigure).getFigureProperties();
		fillColorPicker.setValue(fp.getColor());
		fillSecondaryColorPicker.setValue(fp.getSecondaryColor());
		borderSlider.setValue(fp.getWidth());
		edgeBox.setValue(fp.getEdge());
		shadowsBox.setValue(fp.getShadowType());
	}

	private void setButtons(ToggleButton[] buttons, ToggleGroup group){
		for(ToggleButton button: buttons){
			button.setMinWidth(90);
			button.setToggleGroup(group);
			button.setCursor(Cursor.HAND);
		}
	}

	private void showLayerAlert(String message){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error de dibujo");
        alert.setHeaderText("En las capas");
        alert.setContentText(message);
        alert.showAndWait();
	}


	private void showHiddenLayerAlert(String message){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error de capas");
        alert.setHeaderText("Dibujo en capa");
        alert.setContentText(message);
        alert.showAndWait();
	}

}

