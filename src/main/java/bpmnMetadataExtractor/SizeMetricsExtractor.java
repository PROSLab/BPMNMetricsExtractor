package bpmnMetadataExtractor;

import java.util.ArrayList;
//import java.util.Collection;

//import org.camunda.bpm.model.bpmn.instance.FlowNode;
//import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
//import org.camunda.bpm.model.bpmn.instance.StartEvent;
//import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class SizeMetricsExtractor {
	
	//private ArrayList<String> visitedNodes;
	//private BpmnBasicMetricsExtractor basicExtractor;
	private ArrayList<ArrayList<Integer>> adj;
	private int n;
	private double diam;
	
	public SizeMetricsExtractor(int n, ArrayList<ArrayList<Integer>> adj) {
		//visitedNodes = new ArrayList<String>();
		//this.basicExtractor = basicExtractor;
		this.adj = adj;
		this.n = n;
		this.diam = this.setDiam();
	}

	/*public double getDiam() {
		Collection<ModelElementInstance> startNodes = basicExtractor.getCollectionOfElementType(StartEvent.class);
		ArrayList<Integer> results = new ArrayList<Integer>();
		for (ModelElementInstance startNodeModel : startNodes) {
			visitedNodes.clear();
			FlowNode startNode = (FlowNode) startNodeModel;
			results.add(calculateMaxDiam(startNode, 0));
		}
		int toReturn = 0;
		for (int result : results) {
			toReturn = result > toReturn ? result : toReturn;
		}
		return (double) toReturn;
	}
	
	private int calculateMaxDiam(FlowNode sourceNode, int pathValue) {
		if (!visitedNodes.contains(sourceNode.getId())) {
			visitedNodes.add(sourceNode.getId());
		} else {
			return pathValue;
		}
		int toReturn = 0;
		int tempDiamValue = 0;
		Collection<SequenceFlow> flows = sourceNode.getOutgoing();
		for (SequenceFlow flow : flows) {
			try {
				tempDiamValue = calculateMaxDiam(flow.getTarget(), pathValue + 1);
				if (tempDiamValue > toReturn) {
					toReturn = tempDiamValue;
				}
			}catch(Exception e) {continue;}
		}
		if (flows.size() == 0) {
			toReturn = pathValue;
		}
		visitedNodes.remove(sourceNode.getId());
		return toReturn;
	}*/
	
	public double getDiam() {
		return this.diam;
	}
	
	private void dfs(int node, ArrayList<ArrayList<Integer>> adj, int dp[], boolean visited[]) { 
		visited[node] = true; 
		for (int i = 0; i < adj.get(node).size(); i++) { 
			if (!visited[adj.get(node).get(i)]) 
				dfs(adj.get(node).get(i), adj, dp, visited); 
			// Store the max of the paths 
			dp[node] = Math.max(dp[node], 1 + dp[adj.get(node).get(i)]); 
			} 
		}
	
	private double setDiam() { 
		int[] dp = new int[n+1]; 
		boolean[] visited = new boolean[n + 1]; 
		// Call DFS for every unvisited vertex 
		for (int i = 0; i < n; i++) { 
			if (!visited[i]) 
				dfs(i, adj, dp, visited);
			} 
		int ans = 0; 
		// Traverse and find the maximum of all dp[i] 
		for (int i = 0; i <= n; i++) { 
			ans = Math.max(ans, dp[i]);
			} 
		return ans; 
	}  
	
}
