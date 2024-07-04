package backend;

import javafx.scene.canvas.GraphicsContext;

public enum EdgeType {

    NORMAL(){
        @Override
        public Double[] getBorder() {
            Double[] arr = new Double[5];
            arr[0] = 0.0;
            arr[1] = 0.0;
            arr[2] = 0.0;
            arr[3] = 0.0;
            arr[4] = 0.0;
            return arr;
        }

        @Override
        public String toString() {
            return "Normal";
        }
    },
    SIMPLE_DOTTED(){
        @Override
        public Double[] getBorder() {
            Double[] arr = new Double[5];
            arr[0] = 10.0;
            arr[1] = 0.0;
            arr[2] = 0.0;
            arr[3] = 0.0;
            arr[4] = 0.0;
            return arr;
        }

        @Override
        public String toString() {
            return "Simple";
        }
    },
    COMPLEX_DOTTED(){
        @Override
        public Double[] getBorder() {
            Double[] arr = new Double[5];
            arr[0] = 30.0;
            arr[1] = 10.0;
            arr[2] = 15.0;
            arr[3] = 10.0;
            arr[4] = 0.0;
            return arr;
        }

        @Override
        public String toString() {
            return "Complejo";
        }
    };

    public abstract Double[] getBorder();
}
