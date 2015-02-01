import java.util.Hashmap;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Math;

// this should work - only issue is around the order things get added, test when interests are generated

public class TFIDF {

    private String interests = [];
    private Map<String, List> interestMap;

    public TFIDF() {
        interestMap = new Hashmap<String, List>();
        for (String interest : interests) {
            Map<String, int> courseCounts = new HashMap<String, int>();
            Map<String, double> courseWeightings = new HashMap<String, double>();
            TFIDFComparator comp = new TFIDFComparator(courseWeightings);
            Map<String, double> ordedCourseWeightings = new TreeMap<String, double>(comp);
            int totalCount = 0;
            int myCount = 0;
            BufferedReader reader = new BufferedReader(new FileReader("/Users/susangreenberg/Documents/PCR/courseDescription.txt"));
            String line = reader.line();
            int courseCount = 1;
            while (line != null) {
                String[] words = line.split(" ");
                for (int i = 1; i < words.length(); i++) {
                    if words[i].equals(interest) {
                        totalCount++;
                        myCount++;
                    }
                }
                courseCount.put(words[i], myCount);
                line = reader.line();
                courseCount++;
            }
            int max = 0;
            int coursesContaining = 0;
            for (int count : courseCounts.values()) {
                if (count > max) {
                    max = count;
                }
                if (count > 0) {
                    coursesContaining++;
                }
            }
            for (String course : courseCounts.keys()) {
                double weight = (0.5 + (0.5) * ((double)courseCounts.get(course) / (double)max)) 
                    * Math.log((double)(courseCount)/(double)(coursesContaining)));
                courseWeightings.put(course, weight);
            }
            ordedCourseWeightings.putAll(courseWeightings);
            interestMap.put(interest, new LinkedList<String>(ordedCourseWeightings.keySet()))
        }
    }

    public List get(String interest) {
        return interestsMap.get(interest);
    }
}