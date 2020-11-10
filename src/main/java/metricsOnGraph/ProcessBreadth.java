package metricsOnGraph;

import java.util.ArrayList;
import java.util.Vector;

public class ProcessBreadth {
	private ArrayList<ArrayList<Integer>> adj;
	private int pathNumber;
	private int ndop;
	
	public ProcessBreadth(int [][] rm, ArrayList<ArrayList<Integer>> adj, Vector<Integer> initialNodes, Vector<Integer> finalNodes) {
		this.adj = adj;
		this.pathNumber = 0;
		this.ndop = 0;
		this.setProcessBreadth(rm, initialNodes, finalNodes);
	}
	
	public int getProcessBreadth() {
		return this.pathNumber;
	}
	
	public int getNDOP() {
		return this.ndop;
	}
	
	// A recursive method to count all paths from 'u' to 'd'. 
    private int countPathsUtil(int u, int d, 
                       int pathCount) { 
    	// If current vertex is same as destination, then increment count 
        if (u == d) { 
            pathCount++; 
        } 
        // Recur for all the vertices adjacent to this vertex 
        else { 
        	this.ndop++;
        	for(Integer i : this.adj.get(u)) {
                int n = i; 
                pathCount = countPathsUtil(n, d, pathCount); 
            } 
        } 
        return pathCount; 
    } 
    
    // Returns count of paths from 's' to 'd' 
    private int countPaths(int s, int d) { 
    	// Call the recursive method to count all paths 
        int pathCount = 0; 
        pathCount = countPathsUtil(s, d, 
                                   pathCount); 
        return pathCount; 
    } 
  
    // Returns count of paths from initial nodes to final ones 
    private void setProcessBreadth( int[][]reach, Vector<Integer> initialNodes, Vector<Integer> finalNodes) { 
    	for(Integer finale : finalNodes) {
    		for(Integer initial: initialNodes) {
    			//se esiste almeno un path dal nodo iniziale a quello finale
    			if(reach[initial][finale]==1) {
    				this.pathNumber += this.countPaths(initial, finale);
    			}
    		}
    	} 
    } 

}
