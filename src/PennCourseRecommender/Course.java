package PennCourseRecommender;

import java.util.HashSet;
import java.util.Set;

public class Course {
	
	private String course_name;
	private Set<Course> pre_reqs;
	private Set<Course> descendants;
	private double pcr_score;
	private String course_desc;
	

	
	public Course(String s){
		
		course_name = s; 
		//put in code to get pcr score. 
		
	}



	public void setPrereqs(Set<Course> p){
		pre_reqs = p;
	}
	
}
