import java.io.FileReader;

import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TFIDF {

    private FileReader reader;
    private JSONParser parser;
    private Map<String, List<String>> ranked;

    // Instanitate with keyset of feasible courses
    public TFIDF(Set<String> interests, Set<String> feasibleCourses) {
        try {
            reader = new FileReader("allCourses.json") //set path?
            parser = new JSONParser;
            Object obj = parser.parse(reader);
            JSONObject jObj = (JSONObject) obj; 
            Iterator<JSONObject> keys = jObj.keys();
            Map<String, String[]> courseDesc = new HashMap<String, String>();
            genFeasDesc(keys, feasibleCourses, courseDesc);
            Map<String, Map<String, int>> courseCount = new HashMap<String, Map<String, int>>();
            countCourses(interests, courseCount, courseDesc);
            getScoresAndOrder(courseCount);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getRankedOrder() {
        return ranked;
    }

    private void genFeasDesc(Iterator<JSONObject> keys, Set<String> feasibleCourses, 
            Map<String, String[]> courseDesc) {
        while (keys.hasNext) {
            JSONObject course = keys.next();
            if (feasiblecourse.contains(course.get("course_id").toString()) {
                courseDesc.put(course.get("course_id").toString(), 
                        course.get("course_description").toString().split(" "));
            }

        }
    }

    private void countCourses(Set<String> interests, Map<String, Map<String, int>> courseCount, 
            Map<String, String[]> courseDesc) {
        Map<String, int> maxMap = new HashMap<String, int>();
        maxMap.put("max", 0);
        for (String interest : interests) {
            courseCount.put(interest, maxMap);
        }
        for (String course : courseDesc.keySet()) {
            String [] desc = courseDesc.get(course);
            for (String word : desc) {
                if (interests.contains(word)) {
                    int count = 0;
                    if (courseCount.contains(word)) {
                        if (courseCount.get(word).contains(course)) {
                            Map<String, int> tmp = courseCount.get(word);
                            count = tmp.get(course) + 1;
                            tmp.put(course, count);
                            courseCount.put(word, tmp);
                        } else {
                            Map<String, int> tmp = new HashMap<String, int>();
                            count = 1;
                            tmp.put(course, count);
                            courseCount.put(word, tmp);
                        }
                        if (count > courseCount.get(word).get("max")) {
                            Map<String, int> tmp = new Hashmap<String, int>();
                            tmp.put("max", count);
                            courseCount.put(word, tmp);
                        } 
                    } else {
                        Map<String, int> tmp = new HashMap<String, int>();
                        tmp.put(course, 1);
                        courseCount.put(word, tmp);
                        Map<String, int> tmp2 = new HashMap<String, int>();
                        tmp2.put("max", 1);
                        courseCount.put(word, tmp2);
                    }
                }
            }
        }
    }

    private void getScoresAndOrder(Map<String, Map<String, int>> courseCount) {
        Map<double, List<String>> tree = new TreeMap<Double, List<String>>(); 
        for (String interest : courseCount.keySet()) {
            int max = courseCount.get(interest).get("max");
            courseCount.get(interest).remove("max");
            for (String course : courseCount.get(interest).keySet()) {
                int count = courseCount.get(interest).get(course);
                double weight = (0.5 + (0.5) * (double)(count / max)) 
                    * Math.log((double) count / (courseCount.get(interest).keySet().length));
                if (tree.contains(weight)) {
                    List<String> tmp = tree.get(weight);
                    tmp.add(course);
                    tree.put(weight, tmp);
                } else {
                    List<String> tmp = new List<String>();
                    tmp.add(course);
                    tree.put(weight, tmp);
                }
            }
            List<String> orderedCourses = new LinkedList<String>();
            for (double weight : tree.descendingKeySet()) {
                orderedCourses.add(tree.get(weight));
            }
            ranked.put(interest, orderedCourses);
        }
    } 
}