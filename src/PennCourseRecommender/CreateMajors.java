package PennCourseRecommender;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreateMajors {
	
	private Set<Major> allMajors;
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();

		JSONObject majorsAndGroups =
				(JSONObject) parser.parse(new FileReader("majors.json"));
		
		JSONObject majors = (JSONObject) majorsAndGroups.get("majors");
		JSONObject groups = (JSONObject) majorsAndGroups.get("groups");
		
		//iterate over each major
		for (String majorName : (Set<String>) majors.keySet()) {
			if (majorName.equals("BE")) {
				System.out.println("Working on major: " + majorName);
				Major m = new Major(majorName);
				JSONObject reqs = (JSONObject) majors.get(majorName);
				//iterate over each requirement
				for (String key : (Set<String>) reqs.keySet()) {
					if (key.equals("URL")) m.setURL((String) reqs.get("URL"));
					else if (key.equals("Notes")) {
						m.setNotes((Set<String>) ((JSONObject) reqs.get("Notes")).keySet());
					}
				}
				ReqTree tree = new ReqTree(reqs, groups);
				/*m.setRequirements(tree);
				m.setDescendantScoresWithStrings(tree.getDescendantScores());
				PrintWriter writer = new PrintWriter("majors-test.txt", "UTF-8");
				writer.println(tree.toString());
				writer.close();*/
				tree.getDescendantScores();
			}
			
			/*Set<String> coursesTaken = new HashSet<String>();
			coursesTaken.add("MATH104");
			coursesTaken.add("MATH114");
			coursesTaken.add("CIS160");
			coursesTaken.add("CIS261");
			coursesTaken.add("MATH697");
			coursesTaken.add("MATH104");
			coursesTaken.add("MATH104");
			coursesTaken.add("MATH104");
			coursesTaken.add("PHYS150");
			coursesTaken.add("PHYS151");
			coursesTaken.add("ASTR012");
			coursesTaken.add("ASTR111");
			coursesTaken.add("CIS110");
			coursesTaken.add("CIS120");
			coursesTaken.add("CIS121");
			coursesTaken.add("CIS240");
			coursesTaken.add("CIS262");
			coursesTaken.add("CIS320");
			coursesTaken.add("CIS277");
			coursesTaken.add("CIS371");
			coursesTaken.add("CIS380");
			coursesTaken.add("CIS400");
			coursesTaken.add("CIS401");
			coursesTaken.add("NETS150");
			coursesTaken.add("NETS312");
			coursesTaken.add("NETS412");
			coursesTaken.add("BE101");
			coursesTaken.add("BE99");
			coursesTaken.add("BE305");
			coursesTaken.add("TCOM298");
			coursesTaken.add("TCOM601");
			coursesTaken.add("TCOM400");
			coursesTaken.add("TCOM770");
			coursesTaken.add("COMM201");
			coursesTaken.add("COMM203");
			coursesTaken.add("COMM750");
			coursesTaken.add("COML998");
			coursesTaken.add("COML999");
			coursesTaken.add("COML556");
			coursesTaken.add("EAS203");
			
			System.out.println("Tree is satisfied: " + tree.isSatisfied(coursesTaken));*/
		}
	}
}
