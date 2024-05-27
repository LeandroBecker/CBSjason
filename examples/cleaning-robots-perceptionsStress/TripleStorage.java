
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.HashSet;
//import java.util.List;
import java.util.Set;

public class TripleStorage {
    public static void main(String[] args) {
        ArrayList<Triple<Boolean, Integer, Set<Triple<Boolean, Integer, String>>>> TripleList = new ArrayList<>();

        // Creating Triples
        Triple<Boolean, Integer, String> innerTriple1 = new Triple<>(true, 1, "Hello");
        Triple<Boolean, Integer, String> innerTriple2 = new Triple<>(false, 1, "World");

        // Creating sets of inner Triples
        Set<Triple<Boolean, Integer, String>> innerSet1 = new HashSet<>();
        innerSet1.add(innerTriple1);
        innerSet1.add(innerTriple2);

        Set<Triple<Boolean, Integer, String>> innerSet2 = new HashSet<>();
        innerSet2.add(innerTriple1);

        // Creating outer Triples
        Triple<Boolean, Integer, Set<Triple<Boolean, Integer, String>>> outerTriple1 = new Triple<>(true, 1, innerSet1);
        Triple<Boolean, Integer, Set<Triple<Boolean, Integer, String>>> outerTriple2 = new Triple<>(false, 2, innerSet2);

        // Adding Triples to the list
        TripleList.add(outerTriple1);
        TripleList.add(outerTriple2);

        // Accessing Triple values
        Triple<Boolean, Integer, Set<Triple<Boolean, Integer, String>>> firstTriple = TripleList.get(0);
        boolean firstValue = firstTriple.getFirst();
        Set<Triple<Boolean, Integer, String>> secondValue = firstTriple.getThird();

        System.out.println("First Triple: " + firstTriple);
        System.out.println("First value: " + firstValue);
        System.out.println("Second value: " + secondValue);

        // Iterating over the Triples
        for (Triple<Boolean, Integer, Set<Triple<Boolean, Integer, String>>> Triple : TripleList) {
            boolean boolValue = Triple.getFirst();
            Set<Triple<Boolean, Integer, String>> innerSet = Triple.getThird();

            System.out.println("Outer Triple: " + Triple);
            System.out.println("Boolean value: " + boolValue);
            System.out.println("Inner set:");

            for (Triple<Boolean, Integer, String> innerTriple : innerSet) {
                boolean innerBoolValue = innerTriple.getFirst();
                String innerStringValue = innerTriple.getThird();
                System.out.println("  Inner Triple: " + innerTriple);
                System.out.println("  Inner boolean value: " + innerBoolValue);
                System.out.println("  Inner string value: " + innerStringValue);
            }
        }
    }
}

// Custom Triple class
class Triple<T1, T2, T3> {
    private T1 first;
    private T2 second;
    private T3 third;

    public Triple(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public T3 getThird() {
        return third;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }
}

