package metricsOnGraph;

import java.util.ArrayList;
import java.util.Arrays;

public class ComplexityIndex {
	private int reductions;
	private int[][] adjMatrix;
	private ArrayList<ArrayList<Integer>> adjList;
	
	public ComplexityIndex(int [][] adjMatrix, ArrayList<ArrayList<Integer>> adjList) {
		this.adjList = adjList;
		this.adjMatrix = adjMatrix;
		this.reductions = setCI(0,0);
	}
	
	public int getCI() {
		return this.reductions;
	}
	
	private int setCI(int check, int CI) {
		int sum = 0;
		for(ArrayList<Integer> list : this.adjList)
			sum += list.size();
		if(sum == 0)
			return CI-1;
		if(sum == check) {
			int reduction = this.doNodeReductionSingleOutgoing();
			if(reduction == 1)
				CI += reduction;
			else return -1;
		}
		check = sum;
		this.doSeriesReduction();
		return setCI(check, CI);
	}
	
	private void doParallelReduction() {
		for(int row = 0; row < this.adjMatrix.length; row++)
			for(int column = 0; column < this.adjMatrix.length; column++)
				if(this.adjMatrix[row][column]>1) {
					this.adjMatrix[row][column]=1;
					//toglie dalla lista di adiacenza tutti i valori uguali e ne riaggiunge uno solo
					this.adjList.get(row).removeAll(Arrays.asList(column));
					this.adjList.get(row).add(column);
				}
    }
	
	private void doSeriesReduction() {
		for(int u = 0; u < this.adjList.size();u++) {
    		//esamina lista di interi della lista di liste di interi, dalla prima all'ultima
    		ArrayList<Integer> x = this.adjList.get(u);
    		//controlla se il nodo corrispondente alla lista di interi in esame ha un solo arco uscente
    		if(x.size()==1) {
    			int sum = 0;
    			int row = -1;
    			//salva l'indice dell'arco entrante 
    			for(int i = 0; i <this.adjMatrix.length; i++) {
    				if(this.adjMatrix[i][u]==1)
    					row = i;
    				sum += this.adjMatrix[i][u];
    			}
    			//check per verificare presenza di unico arco entrante
    			if(sum == 1) {
    				//toglie nella matrice di adiacenza l'arco entrante che va dal nodo precedente a quello da ridurre 
					this.adjMatrix[row][u]--;
					//aggiunge un arco entrante dal nodo precedente a quello successivo al nodo da ridurre
					this.adjMatrix[row][x.get(0)]++;
					//prima del remove per eventualità cappio
					//aggiunge nodo successivo al nodo ridotto alla lista del precedente
					this.adjList.get(row).add(x.get(0));
					//rimuovo nodo ridotto dalla lista del precedente
					this.adjList.get(row).remove(Integer.valueOf(u));
					//elimina connessione da nodo da ridurre a nodo successivo
					this.adjMatrix[u][x.get(0)]--;
					//rimuove il nodo ridotto dalla lista in esame 
	    			x.remove(0);
	    			this.doParallelReduction();
    			}
    		}
		}
	}
	
    /*
     * Il seguente metodo svolge la riduzione di nodo per tutti quei nodi che hanno un unico arco uscente
     */
	
    private int doNodeReductionSingleOutgoing() {
    	for(int u = 0; u < this.adjList.size();u++) {
    		//esamina lista di interi della lista di liste di interi, dalla prima all'ultima
    		ArrayList<Integer> x = this.adjList.get(u);
    		//controlla se il nodo corrispondente alla lista di interi in esame ha un solo arco uscente
    		if(x.size()==1) {
    			//aggiorna ogni nodo adiacente al nodo da ridurre 
    			for(int riga=0; riga <this.adjMatrix.length; riga++) {
    				if(this.adjMatrix[riga][u] == 1) {
    					//toglie nella matrice di adiacenza l'arco entrante che va dal nodo precedente a quello da ridurre 
    					this.adjMatrix[riga][u]--;
    					//aggiunge un arco entrante dal nodo precedente a quello successivo al nodo da ridurre
    					this.adjMatrix[riga][x.get(0)]++;
    					//prima del remove per eventualità cappio
    					//aggiunge nodo successivo al nodo ridotto alla lista del precedente
    					this.adjList.get(riga).add(x.get(0));
    					//rimuovo nodo ridotto dalla lista del precedente
    					this.adjList.get(riga).remove(Integer.valueOf(u)); 
    				}
    			}
    			//elimina connessione da nodo da ridurre a nodo successivo
				this.adjMatrix[u][x.get(0)]--;
				//rimuove il nodo ridotto dalla lista in esame 
    			x.remove(0);
    			this.doParallelReduction();
    			return 1;
    		}
    	} 
    	return this.doNodeReductionSingleIncoming();
    }
    
    /*
     * Il seguente metodo svolge la riduzione di nodo per tutti quei nodi che hanno un unico arco entrante
     */
    
   private int doNodeReductionSingleIncoming() {
		int riga = -1;
		//controlla se il nodo ha un solo arco entrante, analizzando le colonne della matrice
    	for(int i = 0; i < this.adjMatrix.length; i++) {
    		int sum = 0;
    		for(int j = 0; j <this.adjMatrix[i].length; j++) {
    			//controlla se esiste un arco entrante
    			if(this.adjMatrix[j][i]==1) {
    				//salva l'indice del nodo da aggiornare dopo la riduzione del nodo in esame
    				riga = j;
    			}
    			//somma tutti i valori della colonna
    			sum+= this.adjMatrix[j][i];
    		}
    		//controlla se la somma della colonna è uguale a 1 
    		//in questo caso esiste un solo arco entrante
    		if(sum == 1) {
    			//aggiorna i nodi che hanno un arco entrante dal nodo da ridurre
    			for(int z = 0; z < this.adjMatrix.length; z++) {
    				if(this.adjMatrix[i][z]==1) {
    					//aggiorna nodo da aggiornare
    					this.adjMatrix[riga][z]++;
    					this.adjList.get(riga).add(z);
    					this.adjMatrix[i][z]--;
    					this.adjList.get(i).remove(Integer.valueOf(z));
    				}
    			}
    			//elimina nodo in esame
    			this.adjMatrix[riga][i]--;
    			this.adjList.get(riga).remove(Integer.valueOf(i));
    			this.doParallelReduction();
    			return 1;
	    	}
    	}
    	return 0;
    }

}
