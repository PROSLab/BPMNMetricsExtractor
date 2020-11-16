package graphElements;

import java.util.ArrayList;
import java.util.List;

public class CycleDetector {
	private int v;
	private boolean[] visited;
	private boolean[] recStack;
	private ArrayList<ArrayList<Integer> > adj; 

	
	public CycleDetector( ArrayList<ArrayList<Integer> > adj){
		this.v = adj.size();
		this.visited = new boolean[v]; 
		this.recStack = new boolean[v];
		this.adj = adj;
	}
	
	public boolean isCyclic() { 
		for (int i = 0; i < this.v; i++) 
			if (isCyclicUtil(i, visited, recStack)) 
                return true; 
		return false; 
    } 
	
	private boolean isCyclicUtil(int i, boolean[] visited, boolean[] recStack) { 
		//segna il nodo corrente come visitato e parte dello stack della ricorsione
		if (recStack[i]) 
			return true; 
		if (visited[i]) 
			return false; 
		visited[i] = true;
		recStack[i] = true; 
		List<Integer> children = adj.get(i); 
		for (Integer c : children) 
			if (isCyclicUtil(c, visited, recStack)) 
				return true; 
		recStack[i] = false; 
		return false; 
		}

}
