package functions;

import data.Shape;
import java.util.List;

public class Functions {

    public static int binarySearch(List<Shape> shapes, Shape figure) {
        int l = 0;
        int r = shapes.size();
        while (r > l) {
            int currentPos = (r + l)/2;
            if (figure.volume() < shapes.get(currentPos).volume()) {
                l = currentPos + 1;
            } else {
                r = currentPos;
            }
        }
        return l;
    }

}
