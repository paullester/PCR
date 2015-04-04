package PennCourseRecommender;

import java.util.Map;
import java.util.Set;

public class Major {

	private String name;
	private Set<Set<Course>> requirements;
	private Map<Course, Integer> numDescendants;
	private String url;
	private Set<String> notes;
}