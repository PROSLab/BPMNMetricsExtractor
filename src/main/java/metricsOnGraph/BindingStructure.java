package metricsOnGraph;

public class BindingStructure {
	private double m;
	private double mmin;
	private double B;
	
	public BindingStructure(int edges, int nodes) {
		this.m = (double)edges;
		this.mmin = (double)(nodes - 1);
		this.B = (this.m - this.mmin) / this.mmin;
	}
	
	public double getB() {
		return this.B;
	}

}
