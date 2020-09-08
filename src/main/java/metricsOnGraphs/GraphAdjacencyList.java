package metricsOnGraphs;

import java.util.ArrayList;

public class GraphAdjacencyList {
	private ArrayList<ArrayList<Integer>> adjList; 
    private int [][] adjMatrix;
	
	public GraphAdjacencyList(int [][] m) {
		this.adjList = new ArrayList<ArrayList<Integer>>();
		this.adjMatrix = m;
		this.convertMatrixToLIst();
		}
	
	private void convertMatrixToLIst(){
		//crea una nuova lista per ogni vertice così che i nodi adiacenti possono essere salvati
		for (int i = 0; i < this.adjMatrix[0].length; i++) {
        	this.adjList.add(new ArrayList<Integer>()); 
        } 
        int i, j; 
        for (i = 0; i < this.adjMatrix[0].length; i++) { 
        	for (j = 0; j < this.adjMatrix.length; j++) { 
        		if (this.adjMatrix[i][j] == 1) {
        			this.adjList.get(i).add(j);
        		} 
        	} 
        } 
    } 
	
	public ArrayList<ArrayList<Integer>> getAdj() {
		return this.adjList;
	}
	
	public void printArrayList() { 
		System.out.println("Adjacency List:");
    	for (int v = 0; v < this.adjList.size(); v++) { 
            System.out.print(v); 
            for (Integer u : this.adjList.get(v)) { 
                System.out.print(" -> " + u); 
            } 
            System.out.println(); 
        } 
    }

}

