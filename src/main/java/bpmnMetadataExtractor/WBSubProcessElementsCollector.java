package bpmnMetadataExtractor;

import java.util.Collection;

import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.CatchEvent;
import org.camunda.bpm.model.bpmn.instance.Event;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.IntermediateCatchEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.SubProcess;
import org.camunda.bpm.model.bpmn.instance.ThrowEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class WBSubProcessElementsCollector {
	private String conversion;
	
	WBSubProcessElementsCollector(String c){
		this.conversion = c;
	}
	
	public void getEventsSubProcess(Collection<Event> events, BaseElement process){
		if(conversion.equals("WhiteBox"))
			for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
				for(Event event : sub.getChildElementsByType(Event.class))
					events.add(event);
				getEventsSubProcess(events, sub);
			}
	}
	
	public void getGatewaysSubProcess(Collection<Gateway> gateways, BaseElement process) {
		if(conversion.equals("WhiteBox"))
			for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
				for(Gateway gateway : sub.getChildElementsByType(Gateway.class))
					gateways.add(gateway);
				getGatewaysSubProcess(gateways, sub);
			}
		
	}
	
	
	//method used by getSequenceFlowsBetweenActivities() in BpmnBasicMetricsExtractor class
	public void getActivitiesSubProcess(Collection<Activity> activities, BaseElement process) {
		if(conversion.equals("WhiteBox"))
			for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
				for(Activity activity : sub.getChildElementsByType(Activity.class))
						activities.add(activity);
				getActivitiesSubProcess(activities, sub);
			}
	}
	
	public void getBoundaryEventsSubProcess(Collection<BoundaryEvent> be, BaseElement process) {
		if(conversion.equals("WhiteBox"))
			for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
				for(BoundaryEvent bound : sub.getChildElementsByType(BoundaryEvent.class))
					be.add(bound);
				getBoundaryEventsSubProcess(be, sub);
			}
		}
	
	public void getThrowEventsSubProcess(Collection<ThrowEvent> te, BaseElement process) {
		if(conversion.equals("WhiteBox"))
			for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
				for(ThrowEvent t : sub.getChildElementsByType(ThrowEvent.class))
					te.add(t);
				getThrowEventsSubProcess(te, sub);
			}
		}
	
	public void getCatchEventsSubProcess(Collection<CatchEvent> ce, BaseElement process) {
		if(conversion.equals("WhiteBox"))
			for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
				for(CatchEvent c : sub.getChildElementsByType(CatchEvent.class))
					ce.add(c);
				getCatchEventsSubProcess(ce, sub);
			}
		}
	
	public void getStartEventsSubProcess(Collection<StartEvent> se, BaseElement process) {
		if(conversion.equals("WhiteBox"))
			for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
				for(StartEvent s : sub.getChildElementsByType(StartEvent.class))
					se.add(s);
				getStartEventsSubProcess(se, sub);
			}
		}
	
	public void getInterEventsSubProcess(Collection<IntermediateCatchEvent> i, BaseElement process) {
		if(conversion.equals("WhiteBox"))
			for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
				for(IntermediateCatchEvent c : sub.getChildElementsByType(IntermediateCatchEvent.class))
					i.add(c);
				getInterEventsSubProcess(i, sub);
			}
		}
	
	//method used by getCollectionOfElementType(Class type) in BpmnBasicMetricsExtractor class
	//it recursively collects elements in subprocess
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getCollectionOfElementTypeWBProcess(Collection<ModelElementInstance> c, BaseElement process, Class type) {
		for(SubProcess sub : process.getChildElementsByType(SubProcess.class)) {
			c.addAll(sub.getChildElementsByType(type));
			this.getCollectionOfElementTypeWBProcess(c, sub, type);
		}		
	}

}
