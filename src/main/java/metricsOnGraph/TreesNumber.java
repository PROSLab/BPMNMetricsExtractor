package metricsOnGraph;

public class TreesNumber {
	private int [][] D;
	private int T;
	
	public TreesNumber(int [][]A) {
		this.D = createD(A);
		this.T = setT();
		
	}
	
	public int getT() {
		return T;
	}
	
	private int [][] createD(int[][] adjMatrix){
		int [][] d1 = new int[adjMatrix.length][adjMatrix.length];
        for(int i = 0; i< adjMatrix.length; i++) {
        	for(int j = 0; j< adjMatrix.length; j++)
        	d1[i][i]+= adjMatrix[i][j];
        }
        int d2[][] = new int[adjMatrix.length][adjMatrix.length];
        System.out.println("D Matrix:");
        for(int i = 0; i < adjMatrix.length; i++){ 
        	for(int j = 0; j < adjMatrix.length; j++){  
                d2[i][j] = d1[i][j] - adjMatrix[i][j];
            	System.out.print(" "+d2[i][j]);
            } 
            System.out.print("\n");
        }
        return d2;
	}
	
	private int setT() {
		//int checkSink = 0;
		int diagonalMinor = 1;
		for(int i = 0; i< this.D.length; i++) {
			if(this.D[i][i]!=0)
				diagonalMinor*=this.D[i][i];
		}
		return diagonalMinor;
	}

}
