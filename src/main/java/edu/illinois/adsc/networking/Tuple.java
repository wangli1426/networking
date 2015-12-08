package edu.illinois.adsc.networking;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by robert on 12/8/15.
 */
public class Tuple implements Serializable {

    Integer[] integers;

    public Tuple() {
        int rand = new Random().nextInt(200)+200;
        integers = new Integer[Math.abs(rand)];
        integers = new Integer[new Random().nextInt(Math.abs(integers.length))];
    }

    public String toString() {
        return integers.length + "";
    }
}
