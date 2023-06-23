import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TupleStorage {
    public static void main(String[] args) {
        ArrayList<Tuple<Boolean, Set<Tuple<Boolean, String>>>> tupleList = new ArrayList<>();

        // Creating tuples
        Tuple<Boolean, String> innerTuple1 = new Tuple<>(true, "Hello");
        Tuple<Boolean, String> innerTuple2 = new Tuple<>(false, "World");

        // Creating sets of inner tuples
        Set<Tuple<Boolean, String>> innerSet1 = new HashSet<>();
        innerSet1.add(innerTuple1);
        innerSet1.add(innerTuple2);

        Set<Tuple<Boolean, String>> innerSet2 = new HashSet<>();
        innerSet2.add(innerTuple1);

        // Creating outer tuples
        Tuple<Boolean, Set<Tuple<Boolean, String>>> outerTuple1 = new Tuple<>(true, innerSet1);
        Tuple<Boolean, Set<Tuple<Boolean, String>>> outerTuple2 = new Tuple<>(false, innerSet2);

        // Adding tuples to the list
        tupleList.add(outerTuple1);
        tupleList.add(outerTuple2);

        // Accessing tuple values
        Tuple<Boolean, Set<Tuple<Boolean, String>>> firstTuple = tupleList.get(0);
        boolean firstValue = firstTuple.getFirst();
        Set<Tuple<Boolean, String>> secondValue = firstTuple.getSecond();

        System.out.println("First tuple: " + firstTuple);
        System.out.println("First value: " + firstValue);
        System.out.println("Second value: " + secondValue);

        // Iterating over the tuples
        for (Tuple<Boolean, Set<Tuple<Boolean, String>>> tuple : tupleList) {
            boolean boolValue = tuple.getFirst();
            Set<Tuple<Boolean, String>> innerSet = tuple.getSecond();

            System.out.println("Outer tuple: " + tuple);
            System.out.println("Boolean value: " + boolValue);
            System.out.println("Inner set:");

            for (Tuple<Boolean, String> innerTuple : innerSet) {
                boolean innerBoolValue = innerTuple.getFirst();
                String innerStringValue = innerTuple.getSecond();
                System.out.println("  Inner tuple: " + innerTuple);
                System.out.println("  Inner boolean value: " + innerBoolValue);
                System.out.println("  Inner string value: " + innerStringValue);
            }
        }
    }
}

// Custom Tuple class
class Tuple<T1, T2> {
    private T1 first;
    private T2 second;

    public Tuple(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

