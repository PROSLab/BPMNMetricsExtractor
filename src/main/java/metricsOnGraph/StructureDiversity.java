package metricsOnGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class StructureDiversity {
	private ArrayList<ArrayList<Integer>> adj;
	private int pathNumber;
	private double D;
	
	public StructureDiversity(int [][] rm, ArrayList<ArrayList<Integer>> adj, Vector<Integer> initialNodes, Vector<Integer> finalNodes) {
		this.adj = adj;
		this.pathNumber = 0;
		this.setPathNumber(rm, initialNodes, finalNodes);
		this.D = (1 /(((double)initialNodes.size())*((double)finalNodes.size())))*this.pathNumber;
	}
	
	public double getD() {
		return this.D;
	}
	
	/*
	 * Il metodo setPathNumber calcola il numero di cammini eterogenei possibili da ogni nodo iniziale 
	 * ad ogni nodo finale
	 */
	
	private void setPathNumber(int[][]reach, Vector<Integer> initialNodes, Vector<Integer> finalNodes) {
		boolean visited[] = new boolean[this.adj.size()];
		for(Integer finale : finalNodes) {
    		Arrays.fill(visited, false);
    		for(Integer initial: initialNodes) {
    			//se esiste almeno un path dal nodo iniziale a quello finale
    			if(reach[initial][finale]==1) {
    				this.pathNumber+=this.countPaths(initial, finale, visited);
    			}
    		}
    	}
	}
	
	//calcola il numero di cammini dalla nodo iniziale source al nodo finale destination  
	
    private int countPaths(int s, int d, boolean visited[]) {
    	visited[d]=false;
    	//chiama il metodo ricorsivo per contare tutti i cammini 
    	int pathCount = 0; 
    	pathCount = countPathsUtil(s, d, 
                        visited,  
                        pathCount); 
    	return pathCount; 
    }
    
    private int countPathsUtil(int u, int d, boolean visited[], int pathCount) { 
    	//segna il nodo in esame come visitato 
    	visited[u] = true; 
    	//se il vertice in esame è quello finale, allora il numero aumenta 
    	if (u == d) 
    		pathCount++;  
    	//itera per tutti i nodi adiacenti a quello in esame 
    	else { 
    		for(Integer i : this.adj.get(u)) {
    			int n = i; 
    			if (!visited[n]) { 
    				pathCount = countPathsUtil(n, d, visited, pathCount);
    			} 
    		} 
    	} 
    	return pathCount; 
    }

}
