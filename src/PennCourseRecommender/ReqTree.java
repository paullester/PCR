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