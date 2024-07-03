package frontend;

import backend.*;
import backend.model.*;
import frontend.DrawFigures.DrawEllipse;
import frontend.DrawFigures.DrawFigure;
import frontend.DrawFigures.DrawRect;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import backend.Layer;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class PaintPane extends BorderPane {

	// BackEnd
	private final Layers canvasState;

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

	// Seleccionar una figura
	private Figure selectedFigure;

	//seleccionar un tipo de sombra
	private ShadowType shadow;

	//seleccionar un tipo de borde
	private EdgeType edge;

	private Layer layer;

	private Layers layers = new Layers();

	// StatusBar
	private final StatusPane statusPane;

	// Colores de relleno de cada figura

	private final Map<Figure, FigureProperties> figurePropertiesMap = new HashMap<>();

	private final Map<Figure, Color> figureColorMap = new HashMap<>();

	private final Map<ToggleButton, Figure> figureButtonMap = new HashMap<>();

	private final Map<Class<? extends Figure>, DrawFigure> drawFigureMap = new HashMap<>();

	ChoiceBox<ShadowType> shadowsBox = new ChoiceBox<>();
	ChoiceBox<EdgeType> edgeBox = new ChoiceBox<>();
	ChoiceBox<Layer> layerBox = new ChoiceBox<>();

	public PaintPane(Layers canvasState, StatusPane statusPane) {

		this.canvasState = canvasState;
		this.statusPane = statusPane;

		ChoiceBox<ShadowType> shadowsBox = new ChoiceBox<>();
		ChoiceBox<EdgeType> edgeBox = new ChoiceBox<>();
		ChoiceBox<Layer> layerBox = new ChoiceBox<>();
		Label shadowLable = new Label("Sombras");
		Label borderLable = new Label("Borde");
		Label fillingLable = new Label("Relleno");
		Label actionLable = new Label("Acciones");
		shadowsBox.getItems().addAll(ShadowType.NONE, ShadowType.SIMPLE, ShadowType.COLOURED, ShadowType.SIMPLE_INVERSED, ShadowType.COLOURED_INVERSED);
		edgeBox.getItems().addAll(EdgeType.NORMAL,EdgeType.SIMPLE_DOTTED, EdgeType.COMPLEX_DOTTED);

		layers.addLayer();
		layers.addLayer();
		layers.addLayer();
		for(Layer layer : layers.getLayers()){
			layer.cannotEliminate();
			layerBox.getItems().add(layer);
		}
		layer = layers.getLayers().getFirst();
		layerBox.setValue(layers.getLayers().getFirst());
		layerBox.setOnAction(event -> {
			layer = layerBox.getValue();
		});

		shadowsBox.setValue(ShadowType.NONE);
		shadowsBox.setOnAction(event -> {
			shadow = shadowsBox.getValue();
		});


		edgeBox.setValue(EdgeType.NORMAL);
		edgeBox.setOnAction(event -> {
			edge = edgeBox.getValue();
		});

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
		//hacer un metodo que haga esto
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}
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
		//hacer un metodo que haga esto
		ToggleButton[] actionsArray = {duplicateButton, divideButton, moveButton};
		ToggleGroup arrTools = new ToggleGroup();
		for (ToggleButton tool : actionsArray) {
			tool.setMinWidth(90);
			tool.setToggleGroup(arrTools);
			tool.setCursor(Cursor.HAND);
		}
		buttonsBox.getChildren().addAll(actionsArray);

		//hacer un metodo que haga esto
		ToggleButton[] layersArr = {showLayer,hideLayer,addLayer,removeLayer};
		ToggleGroup arrLayers = new ToggleGroup();
		for(ToggleButton button : layersArr){
			button.setMinWidth(90);
			button.setToggleGroup(arrLayers);
			button.setCursor(Cursor.HAND);
		}

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

		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

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
				figureColorMap.put(newFigure,fillColorPicker.getValue());
				figurePropertiesMap.put(newFigure, new FigureProperties(fillColorPicker.getValue(), shadowsBox.getValue(), fillSecondaryColorPicker.getValue(), edgeBox.getValue(), borderSlider.getValue()));
				layers.getLayers().get(layer.getLayerNum()).add(newFigure);
			}

			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Layer layer : layers.getLayers()){
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
//CAMBIAR ESTO DESPUES HACER VARIOS SET ON MOUSE CLICKED.
		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : layer.figures()) {
					if (figure.belongs(eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure);

						FigureProperties figureProperties = new FigureProperties(fillColorPicker.getValue(), shadowsBox.getValue(),
								fillSecondaryColorPicker.getValue(), edgeBox.getValue(), borderSlider.getValue());
						figurePropertiesMap.replace(figure, figureProperties);

						if (duplicateButton.isSelected()) {
							//ES MUY FEO, VER SI SE PUEDE CAMBIAR. !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							Figure duplicateFigure = figure.createNewFigure(figure.getPoint1().displacePoint(), figure.getPoint2().displacePoint()
									, figure.getAxis1(), figure.getAxis2(), figure.getAxis1());

							figurePropertiesMap.put(duplicateFigure, figureProperties);
							layers.getLayers().get(layer.getLayerNum()).add(duplicateFigure);
							//canvasState.add(duplicateFigure);

							DrawFigure drawFigure = drawFigureMap.get(figure.getClass());
							drawFigure.createDrawfigure(gc, figureProperties, figure).drawFigure();
							redrawCanvas();
						}
						if (moveButton.isSelected()) {
							if (selectedFigure != null) {
								selectedFigure.centerFigure(canvas.getWidth(), canvas.getHeight());
								redrawCanvas();
							}
						}
						if (divideButton.isSelected()) {

							double midX = (figure.getPoint1().getX() + figure.getPoint2().getX()) / 2;

							//Dividimos por 3.93 debido a que nos queda de la mejor manera con esa cuenta
							Figure figureLeft = figure.createDividedFigure(figure.getPoint1(), new Point(midX, figure.getPoint2().getY()),
									new Point(figure.getPoint1().getX() - figure.getAxis1() / 3.93, figure.getPoint1().getY()), figure.getAxis1() / 2, figure.getAxis2() / 2);
							Figure figureRight = figure.createDividedFigure(new Point(midX, figure.getPoint1().getY()), figure.getPoint2(),
									new Point(figure.getPoint1().getX() + figure.getAxis1() / 3.93, figure.getPoint1().getY()), figure.getAxis1() / 2, figure.getAxis2() / 2);

							layers.getLayers().get(layer.getLayerNum()).add(figureLeft);
							layers.getLayers().get(layer.getLayerNum()).add(figureRight);
							layers.getLayers().get(layer.getLayerNum()).remove(figure);

								/*
								canvasState.add(figureLeft);
								canvasState.add(figureRight);
								canvasState.remove(figure);
								 */

							DrawFigure drawFigureLeft = drawFigureMap.get(figureLeft.getClass());
							drawFigureLeft.createDrawfigure(gc, figureProperties, figureLeft).drawFigure();

							DrawFigure drawFigureRight = drawFigureMap.get(figureRight.getClass());
							drawFigureRight.createDrawfigure(gc, figureProperties, figureRight).drawFigure();

							figurePropertiesMap.put(figureLeft, figureProperties);
							figurePropertiesMap.put(figureRight, figureProperties);

							redrawCanvas();
						}
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
			if(addLayer.isSelected()){
				int num=layers.getLayerNum();
				layerBox.getItems().add(new Layer(num));
				redrawCanvas();
			}
			if(removeLayer.isSelected()){
				if (layers.getLayers().get(layer.getLayerNum()).getEliminate()){
					layers.getLayers().remove(layer);
				}
				redrawCanvas();
			}
		});

		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				if(selectedFigure == null){
					throw new NoFigureSelectionException();
				}
				selectedFigure.moveCoordX(diffX);
				selectedFigure.moveCoordY(diffY);
				redrawCanvas();
			}
		});

		deleteButton.setOnAction(event -> {
			if (selectedFigure != null) {
				layers.getLayers().get(layer.getLayerNum()).remove(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		setLeft(buttonsBox);
		setRight(canvas);
	}


	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		//Podemos hacer un metodo getLayerFigures y devuelve las figuras del layer.
		for (Layer layer : layers.getLayers()) {
			for (Figure figure : layer.figures()) {
				if (!layer.getIsHidden()) {
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
}

