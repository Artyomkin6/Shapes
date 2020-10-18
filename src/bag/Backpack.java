package bag;

import java.util.ArrayList;
import java.util.List;
import data.Shape;
import exceptions.BackpackOverfullingException;
import functions.Functions;

public class Backpack {

    private double size = 0;
    private double capacity;
    private List<Shape> shapes;

    public Backpack() {
        this(100);
    }

    public Backpack(int initialCapacity) {
        capacity = initialCapacity;
        shapes = new ArrayList<>();
    }

    public int put(Shape figure) {
        if (figure.volume() + size <= capacity) {
            int index = Functions.binarySearch(shapes, figure);
            shapes.add(index, figure);
            size += figure.volume();
            return index;
        } else {
            throw new BackpackOverfullingException("The backpack is full");
        }
    }

    public void delete(int index) {
        if (!shapes.isEmpty()) {
            size -= (shapes.remove(index)).volume();
        }
    }

    public void clear() {
        shapes.clear();
        size = 0;
    }



    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Size = " + size + "\n");

        for (int i = 0; i < shapes.size(); ++i) {
            result.append(i).append(": ").append(shapes.get(i).toString()).append('\n');
        }

        return result.toString();
    }

}
