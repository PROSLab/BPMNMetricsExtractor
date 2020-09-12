package graphElements;

public class Node {
	private String id;
	private int vertexNumber;
	
	public Node(String id, int vertex) {
		this.id=id;
		this.vertexNumber = vertex;
	}
	 
	public String getId() {
		return this.id;
	}
	
	public int getVertexNumber() {
		return this.vertexNumber;
	}

}

