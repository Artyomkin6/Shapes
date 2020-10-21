package data;

public class Ball implements Shape {

    private double radius;

    public Ball() {
        radius = 0;
    }

    public Ball(double r) {
        radius = r;
    }

    public double getRadius() {
        return radius;
    }

    public double volume() {
        return 4 * Math.PI * radius * radius * radius / 3;
    }

    public boolean isBeautiful() {
        return true;
    }

    @Override
    public String toString() {
        return "Ball (volume = " + volume() + ") is" +
                (isBeautiful() ? " " : " not ") + "beautiful";
    }

}
