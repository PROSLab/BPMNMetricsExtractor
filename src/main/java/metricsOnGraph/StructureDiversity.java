package metricsOnGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class StructureDiversity {
	private Vector<Integer> initialNodes;
	private Vector<Integer> finalNodes;
	private ArrayList<ArrayList<Integer>> adj;
	private int pathNumber;
	private double D;
	
	public StructureDiversity(int[][] am, int [][] rm, ArrayList<ArrayList<Integer>> adj) {
		this.initialNodes = new Vector<Integer>();
		this.finalNodes = new Vector<Integer>();
		this.adj = adj;
		this.pathNumber = 0;
		this.collectNodes(am);
		this.setPathNumber(rm);
		this.D = (1 /(((double)this.initialNodes.size())*((double)this.finalNodes.size())))*this.pathNumber;
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
	
	public double getD() {
		return this.D;
	}
	
	/*
	 * Il metodo setPathNumber calcola il numero di cammini eterogenei possibili da ogni nodo iniziale 
	 * ad ogni nodo finale
	 */
	
	private void setPathNumber(int[][]reach) {
		boolean visited[] = new boolean[this.adj.size()];
		for(Integer finale : this.finalNodes) {
    		Arrays.fill(visited, false);
    		for(Integer initial: this.initialNodes) {
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
