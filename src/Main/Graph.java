package Main;

import java.util.ArrayList;

abstract class Graph {
	abstract void addKnote(int id,int x,int y);
	abstract void removeKnote(int id);
	abstract GKnote getKnote(int d);
	abstract ArrayList<GKnote> getKnoten();
	
	abstract void addKante(int id,int fromID,int toID);
	abstract void removeKante(int id);
	abstract ArrayList<GKante> getKanten();
	
	abstract Boolean adjacent(GKnote a, GKnote b);
	abstract ArrayList<GKnote> nachbarn(GKnote id);
	
}
