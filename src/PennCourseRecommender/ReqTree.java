package PennCourseRecommender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ReqTree {
	private String data;
	private String description;
	private ReqTree parent;
	private Set<ReqTree> children;
	private Integer level;
	private JSONObject groups;
	private Map<String, Double> descendantScores;
	private Map<String, Set<String>> descendants;
	
	//Constructors
	public ReqTree(JSONObject reqs, JSONObject groups, Set<String> coursesTaken) {
		this.data = "AND";
		this.description = "root";
		this.level = 0;
		this.groups = groups;
		this.children = new HashSet<ReqTree>();
		for (String key : (Set<String>) reqs.keySet()) {
			if (!key.equals("URL") && !key.equals("Notes"))
				this.children.add(new ReqTree(key, reqs.get(key), this, this.level + 1));
		}
		try {
			this.setDescendants();
			if (this.descendants == null) System.out.println("Descendants are null");
			//else System.out.println("Descendants: " + this.descendants);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Setting descendant scores");
		this.setDescendantScores(coursesTaken);
		System.out.println("Setting descendant scores done");
	}
	
	public ReqTree(String description, Object o, ReqTree parent, Integer level) {
		this.parent = parent;
		this.level = level;
		this.descendants = this.parent.descendants;
		this.descendantScores = this.parent.descendantScores;
		this.groups = this.parent.groups;
		this.description = description;
		this.children = new HashSet<ReqTree>();
		if (o instanceof String) {
			this.children = null;
			String s = (String) o;
			if (!s.startsWith("G: ")) {
				this.data = s.replaceAll(" ","");
				this.description = this.data;
			} else {
				JSONArray groupCourses = this.getGroup(s.substring(3));
				for (int i = 0; groupCourses != null && i < groupCourses.size(); i++) {
					this.parent.addChild(new ReqTree("", groupCourses.get(i), this.parent, this.level));
					this.parent.setDescription(s.substring(3));
				}
				this.parent.children.remove(this);
			}
		} else if (o instanceof JSONArray) {
			JSONArray subReq = (JSONArray) o;
			if (subReq.size() > 0) {
				this.data = (String) subReq.get(0);
				for (int i = 1; i < subReq.size(); i++) {
					if (this.data.equals("OF")) {
						this.data = "OF" + subReq.get(1);
						i++;
					}
					this.children.add(new ReqTree("", subReq.get(i), this, this.level + 1));
				}
			} else {
				//Do nothing for Free Electives
			}
		}
	}
	
	public void addChild(ReqTree child) {
		this.children.add(child);
	}
	
	//Setter methods
	private void setDescendants() throws IOException {
		this.descendants = new HashMap<String, Set<String>>();
		Set<String> allCoursesInTree = this.getAllCoursesInTree();
		BufferedReader br = new BufferedReader(new FileReader("/Users/BenGitles/Documents/School/Senior Design/PCR/src/descendants.txt"));
		String line = br.readLine();
		while (line != null) {
			String[] parts = line.split(": ");
			if (allCoursesInTree.contains(parts[0]) && parts.length > 1) {
				Set<String> d = new HashSet<String>();
				for (String s : parts[1].split(" ")) d.add(s);
				this.descendants.put(parts[0], d);
			}
			line = br.readLine();
		}
	}
	
	private void setDescendantScores(Set<String> coursesTaken) {
		//if (this.parent != null) return null;
		//only call this on the root node
		Set<String> allCoursesInTree = this.getAllCoursesInTree();
		this.descendantScores = new HashMap<String, Double>();
		Double maxScore = 0.0;
		for (String course : this.descendants.keySet()) { //iterate over entire tree for each course that has descendants
			if (allCoursesInTree.contains(course)) { //if this course is in the major
				Double finalDescendantScore = 0.0;
				for (ReqTree child : this.children) { //look on each branch of the root node
					if (!child.isSatisfied(coursesTaken)) //if that course or group is not satisfied
						finalDescendantScore += child.getContributionScore(this.descendants.get(course), coursesTaken);
				}
				this.descendantScores.put(course, finalDescendantScore);
				if (finalDescendantScore > maxScore) maxScore = finalDescendantScore;
			}
		}
		for (String course : this.descendantScores.keySet()) {
			if (maxScore > 0.0) this.descendantScores.put(course, this.descendantScores.get(course) / maxScore + 1);
			else this.descendantScores.put(course, 1.0);
		}
	}
	
	public void setDescription(String s) {
		if (this.description == null || this.description == "") {
			this.description = s;
		} else if (!this.description.contains(s)) {
			this.description.concat(", " + s);
		}
	}
	
	public boolean isSatisfied(Set<String> coursesTaken) {
		if (this.data != null) {
			if (this.data.equals("OR")) {
				for (ReqTree child : this.children) {
					if (child.isSatisfied(coursesTaken)) return true;
				}
			} else if (this.data.equals("AND")) {
				for (ReqTree child : this.children) {
					if (!child.isSatisfied(coursesTaken)) return false;
				}
				return true;
			} else if (this.data.startsWith("OF")) {
				Integer numNeeded = Character.getNumericValue(this.data.charAt(2));
				Integer numSatisfied = 0;
				for (ReqTree child : this.children) {
					if (child.isSatisfied(coursesTaken)) {
						if (++numSatisfied == numNeeded) return true;
					}
				}
			} else if (coursesTaken.contains(this.data)) {
				return true;
			}
		}
		return false;
	}
	
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
	
	private Double getContributionScore(Set<String> descendants, Set<String> coursesTaken) {
		Double contributionScore = 0.0;
		if (this.children == null) { //if this node is a course...
			if (descendants.contains(this.data)) return 1.0; //...and is a descendant, return 1.0
		} else if (this.data != null){ //This node must be a group
			Integer numOf = 0; //combining children's contribution scores to the descendant score
			if (this.data.equals("OR")) numOf = 1; //only one needed from an "OR" group
			else if (this.data.equals("AND")) numOf = this.children.size(); //all are needed in an "AND" group
			else if (this.data.startsWith("OF")) {
				Integer numSatisfied = 0;
				for (ReqTree child : this.children) { if (child.isSatisfied(coursesTaken)) numSatisfied++; }
				numOf = Integer.valueOf(this.data.charAt(2)) - numSatisfied;
				//^^^weight by the number of courses needed to take from the group, minus the number already taken
			}
			for (ReqTree child : this.children) { //scan over each requirement in the group
				if (!child.isSatisfied(coursesTaken)) //if the student has not satisfied this requirement
					contributionScore += numOf * child.getContributionScore(descendants, coursesTaken) / this.children.size();
			}
		}
		return contributionScore;
	}
	
	public Map<String, Set<String>> getDescendants() {
		if (this.descendants == null) return this.parent.getDescendants();
		return this.descendants;
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