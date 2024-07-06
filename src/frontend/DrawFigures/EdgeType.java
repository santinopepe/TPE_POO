package frontend.DrawFigures;

import javafx.scene.canvas.GraphicsContext;

//Enum para los tres tipos de bordes
public enum EdgeType {

    NORMAL{
        @Override
        public void getBorder(GraphicsContext gc) {
           gc.setLineDashes(0);
        }

        @Override
        public String toString() {
            return "Normal";
        }
    },
    SIMPLE_DOTTED{
        @Override
        public void getBorder(GraphicsContext gc) {
            gc.setLineDashes(10);
        }

        @Override
        public String toString() {
            return "Simple";
        }
    },
    COMPLEX_DOTTED{
        @Override
        public void getBorder(GraphicsContext gc) {
            gc.setLineDashes(30,10,15,10);
        }

        @Override
        public String toString() {
            return "Complejo";
        }
    };

    public abstract void getBorder(GraphicsContext gc);
}
