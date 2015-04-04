package TFIDF;

import java.util.Comparator;
import java.util.Map;
import java.lang.Double;

public class TFIDFComparator implements Comparator<String> {

    private Map<String, Double> map;

    public TFIDFComparator(Map<String, Double> map) {
        this.map = map;
    }

    @Override
    public int compare(String s1, String s2) {
        if (map.get(s1) >= map.get(s2)) {
            return 1;
        } else {
            return -1;
        }
    }
}