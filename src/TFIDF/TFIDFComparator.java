package src.TFIDF;

import java.util.Map;

public class TFIFDComparator implements Comparator<String> {

    private Map<String, double> map;

    public TFIFDComparator(Map<String, double> map) {
        this.map = map;
    }

    @override
    public int compare(String s1, String s2) {
        if (map.get(s1) >= map.get(s2)) {
            return 1;
        } else {
            return -1;
        }
    }
}