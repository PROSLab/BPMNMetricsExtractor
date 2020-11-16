package metricsOnGraph;

import java.util.Vector;

public class Diameter {
	private int L;
	private int nodesNumber;
	private Vector<Integer> shortestPaths;
	
	public Diameter(int[][] adjMatrix, Vector<Integer> startNodes) {
		this.nodesNumber = adjMatrix.length;
		this.shortestPaths = new Vector<Integer>();
		//find all start events
		for(Integer i : startNodes)
			this.dijkstra(adjMatrix, i);
		this.L = this.setL();
	}
	
	public int getL() {
		return this.L;
	}
	
	private int minDistance(int dist[], Boolean visited[]) { 
        int min = Integer.MAX_VALUE;
        int minIndex = -1; 
        for (int v = 0; v < nodesNumber; v++) 
            if (visited[v] == false && dist[v] <= min) { 
                min = dist[v]; 
                minIndex = v; 
            } 
        return minIndex; 
    }  
  
    //metodo che implementa algoritmo di Dijkstra per i cammini piu corti di un singolo nodo
    //basato sulla matrice di adiacenza del grafo
	
    private void dijkstra(int graph[][], int src) {
    	//array che contiene le distanze piu corte dal nodo source ad ogni altro nodo
        int dist[] = new int[nodesNumber]; 
        //tiene traccia dei vertici inclusi 
        Boolean visited[] = new Boolean[nodesNumber]; 
        //inizializza i due array precedentemente creati 
        for (int i = 0; i < nodesNumber; i++) { 
            dist[i] = Integer.MAX_VALUE; 
            visited[i] = false; 
        } 
        //distanza del source node da se stesso 
        dist[src] = 0; 
        //trova il cammino piu corto per ogni vertice 
        for (int count = 0; count < nodesNumber - 1; count++) { 
            //sceglie il vertice con minor distanza dal set di vertici non ancora analizzato
            int u = minDistance(dist, visited); 
            //segna il vertice analizzato 
            visited[u] = true; 
            //aggiorna il valore dist dei vertici adiacenti dei vertici selezionati 
            for (int v = 0; v < nodesNumber; v++) 
            	//aggiorna dist[v] solo se non analizzato precedentemente, esiste un arco da u a v 
                //e il peso totale del cammino dalla source a v attraverso u piu piccolo del corrente dist[v] 
                if (!visited[v] && graph[u][v] != 0 &&  
                   dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) 
                    dist[v] = dist[u] + graph[u][v]; 
        } 
        //salva il cammino piu lungo tra tutti i cammini piu corti del nodo source
        int shortestPath = 0;
        for(int x : dist)
        	if(x != Integer.MAX_VALUE)
        		shortestPath = Math.max(shortestPath, x);
        this.shortestPaths.add(shortestPath);
    }
    
    private int setL() {
    	int l = 0;
    	for(Integer i : this.shortestPaths)
    		l=Math.max(l, i);
    	return l;
    }
  

}

