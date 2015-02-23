package multiattribute;
import java.util.ArrayList;



public class main {
	
	private static ArrayList<Course> courses = new ArrayList<Course>();
	
	public static void main(String[] args) {
			
			Course cis110 = new Course ("cis 110", 1, 10, 3.3, 2, 1);
			Course cis120 = new Course ("cis 120", 1, 9, 3.5, 2, 1);
			Course cis121 = new Course ("cis 121", 1, 8, 3.4, 2, 1);
			Course cis240 = new Course ("cis 240", 1, 7, 2.8, 2, 1);
			Course cis262 = new Course ("cis 262", 1, 3, 1.6, 2, 1);
			courses.add(cis110);
			courses.add(cis120);
			courses.add(cis121);
			courses.add(cis240);
			courses.add(cis262);
			
			
			printTop3Courses();
			
    }
	
	
	
	//super shit hacky way to print top 3 recommended courses. 
	public static void printTop3Courses(){
		
		ArrayList<Course> temp = new ArrayList<Course>();
		Course toPrint = new Course();
		temp = courses;
		double max=0;
		int i=0;
		
		while(i<3){
			max = 0;
			for (Course c: temp){
				if (c.getScore()>max){
					max = c.getScore();
					toPrint = c;
				}
			}
			
			System.out.println(toPrint.getCourseName() + ": "+toPrint.getScore());
			i++;
		}
		
	}
	
}
