package multiattribute;

//ADSORPTION MODULE

public interface PeerScore {
	
	//returns adsorption score for a certain course based off of the course and the student's peers. (How the peer list/map is initializes is TODO)
	public double getPeerScore(Course c, Student s);
	
	
	
}
