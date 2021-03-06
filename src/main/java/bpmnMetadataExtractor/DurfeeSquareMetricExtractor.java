package bpmnMetadataExtractor;

import java.util.ArrayList;
import java.util.Collections;
/**
 * Class that calculates the DSM metric
 * It equals d if there are d types of elements which occur at least d times in the model (each),
 * and the other types occur no more than d times (each)
 * @author PROSLabTeam
 *
 */
public class DurfeeSquareMetricExtractor {
	private BpmnBasicMetricsExtractor basicMetricsExtractor;
	private ArrayList<Integer> flowNodes;
	
	public DurfeeSquareMetricExtractor(BpmnBasicMetricsExtractor basicMetricsExtractor) {
		this.basicMetricsExtractor = basicMetricsExtractor;
		this.flowNodes = this.getFlowNodes(new ArrayList<Integer>());
	}
	
	public double getDurfeeMetric() {
		return this.calculateDurfee(this.flowNodes);
	}
	
	public double getPerfectSquareMetric(){
		try {
			return this.getPerfectSquareMetric(this.flowNodes);
		} catch (ArithmeticException e) {
			return 0.0;
		}
		
	}
	
	public double getLayoutComplexityMetric(){
		return this.getLayoutComplexity(this.flowNodes);
	}
	/**
	 * Method that gets all the flow nodes in the model and separates them into an ArrayList
	 * @param flowNodes
	 * @return ArrayList of flownodes
	 */
	public ArrayList<Integer> getFlowNodes(ArrayList<Integer> flowNodes){
		flowNodes.add(this.basicMetricsExtractor.getActivities() - this.basicMetricsExtractor.getSubprocesses() - this.basicMetricsExtractor.getTasks());
		flowNodes.add(this.basicMetricsExtractor.getBoundaryEvents());
		flowNodes.add(this.basicMetricsExtractor.getBusinessRuleTasks());
		flowNodes.add(this.basicMetricsExtractor.getCallActivities());
		flowNodes.add(this.basicMetricsExtractor.getCatchEvents() - this.basicMetricsExtractor.getBoundaryEvents() - this.basicMetricsExtractor.getIntermediateCatchEvents() - this.basicMetricsExtractor.getStartEvents());
		flowNodes.add(this.basicMetricsExtractor.getComplexDecisions());
		flowNodes.add(this.basicMetricsExtractor.getEndEvents());
		flowNodes.add(this.basicMetricsExtractor.getFlowJoiningAndDividingEventBasedGateways());
		flowNodes.add(this.basicMetricsExtractor.getEvents() - this.basicMetricsExtractor.getCatchEvents() - this.basicMetricsExtractor.getThrowEvents());
		flowNodes.add(this.basicMetricsExtractor.getExclusiveEventBasedDecisions());
		flowNodes.add(this.basicMetricsExtractor.getExclusiveDataBasedDecisions());
		flowNodes.add(this.basicMetricsExtractor.getInclusiveDecisions());
		flowNodes.add(this.basicMetricsExtractor.getIntermediateCatchEvents());
		flowNodes.add(this.basicMetricsExtractor.getIntermediateThrowEvents());
		flowNodes.add(this.basicMetricsExtractor.getManualTasks());
		flowNodes.add(this.basicMetricsExtractor.getParallelGateways());
		flowNodes.add(this.basicMetricsExtractor.getReceiveTasks());
		flowNodes.add(this.basicMetricsExtractor.getScriptTasks());
		flowNodes.add(this.basicMetricsExtractor.getSendTasks());
		flowNodes.add(this.basicMetricsExtractor.getServiceTasks());
		flowNodes.add(this.basicMetricsExtractor.getStartEvents());
		flowNodes.add(this.basicMetricsExtractor.getSubprocesses());
		flowNodes.add(this.basicMetricsExtractor.getTasks() - this.basicMetricsExtractor.getBusinessRuleTasks() - this.basicMetricsExtractor.getManualTasks() - this.basicMetricsExtractor.getReceiveTasks() - this.basicMetricsExtractor.getScriptTasks() - this.basicMetricsExtractor.getSendTasks() - this.basicMetricsExtractor.getServiceTasks() - this.basicMetricsExtractor.getUserTasks());
		flowNodes.add(this.basicMetricsExtractor.getThrowEvents() - this.basicMetricsExtractor.getEndEvents() - this.basicMetricsExtractor.getIntermediateThrowEvents());
		flowNodes.add(this.basicMetricsExtractor.getTransactions());
		flowNodes.add(this.basicMetricsExtractor.getUserTasks());
		flowNodes.removeIf(a -> (a == 0));
		Collections.sort(flowNodes);
		Collections.reverse(flowNodes);
		return flowNodes;
	}
	
	/**
	 * Durfee Metric Algorithm
	 * @param flowNodes
	 * @return Durfee Square
	 */
	private int calculateDurfee(ArrayList<Integer> flowNodes){
		int ds = 0;
		int max = 0;
		if (!flowNodes.isEmpty()){
			max= Collections.max(flowNodes);	
			if (flowNodes.size() == 1){
				ds = 1;
			} else {
				int [][]durfeeMatrix = this.getOrderedMatrix(flowNodes, max);
//				this.printMatrix(durfeeMatrix);
				if (max > flowNodes.size())
					ds = this.getDurfeeMetricFromMatrix(durfeeMatrix, max);
				else
					ds = this.getDurfeeMetricFromMatrix(durfeeMatrix, flowNodes.size());
			}
		}
		return ds;
	}
	/**
	 * Method that builds a matrix based on the flowNodes Arraylist
	 * @param flowNodes
	 * @param max
	 * @return matrix of flowNodes values
	 */
	public int[][] getOrderedMatrix(ArrayList<Integer>flowNodes, int max){
		if (max > flowNodes.size()){
			int [][] durfeeMatrix = new int [max][max];
			for (int i = 0; i < flowNodes.size(); i++){
				int value = flowNodes.get(i);
				for (int j = 0; j < max; j++){
					if (value > 0){
						durfeeMatrix[i][j] = 1;
						value--;
					}
				}
			}
			return durfeeMatrix;
		} else {
			int [][] durfeeMatrix = new int [flowNodes.size()][flowNodes.size()];
			for (int i = 0; i < flowNodes.size(); i++){
				int value = flowNodes.get(i);
				for (int j = 0; j < flowNodes.size(); j++){
					if (value > 0){
						durfeeMatrix[i][j] = 1;
						value--;
					}
				}
			}
			return durfeeMatrix;
		}	
	}
	
	/**
	 * Calculate durfee square metric from matrix
	 * @param matrix
	 * @param max
	 * @return
	 */
	private int getDurfeeMetricFromMatrix(int [][] matrix, int max){
		int toReturn = 0;
		for (int i = 0; i < max; i ++){
			for (int j = 0; j < max; j++){
				if (i == j && matrix[i][j] == 1){
					toReturn ++;
				}
			}
		}
		return toReturn;
	}
	
	/**
	 * Method that calculates the perfect square metric
	 * @param flowNodes
	 * @return ps = perfect square
	 */
	private int getPerfectSquareMetric(ArrayList<Integer> flowNodes){
		int ps = flowNodes.size();
		int flowNodesSum = 0;
		for (int i = 0; i < flowNodes.size(); i++){
			flowNodesSum += flowNodes.get(i);
		}
		do {
			ps--;	
		} while(ps > (flowNodesSum/ps));
		return ps;
	}
	
	/**
	 * Method that calculates the layout complexity metric based on the Bonsiepe's Technique
	 * @param flowNodes
	 * @return
	 */
	private double getLayoutComplexity(ArrayList<Integer> flowNodes){
		double toReturn = 0.0;
		double p = 0.0;
		int totalElements = 0;
		double size = flowNodes.size();
		
		for (int i = 0; i < flowNodes.size(); i++){
			double nodeAtIndex = flowNodes.get(i);
			p = nodeAtIndex / size;
			totalElements += flowNodes.get(i);
			toReturn += p * this.logBase2(p);
		}
	
		toReturn *= -totalElements;
		toReturn = Math.round(toReturn * 100.0) / 100.0;
		return toReturn;
	}
	
	/**
	 * print matrix
	 * @param matrix
	 */
	public void printMatrix(int[][] matrix) {
	    for (int row = 0; row < matrix.length; row++) {
	        for (int col = 0; col < matrix[row].length; col++) {
	            System.out.printf("%4d", matrix[row][col]);
	        }
	        System.out.println();
	    }
	}
	
	/**
	 * Base 2 logarithm implementation
	 * @param d
	 * @return
	 */
	private double logBase2(double d) {
		double toReturn = 0;
		try {
			toReturn = Math.log(d) / Math.log(2);
		} catch (ArithmeticException e) {
			System.out.println(e);
		}
		return toReturn;
	}


}
