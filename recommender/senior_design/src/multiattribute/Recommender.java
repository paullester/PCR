package multiattribute;

public class Recommender {
	
	
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
	
	
	private String course_name;
	private int feasibility;
	private int descendants; 
	private double pcr; 
	private int student_interested;
	private double adsorption;
	
	
	
	public Recommender(String name, int f, int d, double p, int s, double a){
		
		course_name = name;
		feasibility = f; 
		descendants = d; 
		pcr = p;
		student_interested = s; 
		adsorption = a;

	}
	
	public Recommender(){
			
			course_name = "";
			feasibility = 0; 
			descendants = 0; 
			pcr = 0;
			student_interested = 0; 
			adsorption = 0;
	
		}
	
	//the "weighted" score
	public double getScore(){
		return feasibility*descendants*pcr*student_interested*adsorption;
	}
	
	//getters
	
	public String getCourseName(){
		return course_name;
	}
	
	public int getFeasibility(){
		return feasibility;
	}
	
	public int getDescendants(){
		return descendants;
	}
	
	public double getPcr(){
		return pcr;
	}
	
	public int getInterest(){
		return student_interested;
	}
	
	public double getAdsorption(){
		return adsorption;
	}
		
	
	//setters
	public void setCourseName(String n){
		course_name = n;
	}
	
	public void setFeasibility(int f){
		feasibility = f;
	}
	
	public void setDescendants(int d){
		descendants = d;
	}
	
	public void setPcr(double p){
		pcr = p;
	}
	
	public void setInterest(int s){
		student_interested = s; 
	}
	
	public void setAdsorption(double a){
		adsorption = a;
	}
	
	

}
