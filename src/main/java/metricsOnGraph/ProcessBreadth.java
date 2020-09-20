package metricsOnGraph;

import java.util.ArrayList;
import java.util.Vector;

public class ProcessBreadth {
	private Vector<Integer> initialNodes;
	private Vector<Integer> finalNodes;
	private ArrayList<ArrayList<Integer>> adj;
	private int pathNumber;
	
	public ProcessBreadth(int[][] am, int [][] rm, ArrayList<ArrayList<Integer>> adj) {
		this.initialNodes = new Vector<Integer>();
		this.finalNodes = new Vector<Integer>();
		this.adj = adj;
		this.pathNumber = 0;
		this.collectNodes(am);
		this.setProcessBreadth(rm);
	}
	
	public int getProcessBreadth() {
		return this.pathNumber;
	}
	
	private void collectNodes(int [][] matrix) { 
		//trova quelli finali dalla matrice di adiacenza
		for(int index = 0; index < this.adj.size();index++) {
    		ArrayList<Integer> x = this.adj.get(index);
    		if(x.size()==0) 
    			this.finalNodes.add(index);
		}
		//trova quelli iniziali da matrice di adiacenza
		for(int i = 0; i < matrix.length; i++) {
    		int sum = 0;
    		for(int j = 0; j <matrix[i].length; j++)
    			//somma tutti i valori della colonna
    			sum+= matrix[j][i];
    		//controlla se la somma della colonna è uguale a 1 
    		//in questo caso esiste un solo arco entrante
    		if(sum == 0)
    			this.initialNodes.add(i);
    	}
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
    private void setProcessBreadth( int[][]reach) { 
    	for(Integer finale : this.finalNodes) {
    		for(Integer initial: this.initialNodes) {
    			//se esiste almeno un path dal nodo iniziale a quello finale
    			if(reach[initial][finale]==1) {
    				this.pathNumber += this.countPaths(initial, finale);
    			}
    		}
    	} 
    } 

}
