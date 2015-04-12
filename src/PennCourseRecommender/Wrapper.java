package PennCourseRecommender;

public class Wrapper {
	
	//private static ArrayList<Course> courses = new ArrayList<Course>();
	
	public static void main(String[] args) {

        AllCourses a = new AllCourses();

        //System.out.println("Hello World");
        a.openJsonFile();

        a.initCourses();

        a.closeJsonFile();
	}

    public static int apiCall(int arg) {
        return arg;
    }
}
