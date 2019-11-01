package Main;

class GKnote {
	
	int id,x,y;
	private double distance;
	private GKnote vorganger;
	public GKnote(int id,int x,int y) {
		this.id=id;
		this.x=x;
		this.y=y;
	}
	GKnote getVorganger() {
		return this.vorganger;
	}
	void setVorganger(GKnote vorganger) {
		this.vorganger=vorganger;
	}
	double getDistance() {
		return this.distance;
	}
	void setDistance(double distance) {
		this.distance=distance;
	}
	public String toString() {
		return Integer.toString(id);
	}
	
	
	
}
