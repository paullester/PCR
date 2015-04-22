package tmp;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            reader = new FileReader("allCourses.json"); 
            parser = new JSONParser;
            Object obj = parser.parse(reader);
            JSONObject jObj = (JSONObject) obj; 
            Iterator<JSONObject> keys = jObj.keys();
            Map<String, String[]> courseDesc = new HashMap<String, String[]>();
            genFeasDesc(keys, feasibleCourses, courseDesc);
            Map<String, Map<String, Integer>> courseCount = new HashMap<String, Map<String, Integer>>();
            countCourses(interests, courseCount, courseDesc);
            getScoresAndOrder(courseCount);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
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
            if (feasibleCourses.contains(course.get("course_id").toString())) {
                courseDesc.put(course.get("course_id").toString(), 
                        course.get("course_description").toString().split(" "));
            }

        }
    }

    private void countCourses(Set<String> interests, Map<String, Map<String, Integer>> courseCount, 
            Map<String, String[]> courseDesc) {
        Map<String, Integer> maxMap = new HashMap<String, Integer>();
        maxMap.put("max", 0);
        for (String interest : interests) {
            courseCount.put(interest, maxMap);
        }
        for (String course : courseDesc.keySet()) {
            String [] desc = courseDesc.get(course);
            for (String word : desc) {
                if (interests.contains(word)) {
                    int count = 0;
                    if (courseCount.containsKey(word)) {
                        if (courseCount.get(word).containsKey(course)) {
                            Map<String, Integer> tmp = courseCount.get(word);
                            count = tmp.get(course) + 1;
                            tmp.put(course, count);
                            courseCount.put(word, tmp);
                        } else {
                            Map<String, Integer> tmp = new HashMap<String, Integer>();
                            count = 1;
                            tmp.put(course, count);
                            courseCount.put(word, tmp);
                        }
                        if (count > courseCount.get(word).get("max")) {
                            Map<String, Integer> tmp = new HashMap<String, Integer>();
                            tmp.put("max", count);
                            courseCount.put(word, tmp);
                        } 
                    } else {
                        Map<String, Integer> tmp = new HashMap<String, Integer>();
                        tmp.put(course, 1);
                        courseCount.put(word, tmp);
                        Map<String, Integer> tmp2 = new HashMap<String, Integer>();
                        tmp2.put("max", 1);
                        courseCount.put(word, tmp2);
                    }
                }
            }
        }
    }

    private void getScoresAndOrder(Map<String, Map<String, Integer>> courseCount) {
        Map<Double, List<String>> tree = new TreeMap<Double, List<String>>(); 
        for (String interest : courseCount.keySet()) {
            Integer max = courseCount.get(interest).get("max");
            courseCount.get(interest).remove("max");
            for (String course : courseCount.get(interest).keySet()) {
                Integer count = courseCount.get(interest).get(course);
                Double weight = (0.5 + (0.5) * (double)(count / max)) 
                    * Math.log((double) count / (courseCount.get(interest).keySet().size()));
                if (tree.containsKey(weight)) {
                    List<String> tmp = tree.get(weight);
                    tmp.add(course);
                    tree.put(weight, tmp);
                } else {
                    List<String> tmp = new LinkedList<String>();
                    tmp.add(course);
                    tree.put(weight, tmp);
                }
            }
            List<String> orderedCourses = new LinkedList<String>();
            for (Double weight : ((TreeMap<Double, List<String>>) tree).descendingKeySet()) {
                orderedCourses.addAll(tree.get(weight));
            }
            ranked.put(interest, orderedCourses);
        }
    } 
}