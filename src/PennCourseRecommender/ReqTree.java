package PennCourseRecommender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
				//this.children.add(new ReqTree(reqs.get(key), this, this.level + 1));
				if (reqs.get(key) instanceof JSONArray) {
					JSONArray x = (JSONArray) reqs.get(key);
					//for (int i=1; i < x.size(); i++) {
						this.children.add(new ReqTree(x, this, this.level + 1));
					//}
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
			this.data = s;
			/*if (!s.startsWith("G: ")) {
				this.data = s;
			} else {
				JSONArray groupCourses =
						(JSONArray) this.parent.getGroups().get(s.substring(3));
				for (int i = 0; i < groupCourses.size(); i++) {
					ReqTree grandparent = null;
					if (this.parent.getParent() != null) grandparent = this.parent.getParent();
					this.parent.addChild(new ReqTree(groupCourses.get(i), grandparent, this.level));
				}
			}*/
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
	
	public JSONObject getGroups() {
		if (this.groups == null) return this.parent.getGroups();
		return this.groups;
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
}