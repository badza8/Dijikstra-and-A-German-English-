package Main;

import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

class GraphPanel extends JFrame {

	    
		private static final long serialVersionUID = 1L;

	
		UGraph x;
		
		
		public GraphPanel(UGraph x,int canvasSizeX,int canvasSizeY) {
	    	setTitle("Graph");
	    	setSize(canvasSizeX+10,canvasSizeY+10);
	    	setResizable(true);
	    	setVisible(true);
	    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	this.x=x;
	    	
	    	
	    }
	    
		
	    public void paint(Graphics graphic) {
	    	int knots=x.KnotenList.size() , kants=x.KantenList.size();
	    	
	    	graphic.setColor(Color.RED);
	    	
	    	for(int i=0;i<knots;++i) {//malen der Knoten
		    	
		    	graphic.fillOval(x.KnotenList.get(i).x, x.KnotenList.get(i).y, 8, 8);
		    	
	    	}
	    	
	    	graphic.setColor(Color.BLACK);
	    	int fromID,toID;
	    	GKnote from,to;
	    	
	    	for(int i=0;i<kants;++i) {//malen der Kanten
	    		
	    		fromID= x.KantenList.get(i).fromID;
	    		toID = x.KantenList.get(i).toID;
	    		
				from=x.getKnote(fromID);
				to=x.getKnote(toID);
				
				graphic.drawLine(from.x+4, from.y+4, to.x+4, to.y+4);
				
	    	}
	    	
	    	
	    	dijikstra_sssp(graphic,this.x,this.x.KnotenList.get(56),this.x.KnotenList.get(35));
	    	//a_star_sssp(graphic,this.x,this.x.getKnote(30),this.x.getKnote(53));
	    	
	    	graphic.setColor(Color.GREEN);
	    	
	    	
	    	/*
	    	for(int i=0;i<knots;++i) {
	    		GKnote tmp=x.KnotenList.get(i).getVorganger();
	    		
	    		if(tmp!=null)
	    			graphic.drawLine(x.KnotenList.get(i).x+4, x.KnotenList.get(i).y+4, tmp.x+4, tmp.y+4);
		    	
	    	}*/
	    	
	    	
	    }
	    
	    
	    
	    
	    
	    public static void main(String[] args)  {
	    	
	    	UGraph g=new UGraph();
	    	try {
				fillGraph(g);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	        
	    	
	    	
			 
	         new GraphPanel(g,g.canvasSizeX,g.canvasSizeY);
			
			
		}
	    
	    
	    
	    
	    
	    //												Dijikstra
	    
	    void dijikstra_sssp(Graphics g,UGraph graph,GKnote startKnote,GKnote ziel){
	    	
	    	g.setColor(Color.BLUE);
	    	g.fillOval(startKnote.x, startKnote.y, 10, 10);
	    	g.fillOval(ziel.x, ziel.y, 10, 10);
	    	
	    	
	    	//Anderung der Daten des Knotens           Laufzeit: O(|V|)
	    	
			for(GKnote i:graph.KnotenList) {
				i.setDistance(Integer.MAX_VALUE);
				i.setVorganger(null);
			}
			
			startKnote.setDistance(0);
			startKnote.setVorganger(null);
			
			FibonacciHeap<GKnote> Q=new FibonacciHeap<GKnote>();
			
			//Erzeugen des Fib-Heapes					Laufzeit: O(|V|)
			
			for(GKnote i:graph.KnotenList) 
				
				//Addieren des Knoten in Heap 			Laufzeit: O(1)
				
				Q.insert(new FibonacciHeapNode<GKnote>(i), i.getDistance());
			
			
			while (!(Q.isEmpty())) { //                 Laufzeit: O(|V|)
				
				//Entfernung der erste Knote von Heap   Laufzeit: O(|log V|)
				
				FibonacciHeapNode<GKnote> u=Q.removeMin();
				
				GKnote ug=u.getData();
				
				//Alle Nachbarn von min Knote           Laufzeit: O(|V|)
				
				ArrayList<GKnote> arrGKnote=graph.nachbarn(ug);
				
				//Transformieren Alle Nachbarn Knoten der min Knote von GKnote auf FibonacciHeapNode 
				//und machen eine Array                 Laufzeit: O(|V|)
				
				ArrayList<FibonacciHeapNode<GKnote>> arrFKnote=new ArrayList<FibonacciHeapNode<GKnote>>();
				for(GKnote i:arrGKnote) {
					FibonacciHeapNode<GKnote> tmpFibNode=new FibonacciHeapNode<GKnote>(i);
					tmpFibNode.key=i.getDistance();
					arrFKnote.add(tmpFibNode);
				}
					
				//Dijikstra Algorithums fur FibHeap		Laufzeit: O(|E|)
				
				for(FibonacciHeapNode<GKnote> v:arrFKnote) {
					if(v.getData().id==ziel.id) return;
					if(v.getData().getDistance() > ug.getDistance()+1) {
						Q.decreaseKey(Q.getNode(v.getData().id),ug.getDistance()+1 );
						v.getData().setDistance(ug.getDistance()+1);
						if(v.getData().getVorganger()!=null && v.getData().getVorganger()!=ug ) {
							g.setColor(Color.RED);
							g.drawLine(v.getData().x+4, v.getData().y+4, v.getData().getVorganger().x+4, v.getData().getVorganger().y+4);
						}
						g.setColor(Color.GREEN);
						v.getData().setVorganger(ug);
						g.drawLine(v.getData().x+4, v.getData().y+4,ug.x+4,ug.y+4);
					}
				}
				try {
							Thread.sleep(70);
				} catch (InterruptedException e) {
							e.printStackTrace();
				}
						
					
			}
				
			
	    	
	    }//End Dijikstra
	    
	    
	    
	    
	    
	    //												A*
	    
	    void a_star_sssp(Graphics graphic,UGraph graph,GKnote startKnote,GKnote zielKnote) {
	    	
	    	graphic.setColor(Color.BLUE);
	    	graphic.fillOval(startKnote.x, startKnote.y, 10, 10);
	    	graphic.fillOval(zielKnote.x, zielKnote.y, 10, 10);
	    	
	    	
	    	FibonacciHeap<GKnote> openList=new FibonacciHeap<GKnote>();
	    	ArrayList<GKnote> closedList=new ArrayList<GKnote>();
	    	
	    	startKnote.setDistance(0);
	    	
	    	openList.insert(new FibonacciHeapNode<GKnote>(startKnote), startKnote.getDistance());
	    	
	    	while(!(openList.isEmpty())) {
	    			
	    		FibonacciHeapNode<GKnote> uFib=openList.removeMin();
	    		GKnote u=uFib.data;
	    		if(u.id==zielKnote.id) return;
	    		
	    		closedList.add(u);
	    		if(u.getVorganger()!=null) {             // kurzeste Weg !!!bis!!! Kante
		    		graphic.setColor(Color.ORANGE);
					graphic.drawLine(u.x+4, u.y+4, u.getVorganger().x+4, u.getVorganger().y+4);
					
				}
	    		expand_node(graphic,graph,openList,closedList,u,zielKnote);
	   
	    	}
	    	return;
	    	
	    }
	    
	    void expand_node(Graphics graphic,UGraph graph,FibonacciHeap<GKnote> openList,ArrayList<GKnote> closedList,GKnote u,GKnote ziel) {
	    	
	    	for(GKnote v:graph.nachbarn(u)) {
	    		
	    		FibonacciHeapNode<GKnote> vFibNode=openList.getNode(v.id);
	    		
	    		if(closedList.contains(v)) continue;
	    		
	    		double g=u.getDistance()+1;
	    		
	    		if(vFibNode!=null && g>=v.getDistance()) continue;
	    		
	    		
				try {
							Thread.sleep(50);
				} catch (InterruptedException e) {
							e.printStackTrace();
				}
				
				
				v.setVorganger(u);
	    		v.setDistance(g);
				
	    		double f=g+HEntfernung(v,ziel);
				
	    		
	    		if(vFibNode!=null) 
	    				openList.decreaseKey(vFibNode, f);
	    			
	    		else {
	    			graphic.setColor(Color.CYAN);
		    		graphic.drawLine(v.x+4, v.y+4, v.getVorganger().x+4, v.getVorganger().y+4);
	    			openList.insert(new FibonacciHeapNode<GKnote>(v), f);	
	    			}
	    		
	    	}
	    	
	    	
	    }
	    
	    double HEntfernung(GKnote x,GKnote y) {
	    	return Math.sqrt((y.x-x.x)^2+(y.y-x.y)^2);
	    }
	    
	    static void fillGraph(UGraph graph) throws IOException{
	    	
	    	FileReader in = null;
	    	
	    	
	        int tmpIdVonFile=0,tmpXKoordinateVonFile=0,tmpYKoordinateVonFile=0,canvasSizeX=0,canvasSizeY=0;
	        
	        
	         //File Input Stream
	    	 try {
	    		 
	             in = new FileReader("input1.txt");
	             
	             int tmp=0,indexDerZahl=1,spaceAnzahl=0,pkZeichenAnzahl=0;
	             char charSpeicher=' ';
	             
	             while (pkZeichenAnzahl<3 ) {
	            	 
	            	 charSpeicher = (char) in.read();
	            	 
	            	 
	            	 if(pkZeichenAnzahl==0) {//Anfang der Abschnitt 1
	            		 
	            		 if(charSpeicher>='0' && charSpeicher<='9') {//Konstruieren den Zahl
	   	              	  tmp+=charSpeicher-'0';
	   	              	  tmp*=10;
	   	                }
	            		 
	            		 if(charSpeicher==';' && indexDerZahl==2) {//Ende der Abschnitt 1.1
	            			 
	            			 tmp/=10;
	            			 ++pkZeichenAnzahl;
	            			 indexDerZahl=1;
	            			 spaceAnzahl=0;
	            			 tmpYKoordinateVonFile=tmp;
	            			 canvasSizeX=tmpXKoordinateVonFile;
	            			 canvasSizeY=tmpYKoordinateVonFile;
	            			 tmpXKoordinateVonFile=0;
	            			 tmpYKoordinateVonFile=0;
	            			 tmp=0;
	            			 continue;
	            			 
	            		 }
	            		 else if((charSpeicher<'0' || charSpeicher>'9') && indexDerZahl==1) {//Ende der Abschnitt 1.2
	            			 
	            			 tmp/=10;
	            			 
	            			 ++spaceAnzahl;
	            			 
	            			 indexDerZahl++;
	            			 tmpXKoordinateVonFile=tmp;
			              	 
	            			 
	            			 tmp=0;
	            			 
	            			 char tmpChar;
		              		  
		              		  //lauft nach einem Y Koordinate bis ';' oder bis einem Ziffer 
		              		  
		              		  while ((tmpChar = (char) in.read()) != ';' && tmpChar <'0' && tmpChar>'9') ;
		              				  
		              		  charSpeicher=tmpChar;
		              		  
		              		  if(charSpeicher>='0' && charSpeicher<='9') {//Konstruieren den Zahl
		                      	  tmp+=charSpeicher-'0';
		                      	  tmp*=10;
		                        }
		              		  
	            		 }
	            		 
	            		 
	            	}else if(pkZeichenAnzahl==1) {//Anfang der Abschnitt 2
	          	 
		                if(charSpeicher>='0' && charSpeicher<='9') {//Konstruieren den Zahl
		              	  tmp+=charSpeicher-'0';
		              	  tmp*=10;
		                }
		                
		                else if(charSpeicher==' ') {
		              	  tmp/=10;
		              	  
		              	  switch(indexDerZahl++) {
		              	  	case 1: tmpIdVonFile=tmp;break;
		              	  	case 2: tmpXKoordinateVonFile=tmp;break;
		              	  	case 3: tmpYKoordinateVonFile=tmp;break;
		              	  }
		              	  
		              	  tmp=0;
		              	  
		              	  if(++spaceAnzahl==3) {//erzeugen neue Knote
		              		  
		              		  
		              		  
		              		  graph.addKnote(tmpIdVonFile, tmpXKoordinateVonFile, tmpYKoordinateVonFile);
		              		  
		              		  spaceAnzahl=0;
		              		  indexDerZahl=1;
		              		  
		              		  char tmpChar;
		              		  
		              		  //lauft nach einem Y Koordinate bis ';' oder bis einem Ziffer 
		              		  
		              		  while ((tmpChar = (char) in.read()) != ';' && tmpChar <'0' && tmpChar>'9') ;
		              				  
		              		  charSpeicher=tmpChar;
		              		  
		              		  if(charSpeicher>='0' && charSpeicher<='9') {//Konstruieren den Zahl
		                      	  tmp+=charSpeicher-'0';
		                      	  tmp*=10;
		                        }
		              		}
		              	  
		              	 }
		                else if(spaceAnzahl==2 && indexDerZahl==3) {
		                	graph.addKnote(tmpIdVonFile, tmpXKoordinateVonFile, tmp/10);
		                	indexDerZahl=1;
		                	spaceAnzahl=0;
		                	tmp=0;
		                }
		                
		                
	                }else if(pkZeichenAnzahl==2) {//Anfang der Abschnitt 3
		   	          	 
		                if(charSpeicher>='0' && charSpeicher<='9') {//Konstruieren den Zahl
		              	  tmp+=charSpeicher-'0';
		              	  tmp*=10;
		                }
		                
		                else if(charSpeicher==' ') {
		              	  tmp/=10;
		              	  
		              	  switch(indexDerZahl++) {
		              	  	case 1: tmpIdVonFile=tmp;break;
		              	  	case 2: tmpXKoordinateVonFile=tmp;break;
		              	  	case 3: tmpYKoordinateVonFile=tmp;break;
		              	  }
		              	  
		              	  tmp=0;
		              	  
		              	  if(++spaceAnzahl==3) {//erzeugen neue Kante
		              		  
		              		
		              		  
		              		  graph.addKante(tmpIdVonFile, tmpXKoordinateVonFile, tmpYKoordinateVonFile);
		              		  
		              		  spaceAnzahl=0;
		              		  indexDerZahl=1;
		              		  
		              		  char tmpChar;
		              		  
		              		  //lauft nach einem Y Koordinate bis ';' oder bis einem Ziffer 
		              		  
		              		  while ((tmpChar = (char) in.read()) != ';' && tmpChar <'0' && tmpChar>'9') ;
		              				  
		              		  charSpeicher=tmpChar;
		              		  
		              		  if(charSpeicher>='0' && charSpeicher<='9') {//Konstruieren den Zahl
		                      	  tmp+=charSpeicher-'0';
		                      	  tmp*=10;
		                        }
		              		}
	                
	              	  
		                }
		                
		                else if(spaceAnzahl==2 && indexDerZahl==3) {
		                	graph.addKante(tmpIdVonFile, tmpXKoordinateVonFile, tmp/10);
		                	indexDerZahl=1;
		                	spaceAnzahl=0;
		                	tmp=0;
		                	}
		                
		             }
	            	 
	            	 
	            	 
	            	 if(charSpeicher==';') {
	              		++pkZeichenAnzahl;
	              	}
	            
	             }
	         }
	    	 catch(IOException x) {
	    		 System.out.println("Input error");
	    	 }finally {
	    		 in.close();
	    	 }
	    	graph.canvasSizeX=canvasSizeX;
	    	graph.canvasSizeY=canvasSizeY;
	    	
	    }
	    

}











