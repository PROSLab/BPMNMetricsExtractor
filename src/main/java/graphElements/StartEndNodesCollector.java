package graphElements;

import java.util.ArrayList;
import java.util.Vector;

public class StartEndNodesCollector {
	private Vector<Integer> initialNodes;
	private Vector<Integer> finalNodes;
	
	public StartEndNodesCollector(int [][] matrix, ArrayList<ArrayList<Integer>> adj) {
		this.initialNodes = new Vector<Integer>();
		this.finalNodes = new Vector<Integer>();
		this.collectNodes(matrix, adj);
	}
	
	public Vector<Integer> getInitialNodes() {
		return this.initialNodes;
	}
	
	public Vector<Integer> getFinalNodes() {
		return this.finalNodes;
	}
	
	private void collectNodes(int [][] matrix, ArrayList<ArrayList<Integer>> adj) { 
		//trova quelli finali dalla matrice di adiacenza
		for(int index = 0; index < adj.size();index++) {
    		ArrayList<Integer> x = adj.get(index);
    		if(x.size()==0) 
    			this.finalNodes.add(index);
		}
		//trova quelli iniziali da matrice di adiacenza
		for(int i = 0; i < matrix.length; i++) {
    		int sum = 0;
    		for(int j = 0; j <matrix[i].length; j++)
    			//somma tutti i valori della colonna
    			sum+= matrix[j][i];
    		//controlla se la somma della colonna uguale 1 
    		//in questo caso esiste un solo arco entrante
    		if(sum == 0)
    			this.initialNodes.add(i);
    	}
	}

}
