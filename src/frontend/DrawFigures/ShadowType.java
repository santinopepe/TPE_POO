package frontend.DrawFigures;
import javafx.scene.paint.Color;

//Enum de los distintos tipos de sombra.
public enum ShadowType {
    NONE(0){

        @Override
        public double shadowCoordCalc(double coord, double axis) {
            return 0;
        }

        @Override
        public double rectCalc(double coord1, double coord2) {
            return 0;
        }
        public Color getColor(Color color) {
            return color;
        }

        @Override
        public String toString() {
            return "Ninguna";
        }
    },
    SIMPLE(10.0){

        @Override
        public double shadowCoordCalc(double coord, double axis) {
            return coord - axis/2 + offSet;
        }

        @Override
        public double rectCalc(double coord1, double coord2) {
            return Math.abs(coord1 - coord2);
        }
        public Color getColor(Color color) {
            return Color.GRAY;
        }
        @Override
        public String toString() {
            return "Simple";
        }
    },
    COLOURED(10.0){

        @Override
        public double shadowCoordCalc(double coord, double axis) {
            return coord - axis/2 + offSet;
        }

        @Override
        public double rectCalc(double coord1, double coord2) {
            return Math.abs(coord1 - coord2);
        }
        public Color getColor(Color color) {
            return color.darker();
        }
        @Override
        public String toString() {
            return "Coloreada";
        }
    },
    SIMPLE_INVERSED(-10.0){

        @Override
        public double shadowCoordCalc(double coord, double axis) {
            return coord - axis/2 + offSet;
        }

        @Override
        public double rectCalc(double coord1, double coord2) {
            return Math.abs(coord1 - coord2);
        }

        @Override
        public Color getColor(Color color) {
            return Color.GRAY;
        }
        @Override
        public String toString() {
            return "Simple Inversa";
        }
    },
    COLOURED_INVERSED(-10.0){

        @Override
        public double shadowCoordCalc(double coord, double axis) {
            return coord - axis/2 + offSet;
        }

        @Override
        public double rectCalc(double coord1, double coord2) {
            return Math.abs(coord1 - coord2);
        }

        @Override
        public Color getColor(Color color) {
            return color.darker();
        }
        @Override
        public String toString() {
            return "Coloreada Inversa";
        }
    };

    final double offSet;

    ShadowType(double offSet){
        this.offSet = offSet;
    }

    public abstract double shadowCoordCalc(double coord, double axis);
    public abstract double rectCalc(double coord1, double coord2);
    public abstract Color getColor(Color color);

}
