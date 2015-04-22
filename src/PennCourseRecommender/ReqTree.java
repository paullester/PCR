package PennCourseRecommender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ReqTree {
	private String data;
	private ReqTree parent;
	private Set<ReqTree> children;
	private Integer level;
	private JSONObject groups;
	private Map<String, Double> descendantScores;
	private Map<String, Set<String>> descendants;
	
	//Constructors
	public ReqTree(JSONObject reqs, JSONObject groups) {
		this.data = "AND";
		this.level = 0;
		this.groups = groups;
		this.children = new HashSet<ReqTree>();
		for (String key : (Set<String>) reqs.keySet()) {
			if (!key.equals("URL") && !key.equals("Notes")) {
				if (reqs.get(key) instanceof JSONArray) {
					JSONArray x = (JSONArray) reqs.get(key);
					this.children.add(new ReqTree(x, this, this.level + 1));
				} else {
					this.children.add(new ReqTree(reqs.get(key), this, this.level + 1));
				}
			}
		}
		try {
			this.setDescendants();
			if (this.descendants == null) System.out.println("Descendants are null");
			//else System.out.println("Descendants: " + this.descendants);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDescendantScores();
	}
	
	public ReqTree(Object o, ReqTree parent, Integer level) {
		this.parent = parent;
		this.level = level;
		this.descendants = null;
		this.descendantScores = null;
		this.children = new HashSet<ReqTree>();
		if (o instanceof String) {
			this.children = null;
			String s = (String) o;
			if (!s.startsWith("G: ")) {
				this.data = s.replaceAll(" ","");
			} else {
				JSONArray groupCourses = this.getGroup(s.substring(3));
				for (int i = 0; groupCourses != null && i < groupCourses.size(); i++) {
					this.parent.addChild(new ReqTree(groupCourses.get(i), this.parent, this.level));
				}
			}
		} else if (o instanceof JSONArray) {
			JSONArray subReq = (JSONArray) o;
			if (subReq.size() > 0) {
				this.data = (String) subReq.get(0);
				if (this.data.equals("OF")) {
					this.data = this.data + String.valueOf(subReq.get(1));
					for (int i = 2; i < subReq.size(); i++) {
						this.children.add(new ReqTree(subReq.get(i), this, this.level + 1));
					}
				} else if (this.data.equals("OR") || this.data.equals("AND")) {
					for (int i = 1; i < subReq.size(); i++) {
						this.children.add(new ReqTree(subReq.get(i), this, this.level + 1));
					}
				}
			} else {
				//Figure out what to do about Free Electives
			}
		} else {
			System.out.println("What did you pass in?");
		}
	}
	
	public void addChild(ReqTree child) {
		this.children.add(child);
	}
	
	//Setter methods
	private void setDescendants() throws IOException {
		this.descendants = new HashMap<String, Set<String>>();
		BufferedReader br = new BufferedReader(new FileReader("/Users/BenGitles/Documents/School/Senior Design/PCR/src/descendants.txt"));
		String line = br.readLine();
		while (line != null) {
			String[] parts = line.split(": ");
			if (this.getAllCoursesInTree().contains(parts[0]) && parts.length > 1) {
				Set<String> d = new HashSet<String>();
				for (String s : parts[1].split(" ")) d.add(s);
				this.descendants.put(parts[0], d);
			}
			line = br.readLine();
		}
	}
	
	private void setDescendantScores() {
		//if (this.parent != null) return null;
		//only call this on the root node
		Set<String> allCoursesInTree = this.getAllCoursesInTree();
		this.descendantScores = new HashMap<String, Double>();
		for (String course : allCoursesInTree) { //iterate over entire tree for each course
			Map<String, Double> contributionScores = new HashMap<String, Double>();
			//System.out.print("Descendants of " + course + ": ");
			//System.out.println();
			if (this.descendants.containsKey(course)) {
				for (String descendant : this.descendants.get(course)) {
					if (allCoursesInTree.contains(descendant))
						contributionScores.put(descendant, 0.0);
				}
				for (ReqTree child : this.children) { //look on each branch of the tree
					Map<String, Double> contributionFromThisChild = child.getContributionScores(descendants.get(course));
					for (String x : contributionFromThisChild.keySet()) {
						if (contributionScores.containsKey(x)) {
							contributionScores.put(x, contributionScores.get(x)
									+ contributionFromThisChild.get(x));
						}
					}
				}
			}
			Double finalDescendantScore = 0.0;
			for (String descendant : contributionScores.keySet()) {
				finalDescendantScore += contributionScores.get(descendant);
			}
			this.descendantScores.put(course, finalDescendantScore);
			//System.out.println("Descendant Score for " + course + ": " + finalDescendantScore);
		}
		Double maxScore = 0.0;
		for (String course : this.descendantScores.keySet()) {
			if (maxScore < this.descendantScores.get(course)) {
				maxScore = this.descendantScores.get(course);
			}
		}
		
		for (String course : this.descendantScores.keySet()) {
			this.descendantScores.put(course, this.descendantScores.get(course) / maxScore + 1);
		}
	}
	
	/*public boolean isSatisfied(Set<String> coursesTaken) {
		if (coursesTaken.contains(this.data)) return true;
		else if (this.data.equals("OR")) {
			for (ReqTree child : this.children) {
				if (child.isSatisfied(coursesTaken)) return true;
			}
		} else if (this.data.equals("AND")) {
			for (ReqTree child : this.children) {
				if (!child.isSatisfied(coursesTaken)) return false;
			}
		} else if (this.data.startsWith("OF")) {
			Integer numNeeded = Character.getNumericValue(this.data.charAt(2));
			Integer numSatisfied = 0;
			for (ReqTree child : this.children) {
				if (child.isSatisfied(coursesTaken)) {
					if (++numSatisfied == numNeeded) return true;
				}
			}
		} else {
			System.out.println("What is this node? " + this.data);
		}
		return false;
	}*/
	
	//Getter methods
	public String getData() {
		return this.data;
	}
	
	public ReqTree getParent() {
		return this.parent;
	}
	
	public JSONArray getGroup(String group) {
		if (this.groups == null) return this.parent.getGroup(group);
		return (JSONArray) this.groups.get(group);
	}
	
	public Map<String, Double> getDescendantScores() {
		return this.descendantScores;
	}
	
	/*public void printTopDescendantScores() {
		String course1 = "", course2 = "", course3 = "", course4 = "", course5 = "", course6 = "", course7 = "", course8 = "", course9 = "", course10 = "";
		Double score1 = 0.0, score2 = 0.0, score3 = 0.0, score4 = 0.0, score5 = 0.0, score6 = 0.0, score7 = 0.0, score8 = 0.0, score9 = 0.0, score10 = 0.0;
		for (String course : this.descendantScores.keySet()) {
			if (this.descendantScores.get(course) >= score1) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = score6;
				course7 = course6;
				score6 = score5;
				course6 = course5;
				score5 = score4;
				course5 = course4;
				score4 = score3;
				course4 = course3;
				score3 = score2;
				course3 = course2;
				score2 = score1;
				course2 = course1;
				score1 = this.descendantScores.get(course);
				course1 = course;
			} else if (this.descendantScores.get(course) >= score2) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = score6;
				course7 = course6;
				score6 = score5;
				course6 = course5;
				score5 = score4;
				course5 = course4;
				score4 = score3;
				course4 = course3;
				score3 = score2;
				course3 = course2;
				score2 = this.descendantScores.get(course);
				course2 = course;
			} else if (this.descendantScores.get(course) >= score3) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = score6;
				course7 = course6;
				score6 = score5;
				course6 = course5;
				score5 = score4;
				course5 = course4;
				score4 = score3;
				course4 = course3;
				score3 = this.descendantScores.get(course);
				course3 = course;
			} else if (this.descendantScores.get(course) >= score4) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = score6;
				course7 = course6;
				score6 = score5;
				course6 = course5;
				score5 = score4;
				course5 = course4;
				score4 = this.descendantScores.get(course);
				course4 = course;
			} else if (this.descendantScores.get(course) >= score5) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = score6;
				course7 = course6;
				score6 = score5;
				course6 = course5;
				score5 = this.descendantScores.get(course);
				course5 = course;
			} else if (this.descendantScores.get(course) >= score6) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = score6;
				course7 = course6;
				score6 = this.descendantScores.get(course);
				course6 = course;
			} else if (this.descendantScores.get(course) >= score7) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = this.descendantScores.get(course);
				course7 = course;
			} else if (this.descendantScores.get(course) >= score8) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = this.descendantScores.get(course);
				course8 = course;
			} else if (this.descendantScores.get(course) >= score9) {
				score10 = score9;
				course10 = course9;
				score9 = this.descendantScores.get(course);
				course9 = course;
			} else if (this.descendantScores.get(course) >= score10) {
				score10 = this.descendantScores.get(course);
				course10 = course;
			}
		}
		System.out.println("Course 1: " + course1 + ", Score: " + score1);
		System.out.println("Course 2: " + course2 + ", Score: " + score2);
		System.out.println("Course 3: " + course3 + ", Score: " + score3);
		System.out.println("Course 4: " + course4 + ", Score: " + score4);
		System.out.println("Course 5: " + course5 + ", Score: " + score5);
		System.out.println("Course 6: " + course6 + ", Score: " + score6);
		System.out.println("Course 7: " + course7 + ", Score: " + score7);
		System.out.println("Course 8: " + course8 + ", Score: " + score8);
		System.out.println("Course 9: " + course9 + ", Score: " + score9);
		System.out.println("Course 10: " + course10 + ", Score: " + score10);
		System.out.println("ASTR392 Score: " + this.descendantScores.get("ASTR392"));
		System.out.println("WRIT032 Score: " + this.descendantScores.get("ASTR392"));
	}*/
	
	private Set<String> getAllCoursesInTree() {
		Set<String> courses = new HashSet<String>();
		if (this.children == null) {
			if (this.data != null) courses.add(this.data);
			return courses;
		}
		for (ReqTree rt : this.children) {
			courses.addAll(rt.getAllCoursesInTree());
		}
		return courses;
	}
	
	private Map<String, Double> getContributionScores(Set<String> descendants) {
		Map<String, Double> contributionScores = new HashMap<String, Double>();
		for (String descendant : descendants) contributionScores.put(descendant, 0.0);
		if (this.children == null) {
			if (descendants.contains(this.data)) contributionScores.put(this.data, 1.0);
		} else {
			for (ReqTree child : this.children) {
				Map<String, Double> childContributionScores = child.getContributionScores(descendants);
				for (String x : childContributionScores.keySet()) {
					Integer numOf = 0;
					if (this.data.equals("OR")) numOf = 1;
					else if (this.data.equals("AND")) numOf = this.children.size();
					else if (this.data.startsWith("OF")) numOf = Integer.valueOf(this.data.charAt(2));
					else System.out.println("How'd we get here?");
					
					contributionScores.put(x, contributionScores.get(x) + 
							numOf * childContributionScores.get(x) / this.children.size());
				}
			}
		}
		return contributionScores;
	}
	
	public String toString() {
		String s = "";
		if (this.data != null) s = this.data + "\n";
		for (int i=0; i < this.level; i++) {
			s = "  " + s;
		}
		if (this.children != null) {
			for (ReqTree rt : this.children) {
				s = s + rt.toString();
			}
		}
		return s;
	}
}