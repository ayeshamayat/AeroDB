import java.util.*;

public class Snapshot {
    private Integer id;
    private static LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<Integer>>> snapshotLHMap = new LinkedHashMap<>();

    Snapshot(Integer id, Entry entries) {
        this.id = id;
        this.entries = entries;
    }
    // I forgot classes exist omg

    public static void addToCurrentStateHMap(Entry e) {
        id = 1;
        while (currentStateHMap.containsKey(id)) {
            id++;
        }
        currentStateHMap.put(id, e);
        incrementId();
    }

    public Integer getId() {
        return this.id;
    }

    public void incrementId() {
        this.id++;
    }

    public void resetId(int i) {
        this.id = i;
    }

}
