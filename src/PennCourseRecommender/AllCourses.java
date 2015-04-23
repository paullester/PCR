package PennCourseRecommender;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AllCourses {

	FileReader reader;
	String[] args;
	JSONParser parser;
	
	
	public AllCourses(){
		parser = new JSONParser();
		try {
			reader = new FileReader("/Users/BenGitles/Documents/School/Senior Design/PCR/src/courses.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public HashMap<String, Course> getFeasibleCourses(HashSet<String> stuCourses){
		

		HashMap<String, Course> courses = new HashMap<String, Course>();
		HashMap<String,Course> feasibleCourses = new HashMap<String, Course>();
		
		try {
			Object obj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			Set keys = jsonObject.keySet();
			Iterator<Object> e = keys.iterator();	
			
			while(e.hasNext() ){
				
				//fetch next course and put it into courses hashmap
				String s = e.next().toString();
				
				Course c;
				if(courses.containsKey(s)){
					c = courses.get(s);
				} else {
					c = new Course(s);
					courses.put(s, c);
				}

				//get the prereqs
				JSONObject jsonClassObj  = (JSONObject) jsonObject.get(s);
				JSONArray prereqs = (JSONArray) jsonClassObj.get("prerequisites");

				//System.out.println("Course: "+ s);
				if(prereqs ==null){
					//System.out.println("NO PREREQS BITCH");
					feasibleCourses.put(s, c);
				}else{
					CourseReqTree reqtree = new CourseReqTree();
					reqtree.generateReqTree(prereqs);
					if(reqtree.satisfyReq(stuCourses)){
						feasibleCourses.put(s,c);
					}
				}
				
			}
			
			
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
		
		return feasibleCourses;
	}
	
	public boolean isACourse(String s) throws IOException, ParseException {
		Object obj = parser.parse(new FileReader("/Users/BenGitles/Documents/School/Senior Design/PCR/src/courses.json"));
		JSONObject jsonObject = (JSONObject) obj;
		Set keys = jsonObject.keySet();
		return keys.contains(s);
	}
	
	public void openJsonFile(){
		try {
        	reader = new FileReader("courses.json");
        	System.out.println("JSON FILE SUCCESFULLY READ");
        } catch (IOException e) {
        			e.printStackTrace();
        			
        }
	}
	
	public void closeJsonFile(){
		try {
			reader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public Double tfidf(Set<String> interests, Set<String> feasibleCourses, String course) {
		Map<String, List<Map<String, Double>>> ranked = new HashMap<String, List<Map<String, Double>>>();
        try {
            FileReader myReader = new FileReader("/Users/BenGitles/Documents/School/Senior Design/PCR/src/allCourses.json"); 
            JSONParser myparser = new JSONParser;
            Object obj = myparser.parse(myreader);
            JSONObject jObj = (JSONObject) obj; 
            Iterator<JSONObject> keys = jObj.keys();
            Map<String, String[]> courseDesc = new HashMap<String, String[]>();
            genFeasDesc(keys, feasibleCourses, courseDesc);
            Map<String, Map<String, Integer>> courseCount = new HashMap<String, Map<String, Integer>>();
            countCourses(interests, courseCount, courseDesc);
            getScoresAndOrder(courseCount, ranked);
            return getScore(ranked, course);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }

    private Double getScore(Map<String, List<Map<String, Double>>> ranked, String course) {
    	Double ret = 0.0;
    	for (String interest : ranked.keySet()) {
    		for (Map<String, Double> map : ranked.get(interest)) {
    			for (String c : map.keySet()) {
    				if (c.equals(course)) {
    					ret += map.get(c);
    				}
    			}
    		}
    	}

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

    private void getScoresAndOrder(Map<String, Map<String, Integer>> courseCount, Map<String, List<Map<String, Double>>> ranked) {
        Map<Double, List<String>> tree = new TreeMap<Double, List<String>>(); 
        for (String interest : courseCount.keySet()) {
            Integer max = courseCount.get(interest).get("max");
            courseCount.get(interest).remove("max");
            for (String course : courseCount.get(interest).keySet()) {
                Integer count = courseCount.get(interest).get(course);
                Double weight = (0.5 + (0.5) * (double)(count / max)) 
                    * Math.log((double) count / (courseCount.get(interest).keySet().size()));
                weight++;
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
            List<Map<String, Double>> weights = new LinkedList<Map<String, Double>>();
            for (Double weight : ((TreeMap<Double, List<String>>) tree).descendingKeySet()) {
            	for (String c : tree.get(weight)) {
                	Map<String, Double> tmp = new HashMap<String, Double>();
                	tmp.put(c, weight);
                	weights.add(tmp);
                }
            }
            ranked.put(interest, weights);
        }
    } 
}

