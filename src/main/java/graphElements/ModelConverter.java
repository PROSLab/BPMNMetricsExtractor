package graphElements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.SubProcess;
import org.camunda.bpm.model.bpmn.instance.ThrowEvent;

public class ModelConverter {
	private BpmnModelInstance modelInstance;
	private Vector<String> notification;
	
	public ModelConverter(BpmnModelInstance model) {
		this.modelInstance = model;
		this.notification = new Vector<String>();
	}
	
	public Collection<Process> getProcesses() {
		return this.modelInstance.getModelElementsByType(Process.class);
	}
	
	public Vector<String> getNotification() {
		return this.notification;
	}
	
	private void whiteboxSubprocessConversion(Vector<Edge> edges,BaseElement process, Collection<FlowNode> fn, Collection<SequenceFlow> sf) { 
		//raccoglie tutti i sottoprocessi del processo/sotto-processo in esame
		Collection<SubProcess> subprocesses = process.getChildElementsByType(SubProcess.class); 
		for(SubProcess subprocess : subprocesses) {
			//check per verificare che il sotto-processo sia connesso
			if(subprocess.getIncoming().isEmpty() || subprocess.getOutgoing().isEmpty())
				this.notification.add("Incomplete subprocess in "+process.getId()+": "+subprocess.getId());
			//check per verificare che il sotto-processo contenga al massimo uno start event
			else if(subprocess.getChildElementsByType(StartEvent.class).size()>1)
				this.notification.add("Invalid number of start events in "+subprocess.getId());
			else {
				//raccoglie tutti gli elementi all'interno del sotto-processo e li salva
				Collection<FlowElement> flowElements = subprocess.getFlowElements();
        		for(FlowElement fe : flowElements) {
        			if(fe instanceof FlowNode)
        				fn.add((FlowNode) fe);
       				else if(fe instanceof SequenceFlow)
       					sf.add((SequenceFlow) fe);
       			}
        		//cancella ogni boundary event e il source dei suoi outgoing diventa id dell'attivita al quale attaccato
        		this.resolveBoundaryEvent(subprocess, fn);
        		//chiamata ricorsiva del metodo per ogni sotto-processo all'interno sotto-processo in esame
        		this.whiteboxSubprocessConversion(edges, subprocess, fn, sf);
        		//check per verificare l'assenza di start event ed end event, allora sotto-processo contiene solo attivita
        		if(subprocess.getChildElementsByType(StartEvent.class).isEmpty() &&
        				subprocess.getChildElementsByType(EndEvent.class).isEmpty()) {
        			//check per verificare che il sotto-processo contiene almeno un'attivita
        			if(!subprocess.getChildElementsByType(Activity.class).isEmpty()) {
        				for(SequenceFlow in: subprocess.getIncoming()) {
        					for(FlowElement fe : flowElements)
        						//check per verificare presenza di elementi indisiderati, in tal caso sono eliminati
        						if(!(fe instanceof Activity)) {
       								if(fe instanceof FlowNode)
       	            					fn.remove((FlowNode) fe);
       	            				else if(fe instanceof SequenceFlow)
       	            					sf.remove((SequenceFlow) fe);
       								this.notification.add("Unexpected element in "+subprocess.getId()+" :"+fe.getId());
        						} 
        					    //creo arco con questo target e stesso source
        						else edges.add(new Edge(in.getSource().getId(), fe.getId()));
        					//rimuovo sequence entrante dalla collezione dei sequence flow
        					sf.remove(in);
        				}
        				for(SequenceFlow out: subprocess.getOutgoing()) {
        					for(FlowElement fe : flowElements)
        						if(fe instanceof Activity)
       							//creo arco con questo source e stesso target
       							edges.add(new Edge(fe.getId(), out.getTarget().getId() ));
        					//rimuovo sequence uscente dalla collezione dei sequence flow
       					    sf.remove(out);
       					}
       					//rimuovo sottoprocesso dalla collezione di nodi
        	       		fn.remove(subprocess);
        	       	}
        		} 
        		//check per verificare la presenza di almeno uno start e un end event
        		else if((!subprocess.getChildElementsByType(StartEvent.class).isEmpty()) &&
        				(!subprocess.getChildElementsByType(EndEvent.class).isEmpty())) {
        			for(SequenceFlow in: subprocess.getIncoming()) {
        				for(StartEvent se : subprocess.getChildElementsByType(StartEvent.class))
        					//creo arco con questo target e stesso source
        					edges.add(new Edge(in.getSource().getId(), se.getId()));
       					sf.remove(in); 
       				}
       				for(SequenceFlow out: subprocess.getOutgoing()) {
       					for(EndEvent ee : subprocess.getChildElementsByType(EndEvent.class))
       						//creo arco con questo target e stesso source
       						edges.add(new Edge(ee.getId(), out.getTarget().getId()));
       					sf.remove(out);
       	    		}
        			//check per verificare la presenza di eventuali nodi disconessi, in tal caso li elimina 
        			for(FlowNode fnSub: subprocess.getChildElementsByType(FlowNode.class))
        				if(fnSub.getIncoming().isEmpty() && fnSub.getOutgoing().isEmpty()) {
        					fn.remove(fnSub);
        					this.notification.add("Disconnected node in "+subprocess.getId()+": "+fnSub.getId());
       					}
       				//rimuovo sottoprocesso dalla collezione di nodi
       				fn.remove(subprocess);
        		} 
        		//nel caso il sotto-processo sia privo di start event o di end event, il sotto-processo viene convertito black-box
        		else {
        			for(FlowElement fe : subprocess.getFlowElements()) {
            			if(fe instanceof FlowNode)
            				fn.remove((FlowNode) fe);
            			else if(fe instanceof SequenceFlow)
            				sf.remove((SequenceFlow) fe);
           			}
        			this.notification.add("Start/End event missing in "+subprocess.getId());}
        		}
        	}
    }
	
	/*
	 * Il metodo resolveBoundaryEvent elimina il boundary event dopo aver connesso l'attivita al quale attaccato
	 * con i sequence flow che da esso si diramano
	 */
	
	private void resolveBoundaryEvent(BaseElement process, Collection<FlowNode> flowNodes) {
		//rende ogni boundary event parte dello stesso sequence flow al quale attaccato 
		for(BoundaryEvent be : process.getChildElementsByType(BoundaryEvent.class)) {
        	for(SequenceFlow sfbe: be.getOutgoing()) {
        		sfbe.setSource(be.getAttachedTo());
        		//aggiunge sequence flow nell'outgoing del nuovo source, utile in caso di sottoprocesso
        		be.getAttachedTo().getOutgoing().add(sfbe);
       		}
        	if(be.getName()== null && be.getAttachedTo() instanceof SubProcess)
        		this.notification.add("Nameless catch event attached to subprocess "+be.getAttachedTo().getId()+": "+be.getId());
        	if(be.getName()!= null && be.getAttachedTo() instanceof SubProcess)
        		//crea collegamento tra throw e catch event specifici in base al loro nome
        		for(ThrowEvent te: be.getAttachedTo().getChildElementsByType(ThrowEvent.class)) {
        			if(be.getName().equals(te.getName()))
        				for(SequenceFlow sfbe: be.getOutgoing()) {
        					sfbe.setSource(te);
        					this.notification.add("Link created. Catch event: "+be.getId()+" - Throw event: "+te.getId());
        					//elimino sequence aggiunto precedentemente alla collezione di sequence uscenti del sottoprocesso in esame
        					be.getAttachedTo().getOutgoing().remove(sfbe);
        				}
        		}
        	flowNodes.remove(be);
        }
	}
	
	public GraphMatrixes convertModel(Process process, String opzione) {
		if(process.getChildElementsByType(StartEvent.class).isEmpty() 
				|| process.getChildElementsByType(EndEvent.class).isEmpty()
				|| process.getChildElementsByType(SequenceFlow.class).isEmpty()) {
			this.notification.add("Incomplete process: "+process.getId());
			return null;
		}
		Vector<Node> nodes = new Vector<Node>();
        Vector<Edge> edges = new Vector<Edge>();
		//colleziona tutti i flownode e i sequenceflow del processo
		Collection<FlowNode> flowNodes = process.getChildElementsByType(FlowNode.class);
		Collection<SequenceFlow> sequenceFlows = process.getChildElementsByType(SequenceFlow.class);
		//elimina tutti i flow node disconnessi
		Iterator<FlowNode> i = flowNodes.iterator();
		while(i.hasNext()) {
			FlowNode fn = i.next();
			if(fn.getIncoming().isEmpty() && fn.getOutgoing().isEmpty()) {
				i.remove();
				this.notification.add("Disconnected node in "+process.getId()+": "+fn.getId());
			}	
		}
        //cancella ogni boundary event e il source dei suoi outgoing diventa l'id dell'attivita al quale attaccato
        this.resolveBoundaryEvent(process,flowNodes);
        //controlla opzione per la conversione scelta
        if(opzione.equals("WhiteBox"))
        	this.whiteboxSubprocessConversion(edges, process, flowNodes, sequenceFlows);
        //crea un nuovo nodo per il grafo per ogni flownode del processo
        int vertexNumber = 0;
        for(FlowNode fn : flowNodes) {
        	nodes.add(new Node(fn.getId(),vertexNumber));
        	vertexNumber++;
        }
        //crea un nuovo arco per il grafo per ogni sequenceflow del processo
        for(SequenceFlow sf : sequenceFlows)
        	edges.add(new Edge(sf.getSource().getId(), sf.getTarget().getId()));
        //crea la matrice di adiacenza convertita dal modello
        GraphMatrixes gm = new GraphMatrixes(edges,nodes);
        return gm;
	}
	
	public GraphMatrixes convertEntireModel(String opzione) {
		Vector<Node> nodes = new Vector<Node>();
        Vector<Edge> edges = new Vector<Edge>();
		//colleziona tutti i flownode e i sequenceflow del processo
		Collection<FlowNode> flowNodes = new ArrayList<FlowNode>();
		Collection<SequenceFlow> sequenceFlows = new ArrayList<SequenceFlow>();
		for(Process p : this.modelInstance.getModelElementsByType(Process.class)) {
			if(p.getChildElementsByType(StartEvent.class).isEmpty() || p.getChildElementsByType(EndEvent.class).isEmpty() || p.getChildElementsByType(SequenceFlow.class).isEmpty()) {
				this.notification.add("Incomplete process: "+p.getId());
				return null;
			}
			Collection<FlowNode> flowNodesProcess = p.getChildElementsByType(FlowNode.class);
			//elimina tutti i flow node disconnessi
			Iterator<FlowNode> i = flowNodesProcess.iterator();
			while(i.hasNext()) {
				FlowNode fn = i.next();
				if(fn.getIncoming().isEmpty() && fn.getOutgoing().isEmpty()) {
					i.remove();
					this.notification.add("Disconnected node in "+p.getId()+": "+fn.getId());
				}	
			}
			flowNodes.addAll(flowNodesProcess);
			Collection<SequenceFlow> sequenceFlowsProcess = p.getChildElementsByType(SequenceFlow.class);
			sequenceFlows.addAll(sequenceFlowsProcess);
			//cancella ogni boundary event e il source dei suoi outgoing diventa id dell'attivita al quale attaccato
	        this.resolveBoundaryEvent(p, flowNodes);
	        //controlla l'opzione per la conversione scelta 
	        if(opzione.equals("WhiteBox"))
	        	this.whiteboxSubprocessConversion(edges, p, flowNodes, sequenceFlows);
		}
        //crea un nuovo nodo per il grafo per ogni flownode del processo
        int vertexNumber = 0;
        for(FlowNode fn : flowNodes) {
        	nodes.add(new Node(fn.getId(),vertexNumber));
        	vertexNumber++;
        }
        //crea un nuovo arco per il grafo per ogni sequenceflow del processo
        for(SequenceFlow sf : sequenceFlows)
        	edges.add(new Edge(sf.getSource().getId(), sf.getTarget().getId()));
        for(MessageFlow mf : this.modelInstance.getModelElementsByType(MessageFlow.class)) {
        	//se il messaggio collega due nodi di flusso viene creato un nuovo arco,
        	//altrimenti un messaggio non viene convertito in grafo
        	if(mf.getTarget() instanceof FlowNode && mf.getSource() instanceof FlowNode) {
        		//check per gestire i boundary events
        		if(mf.getTarget() instanceof BoundaryEvent)
        			edges.add(new Edge(mf.getSource().getId(), ((BoundaryEvent) mf.getTarget()).getAttachedTo().getId()));
        		else edges.add(new Edge(mf.getSource().getId(), mf.getTarget().getId()));
        		if(mf.getTarget() instanceof SubProcess || mf.getSource() instanceof SubProcess) {
        			//la conversione white box cancella il nodo al quale un eventuale messaggio viene collegato
        			//problema per sottoprocessi che non sono collapsed segnalato in notifiche
        			if(opzione.equals("WhiteBox")) {
        				if(!(mf.getSource().getChildElementsByType(FlowElement.class).isEmpty()) ||
        						!(mf.getTarget().getChildElementsByType(FlowElement.class).isEmpty())) {
        					this.notification.add("Message flow cannot be white-box converted: "+mf.getId());
        					return null;
        				}
        			}
        		}
        	} else {
        		this.notification.add("Message flow cannot be converted: "+mf.getId());
        		return null;
        	}
        }
        //crea la matrice di adiacenza convertita dal modello
        GraphMatrixes gm = new GraphMatrixes(edges,nodes);
        return gm;
	}	

}

