package PennCourseRecommender;

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
	}
	
	public ReqTree(Object o, ReqTree parent, Integer level) {
		this.parent = parent;
		this.level = level;
		this.children = new HashSet<ReqTree>();
		if (o instanceof String) {
			this.children = null;
			String s = (String) o;
			if (!s.startsWith("G: ")) {
				this.data = s;
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
	
	public boolean isSatisfied(Set<String> coursesTaken) {
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
	}
	
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
	
	public void addChild(ReqTree child) {
		this.children.add(child);
	}
	
	public String toString() {
		String s = this.data + "\n";
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
	
	private Set<String> getAllCoursesInTree() {
		Set<String> courses = new HashSet<String>();
		if (this.children == null) {
			courses.add(this.data);
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
			contributionScores.put(this.data, 1.0);
		} else {
			for (ReqTree child : this.children) {
				Map<String, Double> childContributionScores = child.getContributionScores(descendants);
				for (String x : childContributionScores.keySet()) {
					Integer numOf = 0;
					if (this.data.equals("OR")) numOf = 1;
					else if (this.data.equals("AND")) numOf = child.children.size();
					else if (this.data.startsWith("OF")) numOf = Integer.valueOf(this.data.charAt(2));
					else System.out.println("How'd we get here?");
					contributionScores.put(x, contributionScores.get(x) + 
							numOf * childContributionScores.get(x) / child.children.size());
				}
			}
		}
		return contributionScores;
	}
	
	public Map<String, Double> getDescendantScores() {
		if (this.parent != null) return null; //only call this on the root node
		Set<String> allCoursesInTree = this.getAllCoursesInTree();
		Map<String, Double> finalDescendantScores = new HashMap<String, Double>();
		for (String course : allCoursesInTree) { //iterate over each course in the entire tree
			Set<String> descendants = null;
			//Set<String> descendants = something.getDescendants();
			Map<String, Double> contributionScores = new HashMap<String, Double>();
			for (String descendant : descendants) {
				if (allCoursesInTree.contains(descendant))
					contributionScores.put(descendant, 0.0);
			}
			for (ReqTree child : this.children) { //look on each branch of the tree
				Map<String, Double> contributionFromThisChild = child.getContributionScores(descendants);
				for (String x : contributionFromThisChild.keySet()) {
					contributionScores.put(x, contributionScores.get(x) + contributionFromThisChild.get(x));
				}
			}
			Double finalDescendantScore = 0.0;
			for (String descendant : contributionScores.keySet()) {
				finalDescendantScore += contributionScores.get(descendant);
			}
			finalDescendantScores.put(course, finalDescendantScore);
		}
		return finalDescendantScores;
	}
}