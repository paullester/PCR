package PennCourseRecommender;

import java.util.Set;

public class Course {
	
	private String course_name;
	private Set<Course> pre_reqs;
	private Set<Course> descendants;
	private double pcr_score;
	private String course_desc;
	

}
