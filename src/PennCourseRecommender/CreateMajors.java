package PennCourseRecommender;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CreateMajors {
	
	private Set<Major> allMajors;
	
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();

		try {
			JSONObject majorsAndGroups =
					(JSONObject) parser.parse(new FileReader("majors.json"));
			
			JSONObject majors = (JSONObject) majorsAndGroups.get("majors");
			JSONObject groups = (JSONObject) majorsAndGroups.get("groups");
			
			//iterate over each major
			for (String majorName : (Set<String>) majors.keySet()) {
				Major m = new Major(majorName);
				JSONObject reqs = (JSONObject) majors.get(majorName);
				//iterate over each requirement
				for (String key : (Set<String>) reqs.keySet()) {
					if (key.equals("URL")) m.setURL((String) reqs.get("URL"));
					else if (key.equals("Notes")) {
						m.setNotes((Set<String>) ((JSONObject) reqs.get("Notes")).keySet());
					}
				}
				ReqTree tree = new ReqTree(reqs);
			}
		} catch (Exception e) {
			System.out.println("error");
		}
	}
}
