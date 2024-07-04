package frontend;

import backend.*;
import backend.model.*;
import frontend.DrawFigures.*;
import frontend.DrawFigures.DrawFigure;
import frontend.DrawFigures.DrawRect;
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
	private final ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	private final ToggleButton circleButton = new ToggleButton("Círculo");
	private final ToggleButton squareButton = new ToggleButton("Cuadrado");
	private final ToggleButton ellipseButton = new ToggleButton("Elipse");
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
	private final ColorPicker fillSecondaryColorPicker = new ColorPicker(defaultFillColor);

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

	private final Map<Figure, FigureProperties> figurePropertiesMap = new HashMap<>();

	private final Map<ToggleButton, Figure> figureButtonMap = new HashMap<>();

	private final Map<Class<? extends Figure>, DrawFigure> drawFigureMap = new HashMap<>();

	private final SortedMap<Integer, Layer> layerFigureMap = new TreeMap<>();

	
	public PaintPane(Layers canvasState, StatusPane statusPane) {

		ChoiceBox<ShadowType> shadowsBox = new ChoiceBox<>();
		ChoiceBox<EdgeType> edgeBox = new ChoiceBox<>();
		ChoiceBox<Layer> layerBox = new ChoiceBox<>();

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
		ToggleGroup tools = new ToggleGroup();

		figureButtonMap.put(rectangleButton, new Rectangle(new Point(0,0),new Point(0,0)));
		figureButtonMap.put(circleButton, new Circle(new Point(0,0), 1));
		figureButtonMap.put(squareButton, new Square(new Point(0,0), 0));
		figureButtonMap.put(ellipseButton, new Ellipse(new Point(0,0),1,1));

		//DESPUES REPENSAR.
		drawFigureMap.put(Rectangle.class, new DrawRect(gc, new FigureProperties(null, null, null, null, 0.0),
				new Rectangle(new Point(0,0),new Point(0,0))));
		drawFigureMap.put(Square.class, new DrawRect(gc, new FigureProperties(null, null, null, null, 0.0),
				new Square(new Point(0,0), 0)));
		drawFigureMap.put(Circle.class, new DrawEllipse(gc, new FigureProperties(null, null, null, null, 0.0),
				new Circle(new Point(0,0), 0)));
		drawFigureMap.put(Ellipse.class,new DrawEllipse(gc, new FigureProperties(null, null, null, null, 0.0),
				new  Ellipse(new Point(0,0),1,1)));




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

		layerFigureMap.put(cantLayer, new Layer(cantLayer++));
		layerFigureMap.put(cantLayer, new Layer(cantLayer++));
		layerFigureMap.put(cantLayer, new Layer(cantLayer++));

		layerBox.setValue(layerFigureMap.get(0));
		currentLayer = layerFigureMap.get(0);

		for (int i = 0; i < cantLayer; i++) {
			layerFigureMap.get(i).cannotEliminate();
		}

		layerBox.getItems().add(layerFigureMap.get(0));
		layerBox.getItems().add(layerFigureMap.get(1));
		layerBox.getItems().add(layerFigureMap.get(2));

		fillColorPicker.setOnAction(event -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				figurePropertiesMap.replace(selectedFigure, updateFigureProperties());
				redrawCanvas();
			}
		});

		fillSecondaryColorPicker.setOnAction(event -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				figurePropertiesMap.replace(selectedFigure, updateFigureProperties());
				redrawCanvas();
			}
		});

		borderSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				figurePropertiesMap.replace(selectedFigure, updateFigureProperties());
				redrawCanvas();
			}
		});

		shadowsBox.setOnAction(event -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				shadow = shadowsBox.getValue();
				figurePropertiesMap.replace(selectedFigure, updateFigureProperties());
				redrawCanvas();
			}
		});

		edgeBox.setOnAction(event -> {
			if(selectedFigure != null && selectionButton.isSelected()) {
				edge = edgeBox.getValue();
				figurePropertiesMap.replace(selectedFigure, updateFigureProperties());
				redrawCanvas();
			}
		});

		selectionButton.setOnAction(event -> {
			if (selectionButton.isSelected()) {
				if (selectedFigure != null) {
					updateFigureProperties();
					redrawCanvas();
				}
			}
		});

		addLayer.setOnAction(event -> {
			if(addLayer.isSelected()){
				layerFigureMap.put(cantLayer,new Layer(cantLayer++));
				layerBox.getItems().add(layerFigureMap.get(cantLayer-1));
				currentLayer = layerFigureMap.get(cantLayer-1);
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

				FigureProperties figureProperties = figurePropertiesMap.get(selectedFigure);
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

				figurePropertiesMap.put(figureLeft, leftProperties);
				figurePropertiesMap.put(figureRight, rightProperties);

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
				System.out.println("La capa no existe");
			}
		});

		duplicateButton.setOnAction(event -> {
			Figure figure;
			if(selectedFigure != null && duplicateButton.isSelected()){
				figure = selectedFigure;
				FigureProperties figureProperties = figurePropertiesMap.get(selectedFigure);
				//ES MUY FEO, VER SI SE PUEDE CAMBIAR. !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				Figure duplicateFigure = figure.createNewFigure(figure.getPoint1().displacePoint(), figure.getPoint2().displacePoint()
						, figure.getAxis1(), figure.getAxis2(), figure.getAxis1());

				FigureProperties dupProperties = new FigureProperties(
						figureProperties.getColor(),
						figureProperties.getShadowType(),
						figureProperties.getSecondaryColor(),
						figureProperties.getEdge(),
						figureProperties.getWidth()
				);

				figurePropertiesMap.put(duplicateFigure, dupProperties);
				layerFigureMap.get(currentLayer.getLayerNum()).add(duplicateFigure);
				DrawFigure drawFigure = drawFigureMap.get(figure.getClass());
				drawFigure.createDrawfigure(gc, dupProperties, figure).drawFigure();

				redrawCanvas();
			}
		});



		canvas.setOnMousePressed(event -> startPoint = new Point(event.getX(), event.getY()));

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return ;
			}
			Figure newFigure = null;
			for(ToggleButton button : toolsArr){
				if(button.isSelected() && !button.equals(selectionButton) && !button.equals(deleteButton) ){
					newFigure = figureButtonMap.get(button).createNewFigure(startPoint,endPoint,Math.abs(endPoint.getX()
									- startPoint.getX()),Math.abs(endPoint.getY() - startPoint.getY()), Math.abs(endPoint.getX() - startPoint.getX()));
				}
			}
			if (newFigure != null) {
				figurePropertiesMap.put(newFigure, new FigureProperties(fillColorPicker.getValue(),
						shadowsBox.getValue(),
						fillSecondaryColorPicker.getValue(),
						edgeBox.getValue(), borderSlider.getValue()));

				layerFigureMap.get(currentLayer.getLayerNum()).add(newFigure);
			}

			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Layer layer : layerFigureMap.values()){
				for(Figure figure : layer.figures()) {
					if(figure.belongs(eventPoint)) {
						found = true;
						label.append(figure);
					}
				}
			}
			if(found) {
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
				for (Figure figure : currentLayer.figures()) {
					if (figure.belongs(eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure);
					}
				}
				if (found) {
					statusPane.updateStatus(label.toString());
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
					Point eventPoint = new Point(event.getX(), event.getY());
					double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
					double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
					selectedFigure.moveCoordX(diffX);
					selectedFigure.moveCoordY(diffY);
					redrawCanvas();
				}catch(Exception ex){
					System.out.println("No hay ninguna figura seleccionada");
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
					FigureProperties fp = figurePropertiesMap.get(figure);
					DrawFigure drawFigure = drawFigureMap.get(figure.getClass());
					drawFigure.createDrawfigure(gc, fp, figure).drawFigure();
				}
			}
		}
	}

	//METODO UPDATE CAMBIA LAS CARACTERISTICAS DE UNA FIGURA EN TIMEPO REAL.
	private FigureProperties updateFigureProperties(){
		FigureProperties figureProperties = figurePropertiesMap.get(selectedFigure);
		figureProperties.setColor(fillColorPicker.getValue());
		figureProperties.setShadowType(shadow);
		figureProperties.setSecondaryColor(fillSecondaryColorPicker.getValue());
		figureProperties.setEdge(edge);
		figureProperties.setWidth(borderSlider.getValue());
		return figureProperties;
	}

	private void setButtons(ToggleButton[] buttons, ToggleGroup group){
		for(ToggleButton button: buttons){
			button.setMinWidth(90);
			button.setToggleGroup(group);
			button.setCursor(Cursor.HAND);
		}
	}
}

