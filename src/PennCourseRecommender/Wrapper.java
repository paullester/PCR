package PennCourseRecommender;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Wrapper {

    public Wrapper() {
    }
	
	//private static ArrayList<Course> courses = new ArrayList<Course>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		
		AllCourses a = new AllCourses();

        HashSet<String> stuCourses = new HashSet<String>();
        String input = "";
        Scanner in = new Scanner(System.in);
        String major = "";
        System.out.println("Enter your major (CIS or NETS): ");
    	major = in.nextLine().toUpperCase();
        while (!(major.equals("CIS") || major.equals("NETS"))) {
        	System.out.print("Sorry, that major is not supported. Enter your major (CIS or NETS): ");
        	major = in.nextLine().toUpperCase();
        }
        
        while (!input.equals("DONE")) {
        	System.out.print("Enter a course that you have taken (i.e. CIS120), or enter \"Done\" if you are done: ");
        	input = in.nextLine().toUpperCase().replaceAll(" ", "");
        	if(a.isACourse(input)) stuCourses.add(input);
        	else if (!input.equalsIgnoreCase("done")) System.out.println("Sorry, we do not have that course on file.");
        }
        
        System.out.print("Enter the subjects that you are interested in, separated by commas (i.e. \"astronomy, chemistry, marketing\"): ");
        String[] interestsArray = in.nextLine().split(", ");
        Set<String> interests = new HashSet<String>(Arrays.asList(interestsArray));
        
        Map<String,Course> feasibleCourses = a.getFeasibleCourses(stuCourses);
        Map<String,Double> courseScores = new HashMap<String,Double>();
        JSONParser parser = new JSONParser();
        
        TFIDF foo = new TFIDF(interests, feasibleCourses.keySet());
        Map<String, List<String>> rankedCoursesByInterests = foo.getRankedOrder();
        System.out.println(rankedCoursesByInterests.keySet());
        
        System.out.println("Major: " + major);
        
		JSONObject allScores =
				(JSONObject) parser.parse(new FileReader("/Users/BenGitles/Documents/School/Senior Design/PCR/src/descendantScores.json"));
		JSONObject descendantScores = (JSONObject) allScores.get(major);
		
		JSONObject pcrRatings =
				(JSONObject) parser.parse(new FileReader("/Users/BenGitles/Documents/School/Senior Design/PCR/src/penn_course_review_ratings.json"));
		
        for (String course : feasibleCourses.keySet()) {
        	Double interestScore = 1.0;
        	Double descendantScore = 1.0;
        	Double peerScore = 1.0;
        	Double pcrScore = 1.5;
        	
        	//interestScore
        	//Map<String, List<String>> rankedCoursesByInterests = new TFIDF(interests, feasibleCourses.keySet()).getRankedOrder();
        	
        	//System.out.print(", interest score: " + interestScore);
        	/*
        	if (((Set<String>) descendantScores.keySet()).contains(course)) {
        		descendantScore = 5.0 * ((Double) descendantScores.get(course) - 1) + 1;
        	}
        	*/
        	//System.out.print(", descendant score: " + descendantScore);
        	
        	//peerScore?
        	
        	//System.out.print(", peer score: " + peerScore);
        	
        	if (pcrRatings.containsKey(course)) {
        		pcrScore = ((Double) ((JSONObject) pcrRatings.get(course)).get("rCourseQuality")) / 4.0 + 1.0;
        	}
        	
        	//System.out.println(", PCR score: " + pcrScore);
        	
        	courseScores.put(course, interestScore * descendantScore * peerScore * pcrScore);
        }
        new PrintTopScores(courseScores);
        
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
