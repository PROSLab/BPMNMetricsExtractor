package graphElements;

public class Edge {
	private String sourceId;
	private String targetId;
	
	public Edge(String source, String target) {
		this.sourceId = source;
		this.targetId = target;
		}
	
	public String getSource() {
		return this.sourceId;
	}
	
	public String getTarget() {
		return this.targetId;
	}

}
