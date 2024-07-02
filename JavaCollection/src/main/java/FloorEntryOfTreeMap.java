// Java program to demonstrate the example
// of Map.Entry floorEntry(Key key_ele)
// method of TreeMap

import java.util.*;

public class FloorEntryOfTreeMap {

    public static boolean isParentEntry(final String path, final String parent) {
        if (!path.startsWith(parent)) {
            return false;
        }

        if (path.equals(parent)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        // Instantiates TreeMap
        TreeMap < String, String > tm = new TreeMap < String, String > ();

        tm.put("/HASH", "C");
        tm.put("/HASH/a/b", "C");
        tm.put("/HASH_ALL", "C++");
        tm.put("/RANDOM", "Java");
        tm.put("/SPACE", "Php");

        // Display Returned Key-Value Element
        System.out.println(tm.floorEntry("/HASH/a/b/c"));

        String path = "/HASH/a/b/c";

        Map.Entry<String, String> entry = tm.floorEntry(path);

        while (entry != null && !isParentEntry(path, entry.getKey())) {
            entry = tm.lowerEntry(entry.getKey());
        }
        if (entry == null) {
            System.out.println("null");
        }
        else
        System.out.println(entry.getValue());
    }


}