package data;

public class Cylinder implements Shape {

    private double radius;
    private double height;

    public Cylinder() {
        radius = height = 0;
    }

    public Cylinder(double len) {
        radius = len;
        height = len * 2;
    }

    public Cylinder(double r, double h) {
        radius = r;
        height = h;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

    public double volume() {
        return Math.PI * radius * radius * height;
    }

    public boolean isBeautiful() {
        return 2 * radius <= height;
    }

    @Override
    public String toString() {
        return "Cylinder (volume = " + volume() + ") is" +
                (isBeautiful() ? " " : " not ") + "beautiful";
    }

}
