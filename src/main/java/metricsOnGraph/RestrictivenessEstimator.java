package metricsOnGraph;

public class RestrictivenessEstimator {
	private double nodes;
	private int[][] reach;
	private double RT;
	
	public RestrictivenessEstimator(int nodes, int [][]rm) {
		this.nodes = nodes;
		this.reach = rm;
		this.RT = this.setRT();
	}
	
	private double setRT() {
		int sommaReach = 0;
		for(int i = 0; i < this.reach.length; i++)
			for(int j = 0; j < this.reach.length; j++)
				sommaReach+= this.reach[i][j];
		return (2*((double)sommaReach) - 6*(this.nodes-1))/((this.nodes-2)*(this.nodes-3));
	}
	
	public double getRT() {
		return this.RT;
	}

}
