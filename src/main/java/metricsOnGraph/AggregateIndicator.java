package metricsOnGraph;

public class AggregateIndicator {
	private double AC;
	
	public AggregateIndicator(int l, double b, double d) {
		this.AC = Math.log10((b+l+d)/3.0);
	}
	
	public double getAC() {
		return this.AC;
	}

}
