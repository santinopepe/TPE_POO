package frontend;

import backend.Exceptions.*;
import javafx.scene.control.Alert.AlertType;
import backend.*;
import backend.model.*;
import frontend.Buttons.*;
import frontend.DrawFigures.*;
import frontend.DrawFigures.DrawFigure;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import backend.CanvasState;

import java.util.*;


public class PaintPane extends BorderPane {

    // Canvas y relacionados
	private final Canvas canvas = new Canvas(800, 600);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();
	private final Color lineColor = Color.BLACK;
	private final Color defaultFillColor = Color.YELLOW;

	// Botones Barra Izquierda
	private final ToggleButton selectionButton = new ToggleButton("Seleccionar");
	private final ToggleButton deleteButton = new ToggleButton("Borrar");

	private final CustomButton rectangleButton = new RectangleButton("Rectángulo");
	private final CustomButton circleButton = new CircleButton("Círculo");
	private final CustomButton squareButton = new SquareButton("Cuadrado");
	private final CustomButton ellipseButton = new EllipseButton("Elipse");


	private final Button duplicateButton = new Button("Duplicar");
	private final Button divideButton = new Button("Dividir");
	private final Button moveButton = new Button("Mov. Centro");
	private final Button addLayer = new Button("Agregar capa");
	private final Button removeLayer = new Button("Eliminar capa");
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

	private Layers currentLayer;

	//CanvasState
	CanvasState canvasState;

	// Seleccionar una figura
	private Figure selectedFigure;

	private int cantLayer=0;

	private final Map<Figure, DrawFigure> figurePropertiesMap = new HashMap<>();

	private final Map<Figure,CustomButton> figureButtonMap = new HashMap<>();

	private final SortedMap<Integer, Layers> layerFigureMap = new TreeMap<>();

	private final ChoiceBox<ShadowType> shadowsBox = new ChoiceBox<>();
	private final ChoiceBox<EdgeType> edgeBox = new ChoiceBox<>();
	private final ChoiceBox<Layers> layerBox = new ChoiceBox<>();

	public PaintPane(CanvasState canvasState,StatusPane statusPane) {
		this.canvasState = canvasState;

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
		CustomButton[] customButtons = {rectangleButton, circleButton, squareButton, ellipseButton};

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


		Button[] actionsArray = {duplicateButton, divideButton, moveButton};
		setCustomButtons(actionsArray);
		buttonsBox.getChildren().addAll(actionsArray);

		ToggleButton[] layersArr = {showLayer,hideLayer};
		ToggleGroup arrLayers = new ToggleGroup();
		setButtons(layersArr,arrLayers);

		Button[] addRemoveArr ={addLayer,removeLayer};
		setCustomButtons(addRemoveArr);

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
		horizontalBox.getChildren().addAll(addRemoveArr);

		horizontalBox.setPadding(new Insets(5));
		horizontalBox.setStyle("-fx-background-color: #999");
		horizontalBox.setPrefHeight(50);
		setTop(horizontalBox);

		for (int i = 0; i < 3; i++) {
			layerFigureMap.put(canvasState.getCurrentLayer(), new Layers(canvasState.getAndIncrementLayer()));
			System.out.println(canvasState.getCurrentLayer());
			layerFigureMap.get(i).cannotEliminate();
			layerBox.getItems().add(layerFigureMap.get(i));
		}

		layerBox.setValue(layerFigureMap.get(0));
		currentLayer = layerFigureMap.get(0);

		fillColorPicker.setOnAction(event -> primaryColorAction());

		fillSecondaryColorPicker.setOnAction(event -> secondaryColorAction());

		borderSlider.valueProperty().addListener((observable, oldValue, newValue) -> edgeSliderAction());

		shadowsBox.setOnAction(event -> shadowBoxAction());

		edgeBox.setOnAction(event -> edgeBoxAction());

		selectionButton.setOnAction(event -> selectionButtonAction());

		addLayer.setOnAction(event -> addLayerAction());

		moveButton.setOnAction(event -> moveAction());

		divideButton.setOnAction(event -> divideAction());

		layerBox.setOnAction(event -> layerBoxAction());

		showLayer.setOnAction(event -> showLayerAction());

		hideLayer.setOnAction(event -> hideLayerAction());

		deleteButton.setOnAction(event -> deleteButtonAction());

		removeLayer.setOnAction(event -> removeLayerAction());

		duplicateButton.setOnAction(event -> duplicateAction());

		canvas.setOnMousePressed(this::mousePressed);

		canvas.setOnMouseReleased(event -> mouseReleased(event,customButtons));

		canvas.setOnMouseMoved(event -> mouseMoved(event,statusPane));

		canvas.setOnMouseClicked(event -> mouseClicked(event,statusPane));

		canvas.setOnMouseDragged(this::mouseDragged);

		setLeft(buttonsBox);
		setRight(canvas);
	}



	private void selectionButtonAction(){
		if (selectionButton.isSelected()) {
			if (selectedFigure != null) {
				setPreviousProp();
				redrawCanvas();
			}
		}
	}

	//Acciones con mouse

	private void mouseClicked(MouseEvent event, StatusPane statusPane){
		if(selectionButton.isSelected()) {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder("Se seleccionó: ");
			for (Layers layer : layerFigureMap.values()) {
				if (!layer.getIsHidden()){
					for (Figure figure : layer.figures()) {
						if (figure.belongs(eventPoint)) {
							found = true;
							selectedFigure = figure;
							label.append(figure);
							break;
						}
					}
					if (found) break;
				}
			}
			if (found) {
				setPreviousProp();
				statusPane.updateStatus(label.toString());
			} else {
				selectedFigure = null;
				statusPane.updateStatus("Ninguna figura encontrada");

			}
			redrawCanvas();
		}
	}
	private void mouseDragged(MouseEvent event){
		if(selectionButton.isSelected()) {
			if(selectedFigure!=null){
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				selectedFigure.moveCoordX(diffX);
				selectedFigure.moveCoordY(diffY);
				redrawCanvas();
			}
		}
	}
	private void mouseMoved(MouseEvent event, StatusPane statusPane){
		Point eventPoint = new Point(event.getX(), event.getY());
		boolean found = false;
		StringBuilder label = new StringBuilder();
		List<Layers> layersReversed = new ArrayList<>(layerFigureMap.values());
		Collections.reverse(layersReversed);
		for (Layers layer : layersReversed) {
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
	}
	private void mouseReleased(MouseEvent event, CustomButton[] customButtons){
		Point endPoint = new Point(event.getX(), event.getY());
		if(startPoint == null || endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
			return ;
		}
		Figure newFigure = null;
		try {
			currentLayer.getHiddenException();
			for (CustomButton button : customButtons) {
				if (button.isSelected()) {
					newFigure = button.createNewFigure(startPoint, endPoint, Math.abs(endPoint.getX()
							- startPoint.getX()), Math.abs(endPoint.getY() - startPoint.getY()), Math.abs(endPoint.getX() - startPoint.getX()));
					if (newFigure != null) {
						FigureProperties fp = new FigureProperties(fillColorPicker.getValue(),
								shadowsBox.getValue(),
								fillSecondaryColorPicker.getValue(),
								edgeBox.getValue(), borderSlider.getValue());

						//VER DE SACAR EL PARAMETRO NEWFIGURE.
						figurePropertiesMap.put(newFigure, button.createDrawfigure(gc, fp, newFigure));
						layerFigureMap.get(currentLayer.getLayerNum()).add(newFigure);
						figureButtonMap.put(newFigure, button);
					}

				}
			}
		}catch(HiddenLayerException e){
			showHiddenLayerAlert(e.getMessage());
		}
		startPoint = null;
		redrawCanvas();
	}
	private void mousePressed(MouseEvent event){
		startPoint = new Point(event.getX(), event.getY());
	}

	//Acciones Duplicate y Divide
	private void duplicateAction(){
		Figure figure;
		if(selectedFigure!=null){
			figure = selectedFigure;
			FigureProperties figureProperties = figurePropertiesMap.get(figure).getFigureProperties();


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
			DrawFigure dupDrawFigure = figureButtonMap.get(figure).createDrawfigure(gc, dupProperties, duplicateFigure);
			figurePropertiesMap.put(duplicateFigure, dupDrawFigure);
			layerFigureMap.get(currentLayer.getLayerNum()).add(duplicateFigure);
			figureButtonMap.put(duplicateFigure, figureButtonMap.get(figure));
			selectedFigure = null;
			redrawCanvas();
		}
	}
	private void divideAction(){
		if(selectedFigure != null) {
			double midX = (selectedFigure.getPoint1().getX() + selectedFigure.getPoint2().getX()) / 2;
			double midY = (selectedFigure.getPoint1().getY() + selectedFigure.getPoint2().getY()) / 2;
			double diff = (selectedFigure.getPoint2().getY() - selectedFigure.getPoint1().getY()) / 4;

			Figure figureLeft = selectedFigure.createDividedFigure(
					new Point(selectedFigure.getPoint1().getX(), midY - diff),
					new Point(midX, selectedFigure.getPoint2().getY() - diff),
					new Point(selectedFigure.getPoint1().getX() - selectedFigure.getAxis1() / 3.93, selectedFigure.getPoint1().getY()),
					selectedFigure.getAxis1() / 2,
					selectedFigure.getAxis2() / 2
			);

			Figure figureRight = selectedFigure.createDividedFigure(
					new Point(midX, midY - diff),
					new Point(selectedFigure.getPoint2().getX(), midY + diff),
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

			DrawFigure dfL = figureButtonMap.get(selectedFigure).createDrawfigure(gc, leftProperties, figureLeft);
			DrawFigure dfR = figureButtonMap.get(selectedFigure).createDrawfigure(gc, rightProperties, figureRight);
			figureButtonMap.put(figureLeft, figureButtonMap.get(selectedFigure));
			figureButtonMap.put(figureRight, figureButtonMap.get(selectedFigure));

			figureButtonMap.remove(selectedFigure);
			figurePropertiesMap.remove(selectedFigure);

			figurePropertiesMap.put(figureLeft, dfL);
			figurePropertiesMap.put(figureRight, dfR);

			layerFigureMap.get(currentLayer.getLayerNum()).add(figureLeft);
			layerFigureMap.get(currentLayer.getLayerNum()).add(figureRight);
			layerFigureMap.get(currentLayer.getLayerNum()).remove(selectedFigure);
		}
		selectedFigure = null;
		redrawCanvas();
	}
	private void deleteButtonAction(){
		if(selectedFigure != null){
			layerFigureMap.get(currentLayer.getLayerNum()).remove(selectedFigure);
			selectedFigure = null;
			redrawCanvas();
		}
	}

	private void moveAction(){
		if(selectedFigure != null){
			selectedFigure.centerFigure(canvas.getWidth(), canvas.getHeight());
			redrawCanvas();
		}
	}

	//Acciones con layers
	private void layerBoxAction(){
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
	}
	private void hideLayerAction(){
		layerFigureMap.get(layerBox.getValue().getLayerNum()).hide();
		redrawCanvas();
	}
	private void showLayerAction(){
		layerFigureMap.get(layerBox.getValue().getLayerNum()).unHide();
		redrawCanvas();
	}
	private void addLayerAction(){
		layerFigureMap.put(canvasState.getCurrentLayer(),new Layers(canvasState.getAndIncrementLayer()));
		layerBox.getItems().add(layerFigureMap.get(canvasState.getCurrentLayer()-1));
		currentLayer = layerFigureMap.get(canvasState.getCurrentLayer()-1);
		layerBox.setValue(currentLayer);
	}
	private void removeLayerAction(){
		try {
			currentLayer.canEliminateException();
			layerFigureMap.remove(currentLayer.getLayerNum());
			layerBox.getItems().remove(currentLayer);
			//Agregue esto porque cuando eliminabamos aparecia en choicebox como si
			//estuviesemos en la capa q eliminamos.
			layerBox.setValue(layerFigureMap.get(0));

		}catch (NotDeletableLayerException ex){
			showLayerAlert(ex.getMessage());
		}
	}

	//Acciones que afectan las carateristicas de las figuras.

	private void edgeBoxAction(){
		if(selectedFigure != null && selectionButton.isSelected()) {
			DrawFigure df = figurePropertiesMap.get(selectedFigure);
			df.getFigureProperties().setEdge(edgeBox.getValue());
			redrawCanvas();
		}
	}
	private void shadowBoxAction(){
		if(selectedFigure != null && selectionButton.isSelected()) {
			DrawFigure df = figurePropertiesMap.get(selectedFigure);
			df.getFigureProperties().setShadowType(shadowsBox.getValue());
			redrawCanvas();
		}
	}
	private void edgeSliderAction(){
		if(selectedFigure != null && selectionButton.isSelected()) {
			DrawFigure df = figurePropertiesMap.get(selectedFigure);
			df.getFigureProperties().setWidth(borderSlider.getValue());
			redrawCanvas();
		}
	}
	private void secondaryColorAction(){
		if(selectedFigure != null && selectionButton.isSelected()) {
			DrawFigure df = figurePropertiesMap.get(selectedFigure);
			df.getFigureProperties().setSecondaryColor(fillSecondaryColorPicker.getValue());
			redrawCanvas();
		}
	}
	private void primaryColorAction(){
		if(selectedFigure != null && selectionButton.isSelected()) {
			DrawFigure df = figurePropertiesMap.get(selectedFigure);
			df.getFigureProperties().setColor(fillColorPicker.getValue());
			redrawCanvas();
		}
	}

	private void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (Layers layer : layerFigureMap.values()) {
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

	private void setPreviousProp(){
		FigureProperties fp = figurePropertiesMap.get(selectedFigure).getFigureProperties();
		fillColorPicker.setValue(fp.getColor());
		fillSecondaryColorPicker.setValue(fp.getSecondaryColor());
		borderSlider.setValue(fp.getWidth());
		edgeBox.setValue(fp.getEdge());
		shadowsBox.setValue(fp.getShadowType());
	}

	private void setCustomButtons(Button[] buttons){
		for(Button button: buttons){
			button.setMinWidth(90);
			button.setCursor(Cursor.HAND);
		}
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

