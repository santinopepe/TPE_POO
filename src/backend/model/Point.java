package backend.model;

public class Point {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void moveX(double diff) {
        this.x += diff;
    }

    public void moveY(double diff) {
        this.y += diff;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }


    public Point displacePoint() {
        return new Point(getX() + 10, getY() + 10);
    }
}
