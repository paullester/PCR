package src.PennCourseRecommender;

import java.util.List;


//src.TFIDF.TFIDF MODULE


public interface InterestScore {
	
	//takes in a course and a student. Based on the student's interests and course desc, use TFIDF to return normalized score betwee 0 and 1.
	public double getInterestScore(Course c, Student s);

}
