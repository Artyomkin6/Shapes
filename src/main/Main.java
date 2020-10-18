package main;

import bag.Backpack;
import controller.Controller;
import data.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {



//        Scanner sc = new Scanner(System.in);
//
//        Cube fc = new Cube(12);
//        Ball fb = new Ball(3);
//        Cylinder fcyl = new Cylinder(10, 15);
//
//        Backpack bag = new Backpack(3);
//        try {
//            bag.put(fb);
//            bag.put(fcyl);
////            bag.put(fc);
//        } catch (RuntimeException ex) {
//            System.out.println(ex.toString());
//        }

        Controller app = new Controller();
        app.setVisible(true);
    }
}
