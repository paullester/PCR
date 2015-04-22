package PennCourseRecommender;

import java.math.BigDecimal;
import java.util.Map;

public class PrintTopScores {

	public PrintTopScores(Map<String, Double> map) {		
		String course1 = "", course2 = "", course3 = "", course4 = "", course5 = "", course6 = "", course7 = "", course8 = "", course9 = "", course10 = "";
		Double score1 = 0.0, score2 = 0.0, score3 = 0.0, score4 = 0.0, score5 = 0.0, score6 = 0.0, score7 = 0.0, score8 = 0.0, score9 = 0.0, score10 = 0.0;
		for (String course : map.keySet()) {
			if (map.get(course) >= score1) {
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
				score1 = map.get(course);
				course1 = course;
			} else if (map.get(course) >= score2) {
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
				score2 = map.get(course);
				course2 = course;
			} else if (map.get(course) >= score3) {
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
				score3 = map.get(course);
				course3 = course;
			} else if (map.get(course) >= score4) {
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
				score4 = map.get(course);
				course4 = course;
			} else if (map.get(course) >= score5) {
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
				score5 = map.get(course);
				course5 = course;
			} else if (map.get(course) >= score6) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = score6;
				course7 = course6;
				score6 = map.get(course);
				course6 = course;
			} else if (map.get(course) >= score7) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = score7;
				course8 = course7;
				score7 = map.get(course);
				course7 = course;
			} else if (map.get(course) >= score8) {
				score10 = score9;
				course10 = course9;
				score9 = score8;
				course9 = course8;
				score8 = map.get(course);
				course8 = course;
			} else if (map.get(course) >= score9) {
				score10 = score9;
				course10 = course9;
				score9 = map.get(course);
				course9 = course;
			} else if (map.get(course) >= score10) {
				score10 = map.get(course);
				course10 = course;
			}
		}
		System.out.println("Course 1: " + course1 + ", Score: " + truncate(score1));
		System.out.println("Course 2: " + course2 + ", Score: " + truncate(score2));
		System.out.println("Course 3: " + course3 + ", Score: " + truncate(score3));
		System.out.println("Course 4: " + course4 + ", Score: " + truncate(score4));
		System.out.println("Course 5: " + course5 + ", Score: " + truncate(score5));
		System.out.println("Course 6: " + course6 + ", Score: " + truncate(score6));
		System.out.println("Course 7: " + course7 + ", Score: " + truncate(score7));
		System.out.println("Course 8: " + course8 + ", Score: " + truncate(score8));
		System.out.println("Course 9: " + course9 + ", Score: " + truncate(score9));
		System.out.println("Course 10: " + course10 + ", Score: " + truncate(score10));
	}
	
	private static BigDecimal truncate(double x) {
	    if ( x > 0) {
	        return new BigDecimal(String.valueOf(x)).setScale(2, BigDecimal.ROUND_FLOOR);
	    } else {
	        return new BigDecimal(String.valueOf(x)).setScale(2, BigDecimal.ROUND_CEILING);
	    }
	}
}
