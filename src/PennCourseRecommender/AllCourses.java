package PennCourseRecommender;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
		
		
		
		
		
	}
	
	public HashMap<String, Course> initCourses(){
		
		HashMap<String, Course> courses = new HashMap<String, Course>();
		
		try {
			Object obj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			Set keys = jsonObject.keySet();
			Iterator<Object> e = keys.iterator();
			
			while(e.hasNext()){
				
				//fetch next course and put it into courses hashmap
				String s = e.next().toString();
				Course c;
				if(courses.containsKey(s)){
					c = courses.get(s);
				} else {
					c = new Course(s);
					courses.put(s, c);
				}
				
				
				/*TODO need to figure out how to handle complex prereqs. 
				
				//get the prereqs
				JSONObject jsonClassObj  = (JSONObject) jsonObject.get(s);
				JSONArray prereqs = (JSONArray) jsonClassObj.get("prerequisites");
				//JSONArray satisfyreqs = (JSONArray) jsonClassObj.get("satisfyReqsFor");
	
				HashSet<Course> prereqSet = new HashSet<Course>();

				
				
				System.out.println("Course is " +s);
				if(prereqs!=null){
					Iterator<String> i = prereqs.iterator();
					while(i.hasNext()){
						String tmp = i.next().toString();
						if(courses.containsKey(tmp)){
							prereqSet.add(courses.get(tmp));
						} else {
							Course newCourse = new Course(tmp);
							prereqSet.add(newCourse);
						}
						
						//System.out.println(i.next().toString());
					}
					
					c.setPrereqs(prereqSet);
				}*/
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
		
		return courses;
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
}

