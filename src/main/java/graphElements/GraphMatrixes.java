package graphElements;

import java.util.Vector;

public class GraphMatrixes {
	private int vertex;
	private int edge;
	private Vector<Node> nodes;
	private Vector<Edge> edges;
    private int adjMatrix[][];
    private int reachMatrix[][];

    public GraphMatrixes(Vector<Edge> edges, Vector<Node> nodes) {
    	this.vertex = nodes.size();
    	this.edge = edges.size();
    	this.nodes = nodes;
    	this.edges = edges;
        this.adjMatrix = new int[nodes.size()][nodes.size()]; 
        this.reachMatrix= new int[nodes.size()][nodes.size()];
        this.buildAdjacencyMatrix();
        this.buildReachabilityMatrix();
    }

    private void buildAdjacencyMatrix() {
    	int source=0; 
    	int target = 0;
    	for(Edge e : this.edges) {
    		for(Node n : this.nodes) {
    			//se id del nodo source dell'arco in esame e id del nodo in esame sono uguali
    			//salva il numero del nodo in esame in source
    			if(e.getSource().equals(n.getId()))
    				source = n.getVertexNumber();
    			//se id del nodo target dell'arco in esame e id del nodo in esame sono uguali
    			//salva il numero del nodo in esame in target
    			if(e.getTarget().equals(n.getId())) 
    				target = n.getVertexNumber();
    		}
    		this.adjMatrix[source][target]=1;
    	}
    }
    
    private void buildReachabilityMatrix() {
    	//inizializza la reachMatrix allo stesso modo della matrice di adiacenza
		int  i, j, k;
		for (i = 0; i < this.vertex; i++) 
           for (j = 0; j < this.vertex; j++) 
               this.reachMatrix[i][j] = this.adjMatrix[i][j]; 
		//aggiunge tutti i vertici uno alla volta al set dei vertici intermedi
		/*
		 * Ad ogni iterazione, abbiamo i valori di raggiungibilita per ogni 
		 * coppia di vertici in modo che i valori di raggiungibilita considerano solo i vertici 
		 * nel set {0, 1, 2, .. k-1} come vertici intermedi
		 * Al termine di una iterazione, il vertice no. k viene aggiunto al set dei
		 * vertici intermedi e il set diventa {0, 1, 2, .. k}
		 */
		for (k = 0; k < this.vertex; k++) {
			//seleziona tutti i vertici come sorgente uno alla volta 
           for (i = 0; i < this.vertex; i++) {
        	   //seleziona tutti i vertici come destinazione per la sorgente selezionata
               for (j = 0; j < this.vertex; j++) { 
            	   //se il vertice k si trova nel path da i a j, allora il valore di reach[i][j] = 1
                   this.reachMatrix[i][j] = (this.reachMatrix[i][j]!=0) || 
                            ((this.reachMatrix[i][k]!=0) && (this.reachMatrix[k][j]!=0))?1:0; 
               } 
           } 
       } 
		//rende la la matrice riflessiva
		for (i = 0; i < this.vertex; i++) 
			this.reachMatrix[i][i] = 1;
    }

    public int getVertix() {
    	return this.vertex;
    }
    
    public int getEdge() {
    	return this.edge;
    }
    
    public int[][] getAdjacencyMatrix(){
    	return this.adjMatrix;
    }
    
    public int[][] getReachabilityMatrix(){
    	return this.reachMatrix;
    }

    public void printAdjacencyMatrix() {
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < vertex; i++) {
            for (int j = 0; j <vertex ; j++) {
                System.out.print(adjMatrix[i][j]+ " ");
            }
            System.out.println();
        }
    }
    
    public void printReachabilityMatrix() {
    	System.out.println("Reflexive Transitive Closure:"); 
        for (int i = 0; i < vertex; i++){ 
            for (int j = 0; j < vertex; j++) 
                System.out.print(this.reachMatrix[i][j]+" "); 
            System.out.println();
            } 
        } 

}

