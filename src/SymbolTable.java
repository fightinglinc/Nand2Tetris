import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    // Key-Symbols, Values-Address
    private Map<String, Integer> map;

    public SymbolTable() {
        map = new HashMap<>();
        // Deal with 23 pre-defined labels
        // R0-R15
        for (int i = 0; i < 16; i++) {
            map.put("R" + String.valueOf(i), i);
        }
        map.put("SCREEN", 16384);
        map.put("KBD", 24576);
        map.put("SP", 0);
        map.put("LCL", 1);
        map.put("ARG", 2);
        map.put("THIS", 3);
        map.put("THAT", 4);
    }

    public void addEntry(String str, int value) {
        if (contains(str)) {
            throw new RuntimeException("Symbol '" + str + "' already used!");
        }
        map.put(str, value);
    }

    public boolean contains(String str) {
        return map.containsKey(str);
    }

    public int getValue(String str) {
        return map.get(str);
    }
}
