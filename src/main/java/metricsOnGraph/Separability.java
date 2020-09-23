package metricsOnGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Separability {
	private int numberVertices;
	private ArrayList<ArrayList<Integer>> adj;
	private int time;
	private int cutVertices;
	private ArrayList<Integer> articulationResult;
	
	public Separability(int [][] adjMatrix) {
		this.time = 0;
		this.numberVertices = adjMatrix[0].length;
		this.adj = new ArrayList<ArrayList<Integer>>();
		//create new adjacency list with the undirected version of the graph
		for (int i = 0; i < adjMatrix[0].length; i++) {
        	this.adj.add(new ArrayList<Integer>()); 
        } 
        int i, j; 
        for (i = 0; i < adjMatrix[0].length; i++) { 
        	for (j = 0; j < adjMatrix.length; j++) { 
        		if (adjMatrix[i][j] == 1) {
        			this.adj.get(i).add(j);
        			this.adj.get(j).add(i);
        		} 
        	} 
         }
		articulationResult = new ArrayList<>();
		this.cutVertices = this.findArticulationPoints();
	}
	
	public double getSeparability() {
		return this.cutVertices / (((double)this.numberVertices) - 2.0);
	}
	
	private int findArticulationPoints(){
		int vertices = this.numberVertices;
		Map<Integer, Integer> visitTime = new HashMap<>();
        Map<Integer, Integer> lowTime = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        //created visited array
        boolean[] visited = new boolean[vertices];
        //this loop will handle disconnected graphs as well
        for (int i = 0; i <vertices ; i++) {
            if(!visited[i])
                DFS(i, visitTime, lowTime, visited, parent);
        }
        return articulationResult.size();

    }

    private void DFS(int currentVertex, Map<Integer, Integer> discoveryTime, Map<Integer, Integer> lowTime, boolean[] visited, Map<Integer, Integer> parent){
        int childCount = 0;
        boolean canArticulationPoint = false;
        visited[currentVertex] = true;
        discoveryTime.put(currentVertex, time);
        lowTime.put(currentVertex, time);
        time++;
        for (int i = 0; i <adj.get(currentVertex).size() ; i++) {
            int adjacentVertex = adj.get(currentVertex).get(i);
            if(!visited[adjacentVertex]){
                //make current vertex as parent of adjacent vertex and increase the child count for current vertex
                parent.put(adjacentVertex, currentVertex);
                childCount++;
                //make recursive call for the adjacent vertex
                DFS(adjacentVertex, discoveryTime, lowTime, visited, parent);
                //check for articulation point
                if(discoveryTime.get(currentVertex)<=lowTime.get(adjacentVertex)){
                    canArticulationPoint = true;
                } else{
                    //check the low point is adjacent vertex is less than current vertex,
                    //if yes then update the current vertex low point
                    int currLowTime = lowTime.get(currentVertex);
                    lowTime.put(currentVertex, Math.min(currLowTime, lowTime.get(adjacentVertex)));
                }
            }else{
                //if here means the vertex was visited earlier, certainly there is a back edge,
                // update the low time of current vertex with visited time of adjacent vertex if needed
                int currLowTime = lowTime.get(currentVertex);
                lowTime.put(currentVertex, Math.min(currLowTime, discoveryTime.get(adjacentVertex)));
            }
        }
        //after visiting all the adjacent vertices check if current vertex is articulation point
        if((parent.get(currentVertex)==null && childCount>1)||(parent.get(currentVertex)!=null && canArticulationPoint)){
            //current vertex is AP, add it to the list
            articulationResult.add(currentVertex);
        }
    }

}
