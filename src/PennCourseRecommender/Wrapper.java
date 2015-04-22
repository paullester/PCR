package PennCourseRecommender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Wrapper {

    public Wrapper() {
    }
	
	//private static ArrayList<Course> courses = new ArrayList<Course>();
	
	public static void main(String[] args) {

        AllCourses a = new AllCourses();

        HashSet<String> stuCourses = new HashSet<String>();
        String input = "";
        Scanner in = new Scanner(System.in);
        while (!input.toLowerCase().equals("done")) {
        	System.out.println("Enter a course that you have taken (or enter \"Done\" if you are done): ");
        	input = in.nextLine();
        	stuCourses.add(input);
        	System.out.println();
        }
        Map<String,Course> feasibleCourses = a.getFeasibleCourses(stuCourses);
        Map<String,Double> courseScores = new HashMap<String,Double>();
        for (String course : feasibleCourses.keySet()) {
        	
        }
        
        /*
        System.out.println("feasible courses are");
        for(String s: feasibleCourses.keySet()){
           System.out.println(s);
        }
        a.closeJsonFile();
        
        //System.out.println("Hello World");
        a.openJsonFile();

        a.closeJsonFile();
        */
	}

    public static String apiCall(String arg) {
        return arg + " alksdjflasdkfjalsdfkjasldfj";
    }
}
