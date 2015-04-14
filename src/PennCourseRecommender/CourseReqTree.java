package PennCourseRecommender;

import java.util.HashSet;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CourseReqTree {
	
	String nodeVal;//Can be AND, OR, or the value of the leaf node
	HashSet<CourseReqTree> children;
	
	
	public CourseReqTree(){
		children = new HashSet<CourseReqTree>();
	}


	
	
	public void printReqTree(){
		
		
		if(children == null){
			System.out.println(nodeVal);
		} else {
			System.out.println(nodeVal);
			Iterator<CourseReqTree> i = children.iterator();
			while(i.hasNext()){
				CourseReqTree current = i.next();
				current.printReqTree();
			}
		}
	}
	
	public boolean satisfyReq(HashSet<String> studentCourses){
		
		if(children!=null){
			Iterator<CourseReqTree> i = children.iterator();
			if(this.nodeVal=="AND" ){
				if(i.hasNext()){
					CourseReqTree a = i.next();
					if(i.hasNext()){
						CourseReqTree b = i.next();
						//System.out.println("evaluating AND node");
						return (a.satisfyReq(studentCourses) && b.satisfyReq(studentCourses));
					}
				}
			} else if (this.nodeVal=="OR"){
				if(i.hasNext()){
					CourseReqTree a = i.next();
					if(i.hasNext()){
						CourseReqTree b = i.next();
						//System.out.println("evaluating OR node");
						return (a.satisfyReq(studentCourses) || b.satisfyReq(studentCourses));
					}
				}
			}
		} else {
			if(studentCourses.contains(nodeVal)){
				//System.out.println("student has taken course "+nodeVal);
				return true;
			} else{
				//System.out.println("student does not have course "+nodeVal);
				return false;
			}
			
		}
		
		
		
		return true;
		
		
	}
	
	public CourseReqTree generateReqTree(JSONArray prereqs){
		
		if(prereqs!=null){
			Iterator<String> i = prereqs.iterator();
			
			while(i.hasNext()){
				CourseReqTree curChild;
				Object current = i.next();
				//if it is a list of prereqs
				if(current.getClass() == JSONArray.class){
					curChild = new CourseReqTree();
					children.add(curChild);
					curChild.generateReqTree((JSONArray)current);
				} else if (current.toString().equals("&&")){
					
					//makes the current node an "AND" logical op
					
					this.nodeVal = "AND";
					
				} else if (current.toString().equals("||")){
					this.nodeVal = "OR";
				} else if (current!=null){
					//make a new coursereqtree, add to children and instantiate leaf. 
					curChild = new CourseReqTree();
					children.add(curChild);
					curChild.generateReqLeaf(current.toString());					
				}
				
			}
		}
		
		//quick fix if there is only one prereq
		if(children.size()==1){
			nodeVal = children.iterator().next().toString();
			children =null;
		}
		
		return this;
		
		}
	
	public CourseReqTree generateReqLeaf(String s){
		this.nodeVal = s;
		this.children = null;
		return this;
	}
	
	
}


