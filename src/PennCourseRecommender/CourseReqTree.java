package PennCourseRecommender;

import java.util.HashSet;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CourseReqTree {
	
	
	HashSet<CourseReqTree> children;
	String logicOp; //Can be AND, OR, or LEAF
	
	public CourseReqTree(){
		//
	}
	/*
	public CourseReqTree generateReqTree(JSONArray prereqs){
		
		logicOp = "LEAF"; //default logic operator
		
		
		
		
		if(prereqs!=null){
			Iterator<String> i = prereqs.iterator();
			while(i.hasNext()){
				
				CourseReqTree curChild;
				Object current = i.next();
				
				if(current.getClass() == JSONArray.class){
					curChild = new CourseReqTree();
					children.add(curChild);
					curChild.generateReqTree((JSONArray)current);
				} else if (current.toString() == "&&"){
					logicOp = "AND";
				} else if (current.toString() == "||"){
					logicOp = "OR";
				} else {
					curChild = new CourseReqTree();
					//implement thingy
					children.add(curChild);
					
				}
					
				
				
				if(tmp.getClass() != JSONArray.class){
				
				//String tmp = i.next().toString();
					String tmp2 = tmp.toString();
					if(courses.containsKey(tmp2)){
						prereqSet.add(courses.get(tmp2));
					} else {
						Course newCourse = new Course(tmp2);
						prereqSet.add(newCourse);
					}
					
				}
				
				//System.out.println(i.next().toString());
			}
			
			c.setPrereqs(prereqSet);
		}
	}
		
		
		/*
		PSEUDO CODE
		
		logicOp = LEAF
		
		while(hasnexttoken){
			if = json array, call currnet reqTree.add(generateReqTree (jsonarray))
		
			else (current reqTree.add 
		
		
		
		
		
		return null;
	} */

}
