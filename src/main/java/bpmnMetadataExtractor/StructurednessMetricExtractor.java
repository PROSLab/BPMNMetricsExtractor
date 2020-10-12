package bpmnMetadataExtractor;

import java.util.Vector;

import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

public class StructurednessMetricExtractor {
	private double S;
	
	public StructurednessMetricExtractor(){

	}
	
	public double getS() {
		return this.S;
	}
	
	public void setS(Process p) {
		int originalNode = p.getChildElementsByType(FlowNode.class).size();
		int r = originalNode;
		int reduce = 0;
		for(Gateway split : p.getChildElementsByType(Gateway.class)) {
			if(split.getIncoming().size() == 1) {
				Vector<String> visitedGateways = new Vector<String>();
				String type = split.getElementType().getTypeName();
				for(SequenceFlow sf : split.getOutgoing())
					reduce+= this.search(sf.getTarget(), visitedGateways);
				boolean check = true;
				for(String id : visitedGateways) {
					if(!visitedGateways.get(0).equals(id))
						check = false;
					}
				if(check && type.equals(p.getModelInstance().getModelElementById(visitedGateways.get(0)).getElementType().getTypeName())) {
					reduce++;
					r = r - reduce;
					}
				}
			reduce = 0;
			}
		this.S = 1.0 - (((double)r) / ((double) originalNode));	
	}
	
	private int search(FlowNode fn, Vector<String> visitedGateways) {
		int cont = 0;
		if (fn instanceof Gateway /*&& fn.getOutgoing().size() == 1*/) {
			visitedGateways.add(fn.getId());
			if(fn.getOutgoing().size() != 1)
				visitedGateways.add("NoJoin");
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

}
