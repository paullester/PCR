package PennCourseRecommender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ReqTree {
	private String data;
	private ReqTree parent;
	private Set<ReqTree> children;
	
	public ReqTree(JSONArray subReq, ReqTree parent) {
		this.parent = parent;
		this.data = (String) subReq.get(0);
		if (data.equals("OF")) {
			data.concat((String) subReq.get(1));
		}
		//Now I need to figure out how these subreqs can refer to groupss
	}
	
	public ReqTree(JSONObject reqs) {
		this.data = "AND";
		for (String key : (Set<String>) reqs.keySet()) {
			if (!key.equals("URL") && !key.equals("Notes")) {
				if (reqs.get(key) instanceof JSONArray) {
					JSONArray x = (JSONArray) reqs.get(key);
					for (int i=1; i < x.size(); i++) {
						children.add(new ReqTree(x, this));
					}
				} else {
					ReqTree node = new ReqTree((String) reqs.get(key), this);
					this.children.add(node);
				}
			}
		}
	}
	
	public ReqTree(String s, ReqTree rt) {
		this.data = s;
		this.parent = rt;
		this.children = null;
	}
	
	public void setParent(ReqTree parent) {
		this.parent = parent;
	}
	
	public boolean isSatisfied() {
		return false;
	}
}