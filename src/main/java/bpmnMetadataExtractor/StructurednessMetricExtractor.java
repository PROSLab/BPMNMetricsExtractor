package bpmnMetadataExtractor;

import java.util.Vector;

import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

/**
 * In a block-structured process model, each split element has a corresponding join element of the
 * same type, and split-join pairs are properly nested
 */

public class StructurednessMetricExtractor {
	private double S;
	private Process process;
	private int originalNode;
	private int reducedNodes;
	private Vector<String> blocks;
	
	public StructurednessMetricExtractor(Process p){
		this.process =p;
		this.originalNode = p.getChildElementsByType(FlowNode.class).size();
		this.reducedNodes = originalNode;
		this.blocks = new Vector<String>();
	}
	
	public double getS() {
		return this.S;
	}
	
	public void setS() {
		for(Gateway split : this.process.getChildElementsByType(Gateway.class)) {
			this.reduceGraph(split);
		}
		//System.out.println(reducedNodes);
		this.S = 1.0 - (((double)reducedNodes) / ((double) originalNode));	
	}
	
	/*
	 * studia gli archi uscenti dallo split e ricerca un join
	 */
	private int search(FlowNode fn, Vector<String> visitedGateways) {
		int cont = 0;
		
		//TODO far ripartire la ricerca se è uno split
		if (fn instanceof Gateway && fn.getIncoming().size() == 1) {
			this.reduceGraph((Gateway) fn);
			}
		else
			if (fn instanceof Gateway) {
				if(fn.getOutgoing().size() != 1)
					visitedGateways.add("NoJoin");
				else 
					if(!this.blocks.contains(fn.getId()))
						visitedGateways.add(fn.getId());
				return cont;
				}
		
		if (fn instanceof EndEvent) {
			visitedGateways.add(fn.getId());
			return cont;
		} 
		else 
			for(SequenceFlow sf : fn.getOutgoing()) {
				cont++;
				cont+=search(sf.getTarget(), visitedGateways);
				}
		return cont;
	}
	
	/*
	 * studia ogni gateway del grafo, elimina dal totale il numero di nodi che formano una struttura
	 */
	private void reduceGraph(Gateway split) {
		if(split.getIncoming().size() == 1) {
			
			int reduce = 0;
			Vector<String> visitedGateways = new Vector<String>();
			
			String type = split.getElementType().getTypeName();
			//System.out.println(type);
			for(SequenceFlow sf : split.getOutgoing())
				reduce+= this.search(sf.getTarget(), visitedGateways);
			
			boolean check = true;
			for(String id : visitedGateways) {
				//System.out.println(visitedGateways.get(0)+"   "+id);
				if(!visitedGateways.get(0).equals(id))
					check = false;
			}
				
			//System.out.println(this.process.getModelInstance().getModelElementById(visitedGateways.get(0)).getElementType().getTypeName());
			if(check && type.equals(this.process.getModelInstance().getModelElementById(visitedGateways.get(0)).getElementType().getTypeName())) {
				reduce++;
				this.reducedNodes = this.reducedNodes - reduce;
				this.blocks.add(split.getId()/*.concat(visitedGateways.get(0))*/);
				//System.out.println(split.getId().concat(visitedGateways.get(0)));
				}
			//System.out.println(reduce);
			} 
	}

}
