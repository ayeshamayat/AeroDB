import java.io.*;
import java.util.*;

public class AeroDB {

    private static String[] input;
    private static String command;
    private static int inputValue;

    private static int snapshotNumber = 1;

    private static final String HELP = "BYE   clear database and exit\n" + "HELP  display this help message\n" + "\n"
            + "LIST KEYS       displays all keys in current state\n"
            + "LIST ENTRIES    displays all entries in current state\n"
            + "LIST SNAPSHOTS  displays all snapshots in the database\n" + "\n" + "GET <key>    displays entry values\n"
            + "DEL <key>    deletes entry from current state\n"
            + "PURGE <key>  deletes entry from current state and snapshots\n" + "\n"
            + "APPEND <key> <value ...>  appends values to the back\n" + "\n"
            + "PICK <key> <index>   displays value at index\n"
            + "PLUCK <key> <index>  displays and removes value at index\n"
            + "POP <key>            displays and removes the front value\n" + "\n" + "DROP <id>      deletes snapshot\n"
            + "ROLLBACK <id>  restores to snapshot and deletes newer snapshots\n"
            + "CHECKOUT <id>  replaces current state with a copy of snapshot\n"
            + "SNAPSHOT       saves the current state as a snapshot\n" + "\n" + "MIN <key>  displays minimum value\n"
            + "MAX <key>  displays maximum value\n" + "SUM <key>  displays sum of values\n"
            + "LEN <key>  displays number of values\n" + "\n" + "REV <key>   reverses order of values\n"
            + "UNIQ <key>  removes repeated adjacent values\n" + "SORT <key>  sorts values in ascending order\n" + "\n"
            + "DIFF <key> <key ...>   displays set difference of values in keys\n"
            + "INTER <key> <key ...>  displays set intersection of values in keys\n"
            + "UNION <key> <key ...>  displays set union of values in keys\n"
            + "CARTPROD <key> <key ...>  displays set union of values in keys\n";

    public static void bye() {
        System.out.println("bye");
        System.exit(0);
    }

    public static void help() {
        System.out.println(HELP);
    }

    public static void ok() {
        System.out.println("ok\n");
    }

    public static void noKey() {
        System.out.println("no such key\n");
    }

    public static void noSnapshots() {
        System.out.println("no snapshots\n");
    }

    public static void noSuchSnapshot() {
        System.out.println("no such snapshot\n");
    }

    public static void indexOutOfRange() {
        System.out.println("index out of range\n");
    }

    public static void invalidCommand() {
        System.out.println(
                "invalid Command;\n<value ...> must be an integer between -2,147,483,648 and 2,147,483,647 :)\n");
    }

    public static void moreInput() {
        System.out.println("further commands required, type 'HELP' for help\n");
    }

    public static void getValue(String k) {
        try {
            ArrayList<Integer> arr = entriesLHMap.get(k);
            arr.trimToSize();
            if (arr.size() == 0) {
                System.out.println("[]");
            } else if (arr.size() > 0) {
                System.out.print("[");
                int i = 0;
                while (i < arr.size() - 1) {
                    System.out.print(arr.get(i) + " ");
                    i++;
                }
                System.out.println(arr.get(arr.size() - 1) + "]");
            }
        } catch (NullPointerException e) {
            System.out.println("no such key"); // need this to be without "\n" at the end to support "LIST ENTRIES"
        }
    }

    public static void printSet(Set<Integer> set) {
        Object[] arr = set.toArray();
        if (arr.length == 0) {
            System.out.println("[]\n");
        } else {
            System.out.print("[");
            int i = 0;
            while (i < arr.length - 1) {
                System.out.print(arr[i] + " ");
                i++;
            }
            System.out.println(arr[arr.length - 1] + "]\n");
        }
    }

    public static void printArrayList(ArrayList<Integer> arr) {
        if (arr.size() == 0) {
            System.out.println("[]");
        } else {
            System.out.print("[");
            int i = 0;
            while (i < arr.size() - 1) {
                System.out.print(arr.get(i) + " ");
                i++;
            }
            System.out.println(arr.get(arr.size() - 1) + "]");
        }
    }

    public static void printCartProd(ArrayList<Integer> arr) {
        if (arr.size() == 0) {
            System.out.println("[]");
        } else {
            System.out.print("[");
            int i = 0;
            while (i < arr.size() - 1) {
                System.out.print(arr.get(i) + " ");
                i++;
            }
            System.out.print(arr.get(arr.size() - 1) + "] ");
        }
    }

    public static ArrayList<Integer> inputValues(String[] command) {
        ArrayList<Integer> inputValuesArrayList = new ArrayList<>();
        int i = 1, value;
        while (i < command.length) {
            value = Integer.parseInt(command[i]);
            inputValuesArrayList.add(value);
            i++;
        }
        inputValuesArrayList.trimToSize();
        return inputValuesArrayList;
    }

    public static void addToLinkedListEntry(Entry e) {
        if (Entry.getHeader().getNext().getKey() == null) {
            Entry.getTrailer().setPrevious(e);
            e.setNext(Entry.getTrailer());
            Entry.getHeader().setNext(e);
            e.setPrevious(Entry.getHeader());
        } else {
            Entry.getHeader().getNext().setPrevious(e);
            e.setPrevious(Entry.getHeader());
            e.setNext(header.getNext());
            e.setPrevious(Entry.getHeader());
        }
    }

    public static void minValue(ArrayList<Integer> values) {
        int min = values.get(0); // makes min value the first in the list
        for (int v : values) {
            if (v < min) {
                min = v;
            }
        }
        System.out.println(min + "\n");
    }

    public static void maxValue(ArrayList<Integer> values) {
        int max = values.get(0); // makes max value the first in the list
        for (int v : values) {
            if (v > max) {
                max = v;
            }
        }
        System.out.println(max + "\n");
    }

    public static void setUpLinkedList(Entry header, Entry trailer) {
        header.setNext(trailer);
        trailer.setPrevious(header);
    }

    public static void sumValues(ArrayList<Integer> values) {
        int sum = 0;
        for (int v : values) {
            sum = sum + v;
        }
        System.out.println(sum + "\n");
    }

    public static ArrayList<Integer> revValues(ArrayList<Integer> values) {
        ArrayList<Integer> newArrayList = new ArrayList<>();
        for (int v : values) {
            newArrayList.add(0, v);
        }
        return newArrayList;
    }

    public static ArrayList<Integer> uniqValues(ArrayList<Integer> values) {
        ArrayList<Integer> uniqValues = new ArrayList<>();
        int i = 0, prev, curr = values.get(0);
        uniqValues.add(curr);
        i++;
        while (i < values.size()) {
            curr = values.get(i);
            prev = values.get(i - 1);
            if (!(curr == prev)) {
                uniqValues.add(curr);
            }
            i++;
        }
        return uniqValues;
    }

    public static Set<Integer> unionSet(String[] input) {
        Set<Integer> setA = new HashSet<>(entriesLHMap.get(input[1])), setB;
        int i = 2;
        while (i < input.length) {
            setB = new HashSet<>(entriesLHMap.get(input[i]));
            setA.addAll(setB);
            i++;
        }
        return setA;
    }

    public static Set<Integer> intersectionSet(String[] input) {
        Set<Integer> setA = new HashSet<>(entriesLHMap.get(input[1])), setB;
        int i = 2;
        while (i < input.length) {
            setB = new HashSet<>(entriesLHMap.get(input[i]));
            setA.retainAll(setB);
            i++;
        }
        return setA;
    }

    public static ArrayList<ArrayList<Integer>> cartProdTwoSets(ArrayList<Integer> listA, ArrayList<Integer> listB) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        for (int j = 0; j < listA.size(); j++) {
            int valA = listA.get(j);

            for (int k = 0; k < listB.size(); k++) {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.add(valA);
                int valB = listB.get(k);
                temp.add(valB);
                result.add(temp);
            }
        }
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        setUpLinkedList(header, trailer);

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            input = scan.nextLine().split(" ");
            command = input[0].toUpperCase();

            if (command.equals("BYE")) {
                bye();
            } else if (command.equals("HELP")) {
                System.out.println(HELP);
            }

            else if (command.equals("LIST")) {
                command = input[1].toUpperCase();

                if (command.equals("KEYS")) {
                    ArrayList<String> keyList = new ArrayList<>(entriesLHMap.keySet());
                    if (keyList.isEmpty()) {
                        System.out.println("no keys\n");
                    } else {
                        for (int i = keyList.size() - 1; i >= 0; i--) {
                            String key = keyList.get(i);
                            System.out.println(key);
                        }
                        System.out.println();
                    }
                } else if (command.equals("ENTRIES")) {
                    if (entriesLHMap.isEmpty()) {
                        System.out.println("no entries\n");
                    } else {
                        ArrayList<String> keyList = new ArrayList<>(entriesLHMap.keySet());
                        String key;
                        for (int i = keyList.size() - 1; i >= 0; i--) {
                            key = keyList.get(i);
                            System.out.print(key + " ");
                            getValue(key);
                        }
                        System.out.println();
                    }
                } else if (command.equals("SNAPSHOTS")) {
                    if (snapshotLHMap.isEmpty()) {
                        noSnapshots();
                    } else {
                        ArrayList<Integer> snapshotList = new ArrayList<>(snapshotLHMap.keySet());
                        int snap;
                        for (int i = snapshotList.size() - 1; i >= 0; i--) {
                            snap = snapshotList.get(i);
                            System.out.println(snap);
                        }
                        System.out.println();
                    }
                }
            } // end of "LIST"

            else if (command.equals("GET")) {
                if (input.length > 1) {
                    String key = input[1];
                    getValue(key);
                    System.out.println(); // keep, don't change
                } else {
                    indexOutOfRange();
                }
            }

            else if (command.equals("SET")) {
                ArrayList<Integer> values = new ArrayList<>();
                String newKey;
                int newValue;
                try {
                    newKey = input[1];
                    int i = 2;
                    while (i < input.length) {
                        newValue = Integer.parseInt(input[i]);
                        values.add(newValue);
                        i++;
                    }
                    values.trimToSize();
                    Entry e = new Entry(newKey, values);
                    if (entriesLHMap.containsKey(newKey)) {
                        entriesLHMap.replace(newKey, values);
                        ok();
                        /// OTHER METHODS FOR FIXING ITS VALUE IN THE LINKED LIST and SNAPSHOT
                        continue;
                    }
                    addToLinkedListEntry(e);
                    Entry.addToEntriesLHMap(newKey, values);
                    Snapshot.addToCurrentStateHMap(e);
                    ok();
                } catch (IndexOutOfBoundsException e) {
                } catch (IllegalArgumentException a) {
                    invalidCommand();
                }
            }

            else if (command.equals("DEL")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        entriesLHMap.remove(key);
                        ok();
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException e) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("POP")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        if (values.size() == 0) {
                            System.out.println("nil\n");
                        } else {
                            int s = values.get(0);
                            values.remove(0);
                            entriesLHMap.replace(key, values);
                            System.out.println(s + "\n");
                        }
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException e) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("PICK")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        int index = Integer.valueOf(input[2]) - 1;
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        if ((index >= 0) && (index < values.size())) {
                            System.out.println(values.get(index) + "\n");
                        } else {
                            System.out.println("index out of range\n");
                        }
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    noKey();
                } catch (IncompatibleClassChangeError c) {
                    invalidCommand();
                }
            }

            else if (command.equals("PUSH")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        int i = 2, inputValue;
                        ArrayList<Integer> values;
                        while (i < input.length) {
                            inputValue = Integer.parseInt(input[i]);
                            values = entriesLHMap.get(key);
                            values.add(0, inputValue);
                            i++;
                        }
                        ok();
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("PLUCK")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        int index = Integer.valueOf(input[2]) - 1;
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        if ((index >= 0) && (index < values.size())) {
                            System.out.println(values.get(index) + "\n");
                            values.remove(index);
                        } else {
                            System.out.println("index out of range\n");
                        }
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    noKey();
                } catch (IncompatibleClassChangeError c) {
                    invalidCommand();
                }
            }

            else if (command.equals("APPEND")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        int i = 2, inputValue;
                        ArrayList<Integer> values;
                        while (i < input.length) {
                            inputValue = Integer.parseInt(input[i]);
                            values = entriesLHMap.get(key);
                            values.add(inputValue);
                            i++;
                        }
                        ok();
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    noKey();
                } catch (IncompatibleClassChangeError c) {
                    invalidCommand();
                }
            }

            else if (command.equals("LEN")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        int l = values.size();
                        System.out.println(l + "\n");
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("MIN")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        if (values.size() > 0) {
                            minValue(values);
                            continue;
                        } else {
                            noKey();
                        }
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("MAX")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        if (values.size() > 0) {
                            maxValue(values);
                            continue;
                        } else {
                            noKey();
                        }
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("SUM")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        sumValues(values);
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("REV")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        ArrayList<Integer> newValues = revValues(values);
                        entriesLHMap.replace(key, newValues);
                        ok();
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("SORT")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        Collections.sort(values);
                        // ArrayList<Integer> newValues = sortValues(values);
                        entriesLHMap.replace(key, values);
                        ok();
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    indexOutOfRange();
                }
            }

            else if (command.equals("UNIQ")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        ArrayList<Integer> values = entriesLHMap.get(key);
                        ArrayList<Integer> newValues = uniqValues(values);
                        entriesLHMap.replace(key, newValues);
                        ok();
                    } else {
                        noKey();
                    }
                } catch (IndexOutOfBoundsException i) {
                    moreInput();
                }
            }

            else if (command.equals("PURGE")) {
                try {
                    String key = input[1];
                    if (entriesLHMap.containsKey(key)) {
                        entriesLHMap.remove(key);
                        // LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<Integer>>>
                        // snapshotToInspect;
                        LinkedHashMap<String, ArrayList<Integer>> entriesToInspect;
                        int i = 1;
                        while (i <= snapshotLHMap.size()) {
                            entriesToInspect = snapshotLHMap.get(i);
                            if (entriesToInspect.containsKey(key)) {
                                entriesToInspect.remove(key);
                            }
                            i++;
                        }
                        ok();
                    } else {
                        ok();
                    }
                } catch (IndexOutOfBoundsException i) {
                    moreInput();
                }
            }

            else if (command.equals("SNAPSHOT")) {
                LinkedHashMap<String, ArrayList<Integer>> snapThis = new LinkedHashMap<String, ArrayList<Integer>>(
                        entriesLHMap);
                snapshotLHMap.put(snapshotNumber, snapThis);
                System.out.println("saved as snapshot " + snapshotNumber + "\n");
                snapshotNumber++;
            }

            else if (command.equals("DROP")) {
                try {
                    int key = Integer.parseInt(input[1]), size = snapshotLHMap.size();
                    if (snapshotLHMap.containsKey(key)) {
                        if (size == key) {
                            snapshotLHMap.remove(key);
                        } else {
                            LinkedHashMap<String, ArrayList<Integer>> temp;
                            while (key < size) {
                                temp = snapshotLHMap.get(key + 1);
                                snapshotLHMap.replace(key, temp);
                                key++;
                            }
                            snapshotLHMap.remove(key);
                        }
                        ok();
                    } else {
                        noSuchSnapshot();
                    }
                } catch (IndexOutOfBoundsException i) {
                    moreInput();
                }
            }

            else if (command.equals("CHECKOUT")) {
                try {
                    int key = Integer.parseInt(input[1]);
                    if (snapshotLHMap.containsKey(key)) {
                        entriesLHMap = new LinkedHashMap<String, ArrayList<Integer>>(snapshotLHMap.get(key));
                        ok();
                    } else {
                        noSuchSnapshot();
                    }
                } catch (IndexOutOfBoundsException i) {
                    moreInput();
                }
            }

            else if (command.equals("ROLLBACK")) {
                try {
                    int key = Integer.parseInt(input[1]);
                    int snapshotNumberToReturnTo = Integer.valueOf(input[1]);
                    if (snapshotLHMap.containsKey(key)) {
                        entriesLHMap = snapshotLHMap.get(key);
                        key++;
                        while (key <= snapshotNumber) {
                            snapshotLHMap.remove(key);
                            key++;
                        }
                        snapshotNumber = snapshotNumberToReturnTo + 1;
                        ok();
                    } else {
                        noSuchSnapshot();
                    }
                } catch (IndexOutOfBoundsException i) {
                    moreInput();
                }
            }

            else if (command.equals("UNION")) {
                try {
                    Set<Integer> union = unionSet(input);
                    printSet(union);
                } catch (IndexOutOfBoundsException e) {
                    moreInput();
                }
            }

            else if (command.equals("INTER")) {
                try {
                    Set<Integer> intersection = intersectionSet(input);
                    printSet(intersection);
                } catch (IndexOutOfBoundsException e) {
                    moreInput();
                }
            }

            // Does not work correctly for >2 sets
            else if (command.equals("DIFF")) {
                try {
                    Set<Integer> setToKeep = new HashSet<>(), setAOriginal = new HashSet<>(entriesLHMap.get(input[1])),
                            setACopy = new HashSet<>(), setBOriginal, setBCopy;

                    int i = 2;
                    while (i < input.length) {
                        setACopy.addAll(setAOriginal);

                        setBOriginal = new HashSet<>(entriesLHMap.get(input[i]));
                        setBCopy = new HashSet<>(setBOriginal);

                        setACopy.removeAll(setBOriginal);
                        setBCopy.removeAll(setAOriginal);
                        setToKeep.addAll(setACopy);
                        setToKeep.addAll(setBCopy);

                        setAOriginal = new HashSet<>(setToKeep);
                        i++;
                    }
                    printSet(setToKeep);
                } catch (IndexOutOfBoundsException e) {
                    moreInput();
                }
            }

            // Does not work correctly for >2 sets
            else if (command.equals("CARTPROD")) {
                if (input.length < 3) {
                    moreInput();
                    continue;
                } else if (input.length == 3) {
                    ArrayList<ArrayList<Integer>> result = cartProdTwoSets(entriesLHMap.get(input[1]),
                            entriesLHMap.get(input[2]));
                    System.out.print("[ ");
                    for (int i = 0; i < result.size(); i++) {
                        printCartProd(result.get(i));
                    }
                    System.out.println("]\n");
                } else {
                }
            }

        } // while loop
    } // main
} // class