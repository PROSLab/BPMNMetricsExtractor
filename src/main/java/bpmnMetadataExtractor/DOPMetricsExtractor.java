package bpmnMetadataExtractor;

import java.util.Vector;

import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.EventBasedGateway;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.SubProcess;
import org.camunda.bpm.model.bpmn.instance.Process;

public class DOPMetricsExtractor {
	private int dop;
	private Vector<String> activityId;
	private String conversion;
	
	public DOPMetricsExtractor(String s) {
		this.dop = 1;
		this.activityId = new Vector<String>();
		this.conversion = s;
	}
	
	public int getDop() {
		return this.dop;
	}
	
	/*
	 * Il metodo checkNonInterrupt controlla se l'attività "a" sia eseguita per mezzo di un boundary event non-interrupt
	 */
	
	private int checkNonInterrupt(Activity a, Vector<String> activityId) {
		int d = 0;
		for(String id: activityId) {
			if(a.getId().equals(id))
				d++;
		}
		return d;
	}
	
	//Il metodo checkSubProcess calcola il dop di un sotto-processo
	
	private int checkSubProcess(FlowNode subprocess, Vector<String> activityId,Vector<String> visit) {
		visit.add(subprocess.getId());
		if(subprocess.getIncoming().isEmpty() || subprocess.getOutgoing().isEmpty())
			return 1;
		if(subprocess.getChildElementsByType(StartEvent.class).size()>1)
			return 1;
		if(subprocess.getChildElementsByType(StartEvent.class).isEmpty() &&
				!(subprocess.getChildElementsByType(EndEvent.class).isEmpty()))
			return 1;
		if(!(subprocess.getChildElementsByType(StartEvent.class).isEmpty()) &&
				subprocess.getChildElementsByType(EndEvent.class).isEmpty())
			return 1;
		//raccolgo tutti i boundary event interni al sottoprocesso
		for(BoundaryEvent be : subprocess.getChildElementsByType(BoundaryEvent.class)) 
			if(!be.cancelActivity()) 
				this.activityId.add(be.getAttachedTo().getId());
		int tempDop = 0;
		//check per verificare se il sottoprocesso contiene solo attività
		if(subprocess.getChildElementsByType(SequenceFlow.class).isEmpty()) {
				tempDop = ((SubProcess) subprocess).getChildElementsByType(Activity.class).size();
			return tempDop;
		}
		//per ogni flownode del sottoprocesso
		for(FlowElement fe:((SubProcess) subprocess).getFlowElements())
			if(fe instanceof FlowNode) {
				int tempDop2 = 0;
				visit.add(fe.getId());
				tempDop2 = this.checkNode((FlowNode) fe, activityId, 0, visit);
				//il dop del sottoprocesso viene calcolato
				if(tempDop2 > tempDop)
					tempDop = tempDop2;
				}
		return tempDop;
	}
	
	//Il metodo checkNode aggiorna il dop del processo in base al tipo di nodo esaminato
	
	private int checkNode(FlowNode node, Vector<String> activityId, int dop, Vector<String> visit) {
		if(!(node instanceof ExclusiveGateway) && !(node instanceof EventBasedGateway)) {
			//controlla il tipo di ogni nodo collegato al nodo in esame
			for(SequenceFlow se : node.getOutgoing()) {
				//se è un attività, ma non un sottoprocesso, il degree of parallelism aumenta
				if(se.getTarget() instanceof Activity) {
					//se è un sottoprocesso
					if(se.getTarget() instanceof SubProcess && this.conversion.equals("WhiteBox")) 
						dop+= this.checkSubProcess(se.getTarget(), activityId, visit);
					 else { 
						 dop++;
						 //esegue check per eventuali boundary event non interrupting
						 dop+=this.checkNonInterrupt((Activity) se.getTarget(), activityId);}
					//se il flow node è un evento o un gateway parallelo/inclusive
				} else {
					visit.add(se.getTarget().getId());
					dop+=checkNode(se.getTarget(), activityId, 0, visit);}
			}
		} else {
			//se non è parallelo/inclusive, il degree of parallelism aumenta in base al flusso con maggior numero di attività simultanee
			int partialDop = 0;
			for(SequenceFlow seg: node.getOutgoing()) {
				int tempDop = 0;
				if(seg.getTarget() instanceof Activity) { 
					//se è un sottoprocesso
					if(seg.getTarget() instanceof SubProcess && this.conversion.equals("WhiteBox"))
						tempDop+=this.checkSubProcess(seg.getTarget(), activityId, visit);
					else {
						tempDop++;
						tempDop+=this.checkNonInterrupt((Activity) seg.getTarget(), activityId);
						}
				} else {
					tempDop += this.checkNode(seg.getTarget(), activityId, 0, visit);
					visit.add(seg.getTarget().getId());
				}
				if(tempDop > partialDop)
					partialDop = tempDop;
			}
			dop+=partialDop;
		}
		return dop;
	}
	
		
	public void setDop(Process process) {
		for(BoundaryEvent be : process.getChildElementsByType(BoundaryEvent.class)) {
			if(!be.cancelActivity()) {
				if(!(be.getAttachedTo() instanceof SubProcess) || this.conversion.equals("BlackBox"))
					//salva id dell'attività che ha attaccato un boundary event non interrupting
					this.activityId.add(be.getAttachedTo().getId());
				else {
					//se un attachedto è un sottoprocesso, aggiunge tutte gli id delle attività al suo interno nel vettore di stringhe
					for(FlowElement fe:((SubProcess) be.getAttachedTo()).getFlowElements())
						if(fe instanceof Activity)
							this.activityId.add(fe.getId());
				}
			}
		}
		//mettere un check per verificare se il nodo è stato già ispezionato
		Vector<String> visit = new Vector<String>();
		//controlla il numero di attività che possono essere eseguite simultaneamente per ogni nodo
		for(FlowNode node : process.getChildElementsByType(FlowNode.class)) {
			if(!visit.contains(node.getId())) {
				visit.add(node.getId());
				int tempDop = this.checkNode(node, activityId, 0, visit);
				if(tempDop > this.dop)
					this.dop = tempDop;
			}
		}	
	}

}

