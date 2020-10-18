package data;

public class Cube implements Shape {

    private double length;

    public Cube() {
        length = 0;
    }

    public Cube(double l) {
        length = l;
    }

    public double volume() {
        return length * length * length;
    }

    public boolean isBeautiful() {
        return length >= 2. && length <= 5.;
    }

    @Override
    public String toString() {
        return "Cube (volume = " + volume() + ") is" +
                (isBeautiful() ? " " : " not ") + "beautiful";
    }

}
