package PennCourseRecommender;

import java.util.List;
import java.util.Set;

public class PennCourseRecommender implements Recommender{
	
	
	//////////////
	//	Multi attribute model weights that Paul and Ben invented 
	//	
	//	Feasilbility
	//	Binary: 0 or 1
	//	If the course is not feasible, then it will get a score of 0.
	//
	//	Number of Descendants
	//	Normalized in Range [1,10]
	//	The course that is a prerequisite for the most courses within the student’s majors and minors gets a score of 10; a course that is not a pre- requisite for any other gets a 1.
	//	
	//	Penn Course Review Rating
	//	Normalized in Range [1,4]
	//	The lowest rated course gets a 1; the highest rated course gets a 4.
	//
	//	Student Interests
	//	Binary: 1 or 2
	//	If the course is within the student’s interests, it gets a 2; otherwise, it gets a 1.
	//
	//	Adsorption
	//	Normalized in Range [1,2]
	//	The resulting probabilities from running the adsorption algorithm are normalized on the range [1,2].
	//////
	
	
	private Set<Course> feasibleCourses;
	private Student current_student;
	
	
	
	public PennCourseRecommender(){
		//PeerScore peerScorer = new PeerScore();	
		
		//method here to set feasible courses

	}
	
	
	
	//the "weighted" score
	public double getScore(Course c){
		return 0;
		//return feasibility*descendants*pcr*student_interested*adsorption;
	}
	
	
	public void getFeasibleCourses(){
		//some algorithm to calculate feasible courses. 
		
	}



	@Override
	public List<Course> getRecommendedCourses() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
