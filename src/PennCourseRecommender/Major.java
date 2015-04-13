package PennCourseRecommender;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;



public class Major {

	private String name;
	private ReqTree tree;
	private Map<Course, Double> descendantScores;
	private String url;
	private Set<String> notes;
	
	public Major(String name) {
		this.name = name;
	}
	
	public void setRequirements(ReqTree requirements) {
		this.tree = requirements;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public void setNotes(Set<String> notes) {
		this.notes = notes;
	}
	
	public void setDescendantScores(Map<Course, Double> descendantScores) {
		this.descendantScores = descendantScores;
	}
	
	public void setDescendantScoresWithStrings(Map<String, Double> descendantScores) {
		this.descendantScores = null;
	}
}