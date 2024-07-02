package frontend;

import backend.CanvasState;
import backend.EdgeType;
import backend.ShadowType;
import backend.model.*;
import frontend.DrawFigures.DrawEllipse;
import frontend.DrawFigures.DrawFigure;
import frontend.DrawFigures.DrawRect;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class
PaintPane extends BorderPane {

	// BackEnd
	private final CanvasState canvasState;

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

	// StatusBar
	private final StatusPane statusPane;

	// Colores de relleno de cada figura

	private final Map<Figure, FigureProperties> figurePropertiesMap = new HashMap<>();

	private final Map<Figure, Color> figureColorMap = new HashMap<>();

	private final Map<ToggleButton, Figure> figureButtonMap = new HashMap<>();

	private final Map<Class<? extends Figure>, DrawFigure> drawFigureMap = new HashMap<>();

	ChoiceBox<ShadowType> shadowsBox = new ChoiceBox<>();
	ChoiceBox<EdgeType> edgeBox = new ChoiceBox<>();

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;

		ChoiceBox<ShadowType> shadowsBox = new ChoiceBox<>();
		ChoiceBox<EdgeType> edgeBox = new ChoiceBox<>();
		Label shadowLable = new Label("Sombras");
		Label borderLable = new Label("Borde");
		Label fillingLable = new Label("Relleno");
		shadowsBox.getItems().addAll(ShadowType.NONE, ShadowType.SIMPLE, ShadowType.COLOURED, ShadowType.SIMPLE_INVERSED, ShadowType.COLOURED_INVERSED);
		edgeBox.getItems().addAll(EdgeType.NORMAL,EdgeType.SIMPLE_DOTTED, EdgeType.COMPLEX_DOTTED);

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
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

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
				canvasState.add(newFigure);
			}

			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
				if(figure.belongs(eventPoint)) {
					found = true;
					label.append(figure);
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
				for (Figure figure : canvasState.figures()) {
					if(figure.belongs(eventPoint)) {
						System.out.println(figure);
						found = true;
						selectedFigure = figure;
						label.append(figure);
						FigureProperties figureProperties = new FigureProperties(fillColorPicker.getValue(), shadowsBox.getValue(),
								fillSecondaryColorPicker.getValue(), edgeBox.getValue(), borderSlider.getValue());
						figurePropertiesMap.replace(figure,figureProperties);
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
				canvasState.remove(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		setLeft(buttonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if(figure == selectedFigure) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(lineColor);
			}
			FigureProperties fp = figurePropertiesMap.get(figure);
			DrawFigure drawFigure = drawFigureMap.get(figure.getClass());
			drawFigure.createDrawfigure(gc,fp,figure).drawFigure();
		}
	}

}

