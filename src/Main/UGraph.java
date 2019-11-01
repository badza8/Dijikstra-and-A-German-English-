package Main;
import java.util.*;
public class UGraph extends Graph{
	
	int canvasSizeX=0,canvasSizeY;
	List<GKnote> KnotenList=new ArrayList<GKnote>();
	List<GKante> KantenList=new ArrayList<GKante>();
	int [][] adList=null;

	  
	
	@Override
	void addKnote(int id, int x, int y) {
		
		for(GKnote i:KnotenList) if(i.id==id) return;
		KnotenList.add(new GKnote(id,x,y));
		int nodeNumber = KnotenList.size();
	    int [][] newAdjacencyList= new int [nodeNumber][nodeNumber];
	    
	    if( adList==null ) {
	    	
	    	newAdjacencyList[nodeNumber-1][nodeNumber-1] =-1; //neue Knote hat -1 als leere Kante
		    adList=newAdjacencyList;
		    
	    }
	    else {
	    	
	    	for (int i=0; i< nodeNumber-1; i++) 
	    		for(int j=0;j<nodeNumber-1;++j)
	    				newAdjacencyList [i][j]=adList[i][j];
	    	
	    	for(int i=0;i<nodeNumber;++i) 
	    		newAdjacencyList[i][nodeNumber-1] =-1;
	    	
	    	for(int i=0;i<nodeNumber;++i) 
	    		newAdjacencyList[nodeNumber-1][i] =-1; //neue Knote hat -1 als leere Kante
	    	
	    	adList=newAdjacencyList;
	    
	    }
		
	}
	
	
	@Override
	void removeKnote(int id) {
		
		int indKnote=-2;
		for(int i=0;i<KnotenList.size();++i) {
			if(KnotenList.get(i).id==id) indKnote=i;
		}
		if(indKnote<0) return;
		
		GKnote tmp=KnotenList.get(indKnote);
		
		
		for(int i=0;i<KantenList.size();++i) {//entfernen alle Kanten der Knote
			
			if(KantenList.get(i).fromID==tmp.id || KantenList.get(i).toID==tmp.id) {
				removeKante(KantenList.get(i).id);
			}
			
		}
		
		//entferne die Knote aus KnotenListe
		int nodeNumber =adList.length ;
		
	    int [][] newAdjacencyList= new int [nodeNumber-1][nodeNumber-1];
	    int d=0,e=0;
	    Boolean bd=false,be=false;
	    
	    for (int i=0; i< nodeNumber; i++) {
	    	
	    	if(i==indKnote) {bd=true;++i;} 
	    	if(bd==false)d=i;
	    	else d=i-1;
	    	
	    	for(int j=0;j<nodeNumber;++j) {
    			if(j==indKnote) {be=true;++j;}
    			if(be==false)e=j;
    	    	else e=j-1;
    			newAdjacencyList [d][e] =adList[i][j] ;
    		}
	    	be=false;
	    }
	    adList=newAdjacencyList;
	    
	    KnotenList.remove(tmp);
		
	}
	
	
	@Override
	GKnote getKnote(int d) {
		
		GKnote tmp=null;
		
		for(GKnote i:KnotenList)
			if(i.id==d) { 
				tmp=i;
				break;
			}
		return tmp;
		
	}
	
	
	@Override
	ArrayList<GKnote> getKnoten() {
		
		return (ArrayList<GKnote>) this.KnotenList;
		
	}

	
	@Override
	void addKante(int id, int fromID, int toID) {
		for(GKante i:KantenList) if(i.id==id) return;
		KantenList.add(new GKante(id,fromID,toID));
		
		GKnote tmp1=this.getKnote(fromID);
		GKnote tmp2=this.getKnote(toID);
		
		
		
		int fromID1=KnotenList.indexOf(tmp1);
		int toID1=KnotenList.indexOf(tmp2);
		
		int nodeNumber = adList.length;
		
		  if (fromID1 > nodeNumber || toID1 > nodeNumber || fromID1 < 0 || toID1 < 0 )
		     return;
		  
		  
		 adList[fromID1][toID1]=1;
		 adList[toID1][fromID1]=1;
		
	}
	
	
	void removeKante(int id) {
		
		int indKant=-2;
		for(int i=0;i<KantenList.size();++i) {
			if(KantenList.get(i).id==id) indKant=i;
		}
		
		GKante tmp=KantenList.get(indKant);
		
		int from=-2;
		for(int i=0;i<KnotenList.size();++i) {
			if(KnotenList.get(i).id==tmp.fromID) from=i;
		}
		
		int to=-2;
		for(int i=0;i<KnotenList.size();++i) {
			if(KnotenList.get(i).id==tmp.toID) to=i;
		}
		
		if(to<0 || from<0 ) System.out.println("false");
		
		
		adList[from][to]=-1;
		adList[to][from]=-1;
		
		KantenList.remove(tmp);
		
	}
	
	
	@Override
	ArrayList<GKante> getKanten() {
		
		return (ArrayList<GKante>) this.KantenList;
		
	}
	
	
	@Override
	Boolean adjacent(GKnote a, GKnote b) {
		int tmpInda = KnotenList.indexOf(a);
		int tmpIndb = KnotenList.indexOf(b);
		for(int i:adList[tmpInda])
			if(i==tmpIndb) return true;
		
		return false;
	}
	
	
	@Override
	ArrayList<GKnote> nachbarn(GKnote a) {
		ArrayList<GKnote> tmp=new ArrayList<GKnote>();
		
		int tmpInda=-2;
		int knotenListSize=KnotenList.size();
		for(int i=0;i<knotenListSize;++i) 
			if(KnotenList.get(i).id==a.id) {tmpInda=i;break;}
		
		
		
		if(adList == null || tmpInda<0 || tmpInda>KnotenList.size()) return null;
		
		for(int i=0;i<knotenListSize;++i) {
			int ragac=adList[tmpInda][i];
			if(ragac>-1)
				tmp.add(KnotenList.get(i));
		}
		
		return tmp;
	}
	
	
	

}
