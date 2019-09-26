import java.util.*;

public class Entry {
    private static String key;
    private static ArrayList<Integer> value;
    private static Entry next;
    private static Entry previous;
    private static Entry header = new Entry(null, null);
    private static Entry trailer = new Entry(null, null);
    private static LinkedHashMap<String, ArrayList<Integer>> entriesLHMap = new LinkedHashMap<>(); // current state

    public Entry(String key, ArrayList<Integer> value) {
        this.key = key;
        this.value = value;
    }

    public void setNext(Entry e) {
        this.next = e;
    }

    public void setPrevious(Entry e) {
        this.previous = e;
    }

    public Entry getNext() {
        return this.next;
    }

    public Entry getPrevious() {
        return this.previous;
    }

    public String getKey() {
        return this.key;
    }

    public Entry getHeader() {
        return this.header;
    }

    public Entry getTrailer() {
        return this.trailer;
    }

    public LinkedHashMap<String, ArrayList<Integer>> getEntriesLHMap() {
        return this.entriesLHMap;
    }

    public void addToEntriesLHMap(String s, ArrayList<Integer> arrList) {
        this.entriesLHMap.put(s, arrList);
    }

}