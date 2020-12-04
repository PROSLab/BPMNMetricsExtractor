package bpmnMetadataExtractor;

import java.util.Collection;
import java.util.Vector;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

/**
 * 
 * Classe in cui andare a inserire i metodi per l'estrazione delle
 * statistiche/metadati
 * 
 * @author PROSLabTeam
 *
 */
public class BpmnBasicMetricsExtractor {

	// modello da cui estrarre le metriche
	private BpmnModelInstance modelInstance;
	private JsonEncoder json;
	//added for multi-processess model
	private Process process;
	private int numberProcess;
	private String extraction;
	private String conversion;
	private PEUtility ec;

	public BpmnBasicMetricsExtractor(BpmnModelInstance modelInstance, Process process, JsonEncoder jsonEncoder, int i, String e, String c) {
		this.modelInstance = modelInstance;
		this.process = process;
		this.json = jsonEncoder;
		this.numberProcess = i;
		this.extraction = e;
		this.conversion = c;
		this.ec = new PEUtility(c);
	}
	
	public BpmnBasicMetricsExtractor(BpmnModelInstance modelInstance, JsonEncoder jsonEncoder, int i, String e) {
		this.modelInstance = modelInstance;
		this.json = jsonEncoder;
		this.numberProcess = i;
		this.extraction = e;
	}
	
	public String getExtractionType() {
		return this.extraction;
	}

	/**
	 * Metodo principale per runnare tutti i metodi che ottengono le metriche
	 */
	public void runMetrics() {
		this.json.addBasicMetric("NT", this.getTasks());
		this.json.addBasicMetric("NCD", this.getComplexDecisions());
		this.json.addBasicMetric("NDOin", this.getDataObjectsInput());
		this.json.addBasicMetric("NDOout", this.getDataObjectsOutput());
		this.json.addBasicMetric("NID", this.getInclusiveDecisions());
		this.json.addBasicMetric("NEDDB", this.getExclusiveDataBasedDecisions());
		this.json.addBasicMetric("NEDEB", this.getExclusiveEventBasedDecisions());
		this.json.addBasicMetric("NL", this.getLanes());
		this.json.addBasicMetric("NMF", this.getMessageFlows());
		this.json.addBasicMetric("NP", this.getPools());
		this.json.addBasicMetric("NEP", this.getEmptyPools());
		this.json.addBasicMetric("NSFE", this.getSequenceFlowsFromEvents());
		this.json.addBasicMetric("NSFG", this.getSequenceFlowsFromGateways());
		this.json.addBasicMetric("NSFA", this.getSequenceFlowsBetweenActivities());
		this.json.addBasicMetric("NAC", this.getActivationConditions());
		this.json.addBasicMetric("NACT", this.getActivities());
		this.json.addBasicMetric("NART", this.getArtifacts());
		this.json.addBasicMetric("NASI", this.getAssignments());
		this.json.addBasicMetric("NASO", this.getAssociations());
		this.json.addBasicMetric("NAUD", this.getAuditing());
		this.json.addBasicMetric("NBEL", this.getBaseElements());
		this.json.addBasicMetric("NBEV", this.getBoundaryEvents());
		this.json.addBasicMetric("NBRT", this.getBusinessRuleTasks());
		this.json.addBasicMetric("NCEL", this.getCallableElements());
		this.json.addBasicMetric("NCAC", this.getCallActivities());
		//this.json.addBasicMetric("NCCO", this.getCallConversations());
		this.json.addBasicMetric("NCANEV", this.getCancelEvents());
		this.json.addBasicMetric("NCATEV", this.getCatchEvents());
		this.json.addBasicMetric("NCVAL", this.getCategoryValues());
		this.json.addBasicMetric("NCOL", this.getCollaborations());
		this.json.addBasicMetric("NCOMEV", this.getCompensateEvents());
		this.json.addBasicMetric("NCOCON", this.getCompletionConditions());
		this.json.addBasicMetric("NCBDEF", this.getComplexBehaviorDefinitions());
		//this.json.addBasicMetric("NCOND", this.getConditions());
		this.json.addBasicMetric("NCONDEV", this.getConditionalEvent());
		this.json.addBasicMetric("NCONDEX", this.getConditionExpressions());
		//this.json.addBasicMetric("NCONV", this.getConversations());
		//this.json.addBasicMetric("NCONVAS", this.getConversationAssociations());
		//this.json.addBasicMetric("NCONVL", this.getConversationLinks());
		//this.json.addBasicMetric("NCONVN", this.getConversationNodes());
		//this.json.addBasicMetric("NCORK", this.getCorrelationKeys());
		//this.json.addBasicMetric("NCORP", this.getCorrelationProperties());
		//this.json.addBasicMetric("NCORPB", this.getCorrelationPropertyBindings());
		//this.json.addBasicMetric("NCORPRE", this.getCorrelationPropertyRetrievalExpressions());
		//this.json.addBasicMetric("NCORS", this.getCorrelationSubscriptions());
		this.json.addBasicMetric("NDA", this.getDataAssociations());
		this.json.addBasicMetric("NDInA", this.getDataInputAssociations());
		this.json.addBasicMetric("NDO", this.getDataObjects());
		this.json.addBasicMetric("NDOR", this.getDataObjectReferences());
		this.json.addBasicMetric("NDOutA", this.getDataOutputAssociations());
		this.json.addBasicMetric("NDSTA", this.getDataStates());
		this.json.addBasicMetric("NDSTO", this.getDataStores());
		this.json.addBasicMetric("NDEF", this.getDefinitions());
		this.json.addBasicMetric("NDOC", this.getDocumentations());
		this.json.addBasicMetric("NENDEV", this.getEndEvents());
		this.json.addBasicMetric("NENDP", this.getEndPoints());
		//this.json.addBasicMetric("NERR", this.getErrors());
		this.json.addBasicMetric("NERREV", this.getErrorEvents());
		//this.json.addBasicMetric("NESC", this.getEscalations());
		this.json.addBasicMetric("NESCEV", this.getEscalationEvents());
		this.json.addBasicMetric("NEV", this.getEvents());
		this.json.addBasicMetric("NEVDEF", this.getEventDefinitions());
		this.json.addBasicMetric("NEXP", this.getExpressions());
		this.json.addBasicMetric("NEXT", this.getExtensions());
		this.json.addBasicMetric("NEXTEL", this.getExtensionElements());
		this.json.addBasicMetric("NFLEL", this.getFlowElements());
		this.json.addBasicMetric("NFLNO", this.getFlowNodes());
		this.json.addBasicMetric("NFOREXP", this.getFormalExpressions());
		this.json.addBasicMetric("NGA", this.getGateways());
		//this.json.addBasicMetric("NGC", this.getGlobalConversations());
		//this.json.addBasicMetric("NHP", this.getHumanPerformers());
		this.json.addBasicMetric("NIMP", this.getImports());
		this.json.addBasicMetric("NInDI", this.getInputDataItems());
		this.json.addBasicMetric("NInS", this.getInputSets());
		this.json.addBasicMetric("NINTNO", this.getInteractionNodes());
		this.json.addBasicMetric("NINTE", this.getInterfaces());
		this.json.addBasicMetric("NICEV", this.getIntermediateCatchEvents());
		this.json.addBasicMetric("NITEV", this.getIntermediateThrowEvents());
		this.json.addBasicMetric("NIOB", this.getIoBindings());
		this.json.addBasicMetric("NIOS", this.getIoSpecifications());
		this.json.addBasicMetric("NIAEL", this.getItemAwareElements());
		//this.json.addBasicMetric("NIDEF", this.getItemDefinitions());
		this.json.addBasicMetric("NLEV", this.getLinkEvents());
		this.json.addBasicMetric("NLOOPCA", this.getLoopCardinalities());
		this.json.addBasicMetric("NLOOPCH", this.getLoopCharacteristics());
		this.json.addBasicMetric("NMT", this.getManualTasks());
		//this.json.addBasicMetric("NMES", this.getMessages());
		this.json.addBasicMetric("NMESEV", this.getMessageEvents());
		this.json.addBasicMetric("NMESFA", this.getMessageFlowAssociations());
		this.json.addBasicMetric("NMON", this.getMonitorings());
		this.json.addBasicMetric("NMILCH", this.getMultiInstanceLoopCharacteristics());
		this.json.addBasicMetric("NOP", this.getOperations());
		this.json.addBasicMetric("NOutDI", this.getOutputDataItems());
		this.json.addBasicMetric("NOutS", this.getOutputSets());
		this.json.addBasicMetric("NPG", this.getParallelGateways());
		this.json.addBasicMetric("NPAS", this.getParticipantAssociations());
		this.json.addBasicMetric("NPM", this.getParticipantMultiplicities());
		//this.json.addBasicMetric("NPER", this.getPerformers());
		//this.json.addBasicMetric("NPO", this.getPotentialOwners());
		this.json.addBasicMetric("NPROC", this.getProcesses());
		this.json.addBasicMetric("NPROP", this.getProperties());
		this.json.addBasicMetric("NRT", this.getReceiveTasks());
		this.json.addBasicMetric("NREL", this.getRelationships());
		this.json.addBasicMetric("NREN", this.getRenderings());
		this.json.addBasicMetric("NRES", this.getResources());
		this.json.addBasicMetric("NRESAEX", this.getResourceAssignmentExpressions());
		//this.json.addBasicMetric("NRESP", this.getResourceParameters());
		this.json.addBasicMetric("NRESPB", this.getResourceParameterBindings());
		this.json.addBasicMetric("NRESR", this.getResourceRoles());
		this.json.addBasicMetric("NRE", this.getRootElements());
		//this.json.addBasicMetric("NSCR", this.getScripts());
		this.json.addBasicMetric("NSCT", this.getScriptTasks());
		this.json.addBasicMetric("NSENT", this.getSendTasks());
		this.json.addBasicMetric("NSEQF", this.getSequenceFlows());
		this.json.addBasicMetric("NSERT", this.getServiceTasks());
		//this.json.addBasicMetric("NSI", this.getSignals());
		this.json.addBasicMetric("NSIEV", this.getSignalEvent());
		this.json.addBasicMetric("NSTEV", this.getStartEvents());
		//this.json.addBasicMetric("NSCONV", this.getSubConversations());
		this.json.addBasicMetric("NTEX", this.getTexts());
		this.json.addBasicMetric("NTEXA", this.getTextAnnotations());
		this.json.addBasicMetric("NTHEV", this.getThrowEvents());
		this.json.addBasicMetric("NTC", this.getTimeCycles());
		this.json.addBasicMetric("NTDA", this.getTimeDates());
		this.json.addBasicMetric("NTDU", this.getTimeDurations());
		this.json.addBasicMetric("NTEV", this.getTimerEventDefinitions());
		this.json.addBasicMetric("NTR", this.getTransactions());
		this.json.addBasicMetric("NUT", this.getUserTasks());
		this.json.addBasicMetric("NSUB", this.getSubprocesses());
		this.json.addBasicMetric("NCSUB", this.getCollapsedSubprocesses());
		this.json.addBasicMetric("NFDCG", this.getFlowDividingComplexGateways());
		this.json.addBasicMetric("NFDEBG", this.getFlowDividingEventBasedGateways());
		this.json.addBasicMetric("NFDEXG", this.getFlowDividingExclusiveGateways());
		this.json.addBasicMetric("NFDIG", this.getFlowDividingInclusiveGateways());
		this.json.addBasicMetric("NFDPG", this.getFlowDividingParallelGateways());
		this.json.addBasicMetric("NFDG", this.getFlowDividingGateways());
		this.json.addBasicMetric("NFDT", this.getFlowDividingTasks());
		this.json.addBasicMetric("NFJCG", this.getFlowJoiningComplexGateways());
		this.json.addBasicMetric("NFJEBG", this.getFlowJoiningEventBasedGateways());
		this.json.addBasicMetric("NFJEXG", this.getFlowJoiningExclusiveGateways());
		this.json.addBasicMetric("NFJIG", this.getFlowJoiningInclusiveGateways());
		this.json.addBasicMetric("NFJPG", this.getFlowJoiningParallelGateways());
		this.json.addBasicMetric("NFJG", this.getFlowJoiningGateways());
		this.json.addBasicMetric("NFJT", this.getFlowJoiningTasks());
		this.json.addBasicMetric("NFJDCG", this.getFlowJoiningAndDividingComplexGateways());
		this.json.addBasicMetric("NFJDEBG", this.getFlowJoiningAndDividingEventBasedGateways());
		this.json.addBasicMetric("NFJDEXG", this.getFlowJoiningAndDividingExclusiveGateways());
		this.json.addBasicMetric("NFJDIG", this.getFlowJoiningAndDividingInclusiveGateways());
		this.json.addBasicMetric("NFJDPG", this.getFlowJoiningAndDividingParallelGateways());
		this.json.addBasicMetric("NFJDG", this.getFlowJoiningAndDividingGateways());
		this.json.addBasicMetric("NFJDT", this.getFlowJoiningAndDividingTasks());
		this.json.addBasicMetric("NSTMEV", this.getStartMessageEvents());
		this.json.addBasicMetric("NSTCOEV", this.getStartConditionalEvents());
		this.json.addBasicMetric("NSTSIGEV", this.getStartSignalEvents());
		this.json.addBasicMetric("NSTTEV", this.getStartTimerEvents());
		this.json.addBasicMetric("NSTMUEV", this.getStartMultipleEvents());
		this.json.addBasicMetric("NSTPMUEV", this.getStartParallelMultipleEvents());
		this.json.addBasicMetric("NENDCEV", this.getEndCancelEvents());
		this.json.addBasicMetric("NENDCOMEV", this.getEndCompensationEvents());
		this.json.addBasicMetric("NENDCOMEV", this.getCompensateEvents());
		this.json.addBasicMetric("NENDERREV", this.getEndErrorEvents());
		this.json.addBasicMetric("NENDESCEV", this.getEndEscalationEvents());
		this.json.addBasicMetric("NENDMEV", this.getEndMessageEvents());
		this.json.addBasicMetric("NENDSIGEV", this.getEndSignalEvents());
		this.json.addBasicMetric("NTEEV", this.getEndTerminateEvents());
		this.json.addBasicMetric("NENDMUEV", this.getEndMultipleEvents());
		this.json.addBasicMetric("NIBEV", this.getInterruptingBoundaryEvents());
		this.json.addBasicMetric("NIBCANCEV", this.getInterruptingBoundaryCancelEvents());
		this.json.addBasicMetric("NIBCOMPEV", this.getInterruptingBoundaryCompensationEvents());
		this.json.addBasicMetric("NIBCOEV", this.getInterruptingBoundaryConditionalEvents());
		this.json.addBasicMetric("NIBERREV", this.getInterruptingBoundaryErrorEvents());
		this.json.addBasicMetric("NIBESCEV", this.getInterruptingBoundaryEscalationEvents());
		this.json.addBasicMetric("NIBMEV", this.getInterruptingBoundaryMessageEvents());
		this.json.addBasicMetric("NIBSIGEV", this.getInterruptingBoundarySignalEvents());
		this.json.addBasicMetric("NIBTEV", this.getInterruptingBoundaryTimerEvents());
		this.json.addBasicMetric("NIESCTEV", this.getIntermediateEscalationThrowEvents());
		this.json.addBasicMetric("NICOMTEV", this.getIntermediateCompensationThrowEvents());
		this.json.addBasicMetric("NILTEV", this.getIntermediateLinkThrowEvents());
		this.json.addBasicMetric("NIMTEV", this.getIntermediateMessageThrowEvents());
		this.json.addBasicMetric("NISIGTEV", this.getIntermediateSignalThrowEvents());
		this.json.addBasicMetric("NIMUTEV",this.getIntermediateMultipleThrowEvents());
		this.json.addBasicMetric("NICONCEV", this.getIntermediateConditionalCatchEvents());
		this.json.addBasicMetric("NILCEV", this.getIntermediateLinkCatchEvents());
		this.json.addBasicMetric("NIMCEV", this.getIntermediateMessageCatchEvents());
		this.json.addBasicMetric("NISIGCEV", this.getIntermediateSignalCatchEvents());
		this.json.addBasicMetric("NIMUCEV", this.getIntermediateMultipleCatchEvents());
		this.json.addBasicMetric("NIPMUCEV", this.getIntermediateParallelMultipleCatchEvents());
		this.json.addBasicMetric("NBNIEV", this.getNonInterruptingBoundaryEvents());
		this.json.addBasicMetric("NBNIMEV", this.getNonInterruptingBoundaryMessageEvents());
		this.json.addBasicMetric("NBNITEV", this.getNonInterruptingBoundaryTimerEvents());
		this.json.addBasicMetric("NBNICONEV", this.getNonInterruptingBoundaryConditionalEvents());
		this.json.addBasicMetric("NBNISIGEV", this.getNonInterruptingBoundarySignalEvents());
		this.json.addBasicMetric("NBNIMUEV", this.getNonInterruptingBoundaryMultipleEvents());
		this.json.addBasicMetric("NBIMUEV", this.getInterruptingBoundaryMultipleEvents());
		this.json.addBasicMetric("NBNIESCEV", this.getNonInterruptingBoundaryEscalationEvents());
		this.json.addBasicMetric("NBNIPMUEV", this.getNonInterruptingBoundaryParallelMultipleEvents());
		this.json.addBasicMetric("NBIPMUEV", this.getInterruptingBoundaryParallelMultipleEvents());
		this.json.addBasicMetric("TNIE", getTotalNumberOfIntermediateEvents());
		this.json.addBasicMetric("NG", getGroups());
		this.json.addBasicMetric("NSL", getSwimlanes());
		this.json.addBasicMetric("NITMREV", getIntermediateTimerEvents());
		this.json.addBasicMetric("NESUB", getEventSubprocesses());
		this.json.addBasicMetric("NCESUB", getCollapsedEventSubprocesses());
		this.json.addBasicMetric("NCP", getCollapsedPools());
		this.json.addBasicMetric("NSTESEV", getStartEscalationEvents());
		this.json.addBasicMetric("NSTEREV", getStartErrorEvents());
		this.json.addBasicMetric("NSTCOMEV", getStartCompensationEvents());
		this.json.addBasicMetric("NSTNNEV", getStartNoneEvents());
		this.json.addBasicMetric("NINNEV", getIntermediateNoneEvents());
		this.json.addBasicMetric("NENNNEV", getEndNoneEvents());
		this.json.addBasicMetric("NISCE", this.getNonInterruptingStartConditionalEvents());
		this.json.addBasicMetric("NISEE", this.getNonInterruptingStartEscalationEvents());
		this.json.addBasicMetric("NISME", this.getNonInterruptingStartMessageEvents());
		this.json.addBasicMetric("NISSE", this.getNonInterruptingStartSignalEvents());
		this.json.addBasicMetric("NISMUE", this.getNonInterruptingStartMultipleEvents());
		this.json.addBasicMetric("NISMUPE", this.getNonInterruptingStartMultipleParallelEvents());
		this.json.addBasicMetric("NISTE", this.getNonInterruptingStartTimerEvents());
	}
	
	public void runMetricsProcess() {
		this.json.addBasicMetric("NT", this.getTasks(), this.numberProcess);
		this.json.addBasicMetric("NCD", this.getComplexDecisions(), this.numberProcess);
		this.json.addBasicMetric("NDOin", this.getDataObjectsInput(), this.numberProcess);
		this.json.addBasicMetric("NDOout", this.getDataObjectsOutput(), this.numberProcess);
		this.json.addBasicMetric("NID", this.getInclusiveDecisions(), this.numberProcess);
		this.json.addBasicMetric("NEDDB", this.getExclusiveDataBasedDecisions(), this.numberProcess);
		this.json.addBasicMetric("NEDEB", this.getExclusiveEventBasedDecisions(), this.numberProcess);
		this.json.addBasicMetric("NL", this.getLanes(), this.numberProcess);
		this.json.addBasicMetric("NMF", this.getMessageFlows(), this.numberProcess);
		this.json.addBasicMetric("NP", this.getPools(), this.numberProcess);
		this.json.addBasicMetric("NEP", this.getEmptyPools(), this.numberProcess);
		this.json.addBasicMetric("NSFE", this.getSequenceFlowsFromEvents(), this.numberProcess);
		this.json.addBasicMetric("NSFG", this.getSequenceFlowsFromGateways(), this.numberProcess);
		this.json.addBasicMetric("NSFA", this.getSequenceFlowsBetweenActivities(), this.numberProcess);
		this.json.addBasicMetric("NAC", this.getActivationConditions(), this.numberProcess);
		this.json.addBasicMetric("NACT", this.getActivities(), this.numberProcess);
		this.json.addBasicMetric("NART", this.getArtifacts(), this.numberProcess);
		this.json.addBasicMetric("NASI", this.getAssignments(), this.numberProcess);
		this.json.addBasicMetric("NASO", this.getAssociations(), this.numberProcess);
		this.json.addBasicMetric("NAUD", this.getAuditing(), this.numberProcess);
		this.json.addBasicMetric("NBEL", this.getBaseElements(), this.numberProcess);
		this.json.addBasicMetric("NBEV", this.getBoundaryEvents(), this.numberProcess);
		this.json.addBasicMetric("NBRT", this.getBusinessRuleTasks(), this.numberProcess);
		this.json.addBasicMetric("NCEL", this.getCallableElements(), this.numberProcess);
		this.json.addBasicMetric("NCAC", this.getCallActivities(), this.numberProcess);
		//this.json.addBasicMetric("NCCO", this.getCallConversations(), this.numberProcess);
		this.json.addBasicMetric("NCANEV", this.getCancelEvents(), this.numberProcess);
		this.json.addBasicMetric("NCATEV", this.getCatchEvents(), this.numberProcess);
		this.json.addBasicMetric("NCVAL", this.getCategoryValues(), this.numberProcess);
		this.json.addBasicMetric("NCOL", this.getCollaborations(), this.numberProcess);
		this.json.addBasicMetric("NCOMEV", this.getCompensateEvents(), this.numberProcess);
		this.json.addBasicMetric("NCOCON", this.getCompletionConditions(), this.numberProcess);
		this.json.addBasicMetric("NCBDEF", this.getComplexBehaviorDefinitions(), this.numberProcess);
		//this.json.addBasicMetric("NCOND", this.getConditions(), this.numberProcess);
		this.json.addBasicMetric("NCONDEV", this.getConditionalEvent(), this.numberProcess);
		this.json.addBasicMetric("NCONDEX", this.getConditionExpressions(), this.numberProcess);
		//this.json.addBasicMetric("NCONV", this.getConversations(), this.numberProcess);
		//this.json.addBasicMetric("NCONVAS", this.getConversationAssociations(), this.numberProcess);
		//this.json.addBasicMetric("NCONVL", this.getConversationLinks(), this.numberProcess);
		//this.json.addBasicMetric("NCONVN", this.getConversationNodes(), this.numberProcess);
		//this.json.addBasicMetric("NCORK", this.getCorrelationKeys(), this.numberProcess);
		//this.json.addBasicMetric("NCORP", this.getCorrelationProperties(), this.numberProcess);
		//this.json.addBasicMetric("NCORPB", this.getCorrelationPropertyBindings(), this.numberProcess);
		//this.json.addBasicMetric("NCORPRE", this.getCorrelationPropertyRetrievalExpressions(), this.numberProcess);
		//this.json.addBasicMetric("NCORS", this.getCorrelationSubscriptions(), this.numberProcess);
		this.json.addBasicMetric("NDA", this.getDataAssociations(), this.numberProcess);
		this.json.addBasicMetric("NDInA", this.getDataInputAssociations(), this.numberProcess);
		this.json.addBasicMetric("NDO", this.getDataObjects(), this.numberProcess);
		this.json.addBasicMetric("NDOR", this.getDataObjectReferences(), this.numberProcess);
		this.json.addBasicMetric("NDOutA", this.getDataOutputAssociations(), this.numberProcess);
		this.json.addBasicMetric("NDSTA", this.getDataStates(), this.numberProcess);
		this.json.addBasicMetric("NDSTO", this.getDataStores(), this.numberProcess);
		this.json.addBasicMetric("NDEF", this.getDefinitions(), this.numberProcess);
		this.json.addBasicMetric("NDOC", this.getDocumentations(), this.numberProcess);
		this.json.addBasicMetric("NENDEV", this.getEndEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDP", this.getEndPoints(), this.numberProcess);
		//this.json.addBasicMetric("NERR", this.getErrors(), this.numberProcess);
		this.json.addBasicMetric("NERREV", this.getErrorEvents(), this.numberProcess);
		//this.json.addBasicMetric("NESC", this.getEscalations(), this.numberProcess);
		this.json.addBasicMetric("NESCEV", this.getEscalationEvents(), this.numberProcess);
		this.json.addBasicMetric("NEV", this.getEvents(), this.numberProcess);
		this.json.addBasicMetric("NEVDEF", this.getEventDefinitions(), this.numberProcess);
		this.json.addBasicMetric("NEXP", this.getExpressions(), this.numberProcess);
		this.json.addBasicMetric("NEXT", this.getExtensions(), this.numberProcess);
		this.json.addBasicMetric("NEXTEL", this.getExtensionElements(), this.numberProcess);
		this.json.addBasicMetric("NFLEL", this.getFlowElements(), this.numberProcess);
		this.json.addBasicMetric("NFLNO", this.getFlowNodes(), this.numberProcess);
		this.json.addBasicMetric("NFOREXP", this.getFormalExpressions(), this.numberProcess);
		this.json.addBasicMetric("NGA", this.getGateways(), this.numberProcess);
		//this.json.addBasicMetric("NGC", this.getGlobalConversations(), this.numberProcess);
		//this.json.addBasicMetric("NHP", this.getHumanPerformers(), this.numberProcess);
		this.json.addBasicMetric("NIMP", this.getImports(), this.numberProcess);
		this.json.addBasicMetric("NInDI", this.getInputDataItems(), this.numberProcess);
		this.json.addBasicMetric("NInS", this.getInputSets(), this.numberProcess);
		this.json.addBasicMetric("NINTNO", this.getInteractionNodes(), this.numberProcess);
		this.json.addBasicMetric("NINTE", this.getInterfaces(), this.numberProcess);
		this.json.addBasicMetric("NICEV", this.getIntermediateCatchEvents(), this.numberProcess);
		this.json.addBasicMetric("NITEV", this.getIntermediateThrowEvents(), this.numberProcess);
		this.json.addBasicMetric("NIOB", this.getIoBindings(), this.numberProcess);
		this.json.addBasicMetric("NIOS", this.getIoSpecifications(), this.numberProcess);
		this.json.addBasicMetric("NIAEL", this.getItemAwareElements(), this.numberProcess);
		//this.json.addBasicMetric("NIDEF", this.getItemDefinitions(), this.numberProcess);
		this.json.addBasicMetric("NLEV", this.getLinkEvents(), this.numberProcess);
		this.json.addBasicMetric("NLOOPCA", this.getLoopCardinalities(), this.numberProcess);
		this.json.addBasicMetric("NLOOPCH", this.getLoopCharacteristics(), this.numberProcess);
		this.json.addBasicMetric("NMT", this.getManualTasks(), this.numberProcess);
		//this.json.addBasicMetric("NMES", this.getMessages(), this.numberProcess);
		this.json.addBasicMetric("NMESEV", this.getMessageEvents(), this.numberProcess);
		this.json.addBasicMetric("NMESFA", this.getMessageFlowAssociations(), this.numberProcess);
		this.json.addBasicMetric("NMON", this.getMonitorings(), this.numberProcess);
		this.json.addBasicMetric("NMILCH", this.getMultiInstanceLoopCharacteristics(), this.numberProcess);
		this.json.addBasicMetric("NOP", this.getOperations(), this.numberProcess);
		this.json.addBasicMetric("NOutDI", this.getOutputDataItems(), this.numberProcess);
		this.json.addBasicMetric("NOutS", this.getOutputSets(), this.numberProcess);
		this.json.addBasicMetric("NPG", this.getParallelGateways(), this.numberProcess);
		this.json.addBasicMetric("NPAS", this.getParticipantAssociations(), this.numberProcess);
		this.json.addBasicMetric("NPM", this.getParticipantMultiplicities(), this.numberProcess);
		//this.json.addBasicMetric("NPER", this.getPerformers(), this.numberProcess);
		//this.json.addBasicMetric("NPO", this.getPotentialOwners(), this.numberProcess);
		this.json.addBasicMetric("NPROC", this.getProcesses(), this.numberProcess);
		this.json.addBasicMetric("NPROP", this.getProperties(), this.numberProcess);
		this.json.addBasicMetric("NRT", this.getReceiveTasks(), this.numberProcess);
		this.json.addBasicMetric("NREL", this.getRelationships(), this.numberProcess);
		this.json.addBasicMetric("NREN", this.getRenderings(), this.numberProcess);
		this.json.addBasicMetric("NRES", this.getResources(), this.numberProcess);
		this.json.addBasicMetric("NRESAEX", this.getResourceAssignmentExpressions(), this.numberProcess);
		//this.json.addBasicMetric("NRESP", this.getResourceParameters(), this.numberProcess);
		this.json.addBasicMetric("NRESPB", this.getResourceParameterBindings(), this.numberProcess);
		this.json.addBasicMetric("NRESR", this.getResourceRoles(), this.numberProcess);
		this.json.addBasicMetric("NRE", this.getRootElements(), this.numberProcess);
		//this.json.addBasicMetric("NSCR", this.getScripts(), this.numberProcess);
		this.json.addBasicMetric("NSCT", this.getScriptTasks(), this.numberProcess);
		this.json.addBasicMetric("NSENT", this.getSendTasks(), this.numberProcess);
		this.json.addBasicMetric("NSEQF", this.getSequenceFlows(), this.numberProcess);
		this.json.addBasicMetric("NSERT", this.getServiceTasks(), this.numberProcess);
		//this.json.addBasicMetric("NSI", this.getSignals(), this.numberProcess);
		this.json.addBasicMetric("NSIEV", this.getSignalEvent(), this.numberProcess);
		this.json.addBasicMetric("NSTEV", this.getStartEvents(), this.numberProcess);
		//this.json.addBasicMetric("NSCONV", this.getSubConversations(), this.numberProcess);
		this.json.addBasicMetric("NTEX", this.getTexts(), this.numberProcess);
		this.json.addBasicMetric("NTEXA", this.getTextAnnotations(), this.numberProcess);
		this.json.addBasicMetric("NTHEV", this.getThrowEvents(), this.numberProcess);
		this.json.addBasicMetric("NTC", this.getTimeCycles(), this.numberProcess);
		this.json.addBasicMetric("NTDA", this.getTimeDates(), this.numberProcess);
		this.json.addBasicMetric("NTDU", this.getTimeDurations(), this.numberProcess);
		this.json.addBasicMetric("NTEV", this.getTimerEventDefinitions(), this.numberProcess);
		this.json.addBasicMetric("NTR", this.getTransactions(), this.numberProcess);
		this.json.addBasicMetric("NUT", this.getUserTasks(), this.numberProcess);
		this.json.addBasicMetric("NSUB", this.getSubprocesses(), this.numberProcess);
		this.json.addBasicMetric("NCSUB", this.getCollapsedSubprocesses(), this.numberProcess);
		this.json.addBasicMetric("NFDCG", this.getFlowDividingComplexGateways(), this.numberProcess);
		this.json.addBasicMetric("NFDEBG", this.getFlowDividingEventBasedGateways(), this.numberProcess);
		this.json.addBasicMetric("NFDEXG", this.getFlowDividingExclusiveGateways(), this.numberProcess);
		this.json.addBasicMetric("NFDIG", this.getFlowDividingInclusiveGateways(), this.numberProcess);
		this.json.addBasicMetric("NFDPG", this.getFlowDividingParallelGateways(), this.numberProcess);
		this.json.addBasicMetric("NFDG", this.getFlowDividingGateways(), this.numberProcess);
		this.json.addBasicMetric("NFDT", this.getFlowDividingTasks(), this.numberProcess);
		this.json.addBasicMetric("NFJCG", this.getFlowJoiningComplexGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJEBG", this.getFlowJoiningEventBasedGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJEXG", this.getFlowJoiningExclusiveGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJIG", this.getFlowJoiningInclusiveGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJPG", this.getFlowJoiningParallelGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJG", this.getFlowJoiningGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJT", this.getFlowJoiningTasks(), this.numberProcess);
		this.json.addBasicMetric("NFJDCG", this.getFlowJoiningAndDividingComplexGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJDEBG", this.getFlowJoiningAndDividingEventBasedGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJDEXG", this.getFlowJoiningAndDividingExclusiveGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJDIG", this.getFlowJoiningAndDividingInclusiveGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJDPG", this.getFlowJoiningAndDividingParallelGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJDG", this.getFlowJoiningAndDividingGateways(), this.numberProcess);
		this.json.addBasicMetric("NFJDT", this.getFlowJoiningAndDividingTasks(), this.numberProcess);
		this.json.addBasicMetric("NSTMEV", this.getStartMessageEvents(), this.numberProcess);
		this.json.addBasicMetric("NSTCOEV", this.getStartConditionalEvents(), this.numberProcess);
		this.json.addBasicMetric("NSTSIGEV", this.getStartSignalEvents(), this.numberProcess);
		this.json.addBasicMetric("NSTTEV", this.getStartTimerEvents(), this.numberProcess);
		this.json.addBasicMetric("NSTMUEV", this.getStartMultipleEvents(), this.numberProcess);
		this.json.addBasicMetric("NSTPMUEV", this.getStartParallelMultipleEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDCEV", this.getEndCancelEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDCOMEV", this.getEndCompensationEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDCOMEV", this.getCompensateEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDERREV", this.getEndErrorEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDESCEV", this.getEndEscalationEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDMEV", this.getEndMessageEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDSIGEV", this.getEndSignalEvents(), this.numberProcess);
		this.json.addBasicMetric("NTEEV", this.getEndTerminateEvents(), this.numberProcess);
		this.json.addBasicMetric("NENDMUEV", this.getEndMultipleEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBEV", this.getInterruptingBoundaryEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBCANCEV", this.getInterruptingBoundaryCancelEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBCOMPEV", this.getInterruptingBoundaryCompensationEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBCOEV", this.getInterruptingBoundaryConditionalEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBERREV", this.getInterruptingBoundaryErrorEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBESCEV", this.getInterruptingBoundaryEscalationEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBMEV", this.getInterruptingBoundaryMessageEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBSIGEV", this.getInterruptingBoundarySignalEvents(), this.numberProcess);
		this.json.addBasicMetric("NIBTEV", this.getInterruptingBoundaryTimerEvents(), this.numberProcess);
		this.json.addBasicMetric("NIESCTEV", this.getIntermediateEscalationThrowEvents(), this.numberProcess);
		this.json.addBasicMetric("NICOMTEV", this.getIntermediateCompensationThrowEvents(), this.numberProcess);
		this.json.addBasicMetric("NILTEV", this.getIntermediateLinkThrowEvents(), this.numberProcess);
		this.json.addBasicMetric("NIMTEV", this.getIntermediateMessageThrowEvents(), this.numberProcess);
		this.json.addBasicMetric("NISIGTEV", this.getIntermediateSignalThrowEvents(), this.numberProcess);
		this.json.addBasicMetric("NIMUTEV",this.getIntermediateMultipleThrowEvents(), this.numberProcess);
		this.json.addBasicMetric("NICONCEV", this.getIntermediateConditionalCatchEvents(), this.numberProcess);
		this.json.addBasicMetric("NILCEV", this.getIntermediateLinkCatchEvents(), this.numberProcess);
		this.json.addBasicMetric("NIMCEV", this.getIntermediateMessageCatchEvents(), this.numberProcess);
		this.json.addBasicMetric("NISIGCEV", this.getIntermediateSignalCatchEvents(), this.numberProcess);
		this.json.addBasicMetric("NIMUCEV", this.getIntermediateMultipleCatchEvents(), this.numberProcess);
		this.json.addBasicMetric("NIPMUCEV", this.getIntermediateParallelMultipleCatchEvents(), this.numberProcess);
		this.json.addBasicMetric("NBNIEV", this.getNonInterruptingBoundaryEvents(), this.numberProcess);
		this.json.addBasicMetric("NBNIMEV", this.getNonInterruptingBoundaryMessageEvents(), this.numberProcess);
		this.json.addBasicMetric("NBNITEV", this.getNonInterruptingBoundaryTimerEvents(), this.numberProcess);
		this.json.addBasicMetric("NBNICONEV", this.getNonInterruptingBoundaryConditionalEvents(), this.numberProcess);
		this.json.addBasicMetric("NBNISIGEV", this.getNonInterruptingBoundarySignalEvents(), this.numberProcess);
		this.json.addBasicMetric("NBNIMUEV", this.getNonInterruptingBoundaryMultipleEvents(), this.numberProcess);
		this.json.addBasicMetric("NBIMUEV", this.getInterruptingBoundaryMultipleEvents(), this.numberProcess);
		this.json.addBasicMetric("NBNIESCEV", this.getNonInterruptingBoundaryEscalationEvents(), this.numberProcess);
		this.json.addBasicMetric("NBNIPMUEV", this.getNonInterruptingBoundaryParallelMultipleEvents(), this.numberProcess);
		this.json.addBasicMetric("NBIPMUEV", this.getInterruptingBoundaryParallelMultipleEvents(), this.numberProcess);
		json.addBasicMetric("TNIE", getTotalNumberOfIntermediateEvents(), this.numberProcess);
		this.json.addBasicMetric("NG", getGroups(), this.numberProcess);
		this.json.addBasicMetric("NSL", getSwimlanes(), this.numberProcess);
		this.json.addBasicMetric("NITMREV", getIntermediateTimerEvents(), this.numberProcess);
		this.json.addBasicMetric("NESUB", getEventSubprocesses(), this.numberProcess);
		this.json.addBasicMetric("NCESUB", getCollapsedEventSubprocesses(), this.numberProcess);
		this.json.addBasicMetric("NCP", getCollapsedPools(), this.numberProcess);
		this.json.addBasicMetric("NSTESEV", getStartEscalationEvents(), this.numberProcess);
		this.json.addBasicMetric("NSTEREV", getStartErrorEvents(), this.numberProcess);
		this.json.addBasicMetric("NSTCOMEV", getStartCompensationEvents(), this.numberProcess);
		this.json.addBasicMetric("NSTNNEV", getStartNoneEvents(), this.numberProcess);
		this.json.addBasicMetric("NINNEV", getIntermediateNoneEvents(), this.numberProcess);
		this.json.addBasicMetric("NENNNEV", getEndNoneEvents(), this.numberProcess);
		this.json.addBasicMetric("NISCE", this.getNonInterruptingStartConditionalEvents(), this.numberProcess);
		this.json.addBasicMetric("NISEE", this.getNonInterruptingStartEscalationEvents(), this.numberProcess);
		this.json.addBasicMetric("NISME", this.getNonInterruptingStartMessageEvents(), this.numberProcess);
		this.json.addBasicMetric("NISSE", this.getNonInterruptingStartSignalEvents(), this.numberProcess);
		this.json.addBasicMetric("NISMUE", this.getNonInterruptingStartMultipleEvents(), this.numberProcess);
		this.json.addBasicMetric("NISMUPE", this.getNonInterruptingStartMultipleParallelEvents(), this.numberProcess);
		this.json.addBasicMetric("NISTE", this.getNonInterruptingStartTimerEvents(), this.numberProcess);
	}
	
	/**
	 * Metrica: NT
	 * 
	 * @return number of tasks
	 */
	public int getTasks() {
		return getNumberOfTypeElement(Task.class);
	}

	/**
	 * Metrica: NCD (number of complex gateways)
	 * 
	 * @return Number of complex decisions 
	 */
	public int getComplexDecisions() {
		return getNumberOfTypeElement(ComplexGateway.class);
	}

	/**
	 * Metrica: NDOin
	 * 
	 * @return number of data objects that are input of activities
	 */
	public int getDataObjectsInput() {
		if(this.extraction.equals("Process")) {
			Vector<String> dataInput = new Vector<String>();
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				for(DataInputAssociation da : ((Activity) a).getDataInputAssociations())
					for(ItemAwareElement iae : da.getSources() )
						if(!dataInput.contains(iae.getId()))
							dataInput.add(iae.getId());
			return dataInput.size();
		}
		return getNumberOfTypeElement(DataInput.class);
	}

	/**
	 * Metrica: NDOout
	 * 
	 * @return number of data objects that are output of activities
	 */
	public int getDataObjectsOutput() {
		if(this.extraction.equals("Process")) {
			Vector<String> dataOutput = new Vector<String>();
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				for(DataOutputAssociation da : ((Activity) a).getDataOutputAssociations())
					if(!dataOutput.contains(da.getTarget().getId()))
						dataOutput.add(da.getTarget().getId());
			return dataOutput.size();
		}
		return getNumberOfTypeElement(DataOutput.class);
	}

	/**
	 * Metrica: NID (number of inclusive gateways)
	 * 
	 * @return number of inclusive decisions
	 */
	public int getInclusiveDecisions() {
		return getNumberOfTypeElement(InclusiveGateway.class);
	}
	
	/**
	 * Metrica: NEDDB (number of exclusive gateways)
	 * 
	 * @return number of data based decisions
	 */
	public int getExclusiveDataBasedDecisions() {
		return getNumberOfTypeElement(ExclusiveGateway.class);
	}

	/**
	 * Metrica: NEDEB (number of event based gateways)
	 * 
	 * @return number of event based decisions
	 */
	public int getExclusiveEventBasedDecisions() {
		return getNumberOfTypeElement(EventBasedGateway.class);
	}

	/**
	 * Metrica: NL
	 * 
	 * @return number of lanes
	 */
	public int getLanes() {
		int numberOfLanes = 0;
		try {
			if(this.extraction.equals("Model")) {
				Collection<Participant> participants = this.modelInstance.getModelElementsByType(Participant.class);
				for (Participant p : participants) {
					Collection<LaneSet> laneSets = p.getProcess().getLaneSets();
					for (LaneSet l : laneSets) {
						Collection<Lane> lanes = l.getLanes();
						if (lanes.size() > 1) {
							numberOfLanes += lanes.size();
						}
					}
				}
			} else {
				Collection<LaneSet> laneSets = this.process.getLaneSets();
				for (LaneSet l : laneSets) {
					Collection<Lane> lanes = l.getLanes();
					if (lanes.size() > 1) {
						numberOfLanes += lanes.size();
					}
				}	
			}
		} catch (Exception e) {}
		return numberOfLanes;
	}
	
	/**
	 * Metrica: NMF
	 * 
	 * @return number of message flows
	 */
	public int getMessageFlows() {
		return getNumberOfTypeElement(MessageFlow.class);
	}

	/**
	 * Metrica: NP
	 * 
	 * @return number of pools
	 */
	public int getPools() {
		return getNumberOfTypeElement(Participant.class);
	}
	
	/**
	 * Metric: NSL
	 * 
	 * @return number of Swimlanes
	 */
	public int getSwimlanes() {
		return getPools() + getLanes();
	}

	/**
	 * Metrica: NSFE 
	 * 
	 * @return number of outgoing sequence flows from events
	 */
	public int getSequenceFlowsFromEvents() {
		Collection<Event> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(Event.class);
		else {
			events = this.process.getChildElementsByType(Event.class);
			ec.getEventsSubProcess(events, process);
		}
		int numberOfOutgoingSequenceFlows = 0;
		for (Event e : events) {
			numberOfOutgoingSequenceFlows += e.getOutgoing().size();
		}
		return numberOfOutgoingSequenceFlows;
	}
	
	/**
	 * Metrica:NSFG 
	 * 
	 * @return Number of outgoing sequence flows from gateways
	 */
	public int getSequenceFlowsFromGateways() {
		Collection<Gateway> gateways;
		if(this.extraction.equals("Model"))
			gateways = this.modelInstance.getModelElementsByType(Gateway.class);
		else {
			gateways = this.process.getChildElementsByType(Gateway.class);
			ec.getGatewaysSubProcess(gateways, process);
		}
		int numberOfOutgoingSequenceFlows = 0;
		for (Gateway g : gateways) {
			numberOfOutgoingSequenceFlows += g.getOutgoing().size();
		}
		return numberOfOutgoingSequenceFlows;
	}

	/**
	 * Metrica: NSFA 
	 * 
	 * @return Number of sequence flows between activities
	 */
	public int getSequenceFlowsBetweenActivities() {
		Collection<Activity> activities;
		if(this.extraction.equals("Model")) 
			activities =  this.modelInstance.getModelElementsByType(Activity.class);
		else {
			activities = this.process.getChildElementsByType(Activity.class);
			ec.getActivitiesSubProcess(activities, process);
		}
		Collection<SequenceFlow> outgoingFlows;
		int numberOfSequenceFlows = 0;
		for (Activity a : activities) {
			outgoingFlows = a.getOutgoing();
			for (SequenceFlow s : outgoingFlows) {
				try {
					if (s.getTarget() instanceof Activity) {
						numberOfSequenceFlows++;
					}
				}catch(Exception e) {
					continue;
				}
			}
		}
		return numberOfSequenceFlows;
	}
	
	/**
	 * Metric: NAC
	 * 
	 * @return number of Activation Conditions 
	 */
	public int getActivationConditions() {
		if(this.extraction.equals("Process")) {
			int a = 0;
			for(ModelElementInstance fe : this.getCollectionOfElementType(ComplexGateway.class))
				if(((ComplexGateway) fe).getActivationCondition() != null)
				  a++;
			return a;
		}
		return getNumberOfTypeElement(ActivationCondition.class);
	}
	
	/**
	 * Metric: NACT
	 * 
	 * @return number of Activities
	 */
	public int getActivities() {
		return getNumberOfTypeElement(Activity.class);
	}
	
	/**
	 * Metric: NART
	 * 
	 * @return number of Artifacts
	 */
	public int getArtifacts() {
		return getNumberOfTypeElement(Artifact.class);
	}
	
	/**
	 * Metric: NASI
	 * 
	 * @return number of Assignments
	 */
	public int getAssignments() {
		if(this.extraction.equals("Process")) {
			int as = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) {
				for(DataInputAssociation da : ((Activity) a).getDataInputAssociations())
					as += da.getAssignments().size();
				for(DataOutputAssociation da : ((Activity) a).getDataOutputAssociations())
					as += da.getAssignments().size();
			}
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(DataInputAssociation da : ((ThrowEvent) e).getDataInputAssociations())
					as += da.getAssignments().size();
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
					for(DataOutputAssociation da : ((CatchEvent) e).getDataOutputAssociations())
						as += da.getAssignments().size();
			return as;
		}
		return getNumberOfTypeElement(Assignment.class);
	}
	
	/**
	 * Metric: NASO 
	 * 
	 * @return number of Associations
	 */
	public int getAssociations() {
		return getNumberOfTypeElement(Association.class);
	}
	
	/**
	 * Metric: NAUD
	 * 
	 * @return number of Auditings
	 */
	public int getAuditing() {
		if(this.extraction.equals("Process")) {
			int a = 0;
			if(this.process.getAuditing() != null)
				  a++;
			for(ModelElementInstance fe : this.getCollectionOfElementType(FlowElement.class))
				if(((FlowElement) fe).getAuditing() != null)
				  a++;
			return a;
		}
		return getNumberOfTypeElement(Auditing.class);
	}
	
	/**
	 * Metric: NBEL
	 * 
	 * @return number of base elements
	 */
	public int getBaseElements() {
		return getNumberOfTypeElement(BaseElement.class);
	}
	
	/**
	 * Metric: NBEV
	 * 
	 * @return number of Boundary Events
	 */
	public int getBoundaryEvents() {
		return getNumberOfTypeElement(BoundaryEvent.class);
	}
	
	/**
	 * Metric: NIBEV
	 * @return the number of Interrupting Boundary Events
	 */
	public int getInterruptingBoundaryEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		int toReturn = 0;
		for (BoundaryEvent event: boundaryEvents) {
			if (event.cancelActivity()) {
				toReturn += 1;
			}
		 }
		return toReturn;
	}
	
	/**
	 * Metric: NIBMT
	 * 
	 * @return number of interrupting Boundary Message Events
	 */
	public int getInterruptingBoundaryMessageEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.MessageEventDefinitionImpl");
	}
	
	/**
	 * Metric: NIBTEV
	 * 
	 * @return number of interrupting Boundary Timer Events
	 */
	public int getInterruptingBoundaryTimerEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.TimerEventDefinitionImpl");
	}
	
	/**
	 * Metric: NIBCOEV
	 * 
	 * @return number of interrupting Boundary Conditional Events
	 */
	public int getInterruptingBoundaryConditionalEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.ConditionalEventDefinitionImpl");
	}
	
	/**
	 * Metric: NIBSIGEV
	 * 
	 * @return number of interrupting Boundary Signal Events
	 */
	public int getInterruptingBoundarySignalEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.SignalEventDefinitionImpl");
	}
	
	/**
	 * Metric: NIBESCEV
	 * 
	 * @return number of interrupting Boundary Escalation Events
	 */
	public int getInterruptingBoundaryEscalationEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.EscalationEventDefinitionImpl");
	}
	
	/**
	 * Metric: NIBCANCEV
	 * 
	 * @return number of interrupting Boundary Cancel Events
	 */
	public int getInterruptingBoundaryCancelEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.CancelEventDefinitionImpl");
	}
	
	/**
	 * Metric: NIBERREV
	 * 
	 * @return number of interrupting Boundary Error Events
	 */
	public int getInterruptingBoundaryErrorEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.ErrorEventDefinitionImpl");
		}
	
	/**
	 * Metric: NIBCOMPEV 
	 * 
	 * @return number of  interrupting Boundary Compensation Events
	 */
	public int getInterruptingBoundaryCompensationEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.CompensateEventDefinitionImpl");
	}
	
	/**
	 * Metric: NBNIEV
	 * @return the number of non-interrupting boundary events
	 */
	public int getNonInterruptingBoundaryEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		int toReturn = 0;
		 for (BoundaryEvent event: boundaryEvents) {
			 if (!event.cancelActivity()) {
				 toReturn += 1;
			 }
		 }
		return toReturn;
	}
	
	/**
	 * Metric: NBNIMEV
	 * @return the number of non-interrupting boundary message events
	 */
	public int getNonInterruptingBoundaryMessageEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getNonInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.MessageEventDefinitionImpl");
	}
	
	/**
	 * Metric: NBNITEV
	 * @return
	 */
	public int getNonInterruptingBoundaryTimerEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getNonInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.TimerEventDefinitionImpl");
	}
	
	/**
	 * Metric: NBNICONEV
	 * @return the number of non-interrupting boundary conditional events
	 */
	public int getNonInterruptingBoundaryConditionalEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getNonInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.ConditionalEventDefinitionImpl");
	}
	
	/**
	 * Metric: NBNISIGEV
	 * @return the number of non-interrupting boundary signal events
	 */
	public int getNonInterruptingBoundarySignalEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getNonInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.SignalEventDefinitionImpl");
	}
	
	/**
	 * Metric: NBNIESCEV
	 * @return the number of non-interrupting boundary escalation events
	 */
	public int getNonInterruptingBoundaryEscalationEvents() {
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		return this.getNonInterruptingBoundaryEventsEventDefinitions(boundaryEvents, "org.camunda.bpm.model.bpmn.impl.instance.EscalationEventDefinitionImpl");
	}
	
	/**
	 * Metric: NBNIMUEV
	 * @return the number of non-interrupting boundary multiple events
	 */
	public int getNonInterruptingBoundaryMultipleEvents() {
		int toReturn = 0;
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		for (BoundaryEvent event: boundaryEvents) {
			if (!event.cancelActivity() && event.getEventDefinitions().size() > 1) {
				toReturn += 1;
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Metric: NBNIPMUEV
	 * @return the number of non-interrupting boundary parallel multiple events
	 */
	public int getNonInterruptingBoundaryParallelMultipleEvents() {
		int toReturn = 0;
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		for (BoundaryEvent event: boundaryEvents) {
			if (!event.cancelActivity() && event.isParallelMultiple()) {
				toReturn++;
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Metric: NBRT
	 * 
	 * @return number dei Business Rule Task
	 */
	public int getBusinessRuleTasks() {
		return getNumberOfTypeElement(BusinessRuleTask.class);
	}
	
	/**
	 * Metric: NCEL
	 * 
	 * @return number of Callable Elements
	 */
	public int getCallableElements() {
		return getNumberOfTypeElement(CallableElement.class);
	}
	
	/**
	 * Metric: NCAC
	 * 
	 * @return number of Call Activities
	 */
	public int getCallActivities() {
		return getNumberOfTypeElement(CallActivity.class);
	}
	
	/**
	 * Metric: NCCO
	 * 
	 * @return number of Call Conversations
	 */
	/*public int getCallConversations() {
		return getNumberOfTypeElement(CallConversation.class);
	}*/
	
	/**
	 * Metric: NCANEV
	 * 
	 * @return number of Cancel Events
	 */
	public int getCancelEvents() {
		if(this.extraction.equals("Process")) {
			int ce = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof CancelEventDefinition)
						ce++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof CancelEventDefinition)
						ce++;
			return ce;
		}
		return getNumberOfTypeElement(CancelEventDefinition.class);
	}
	
	/**
	 * Metric: NCATEV
	 * 
	 * @return number of Catch Events
	 */
	public int getCatchEvents() {
		return getNumberOfTypeElement(CatchEvent.class);
	}
	
	/**
	 * Metric: NCVAL
	 * 
	 * @return number of Category Values
	 */
	public int getCategoryValues() {
		return getNumberOfTypeElement(CategoryValue.class);
	}
	
	/**
	 * Metric: NCOL
	 * 
	 * @return number of Collaboration
	 */
	public int getCollaborations() {
		return getNumberOfTypeElement(Collaboration.class);
	}
	
	/**
	 * Metric: NCOMEV
	 * 
	 * @return number of Compensate Events 
	 */
	public int getCompensateEvents() {
		if(this.extraction.equals("Process")) {
			int ce = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof CompensateEventDefinition)
						ce++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof CompensateEventDefinition)
						ce++;
			return ce;
		}
		return getNumberOfTypeElement(CompensateEventDefinition.class);
	}
	
	/**
	 * Metric: NCOCON
	 * 
	 * @return number of Completion Conditions
	 */
	public int getCompletionConditions() {
		if(this.extraction.equals("Process")) {
			int cc = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) {
				if(((Activity)a).getLoopCharacteristics() instanceof MultiInstanceLoopCharacteristics)
					if(((MultiInstanceLoopCharacteristics) ((Activity)a).getLoopCharacteristics()).getCompletionCondition() != null)
					  cc++;
			}
			return cc;
		}
		return getNumberOfTypeElement(CompletionCondition.class);
	}
	
	/**
	 * Metric: NCBDEF
	 * 
	 * @return number of Complex Behavior
	 */
	public int getComplexBehaviorDefinitions() {
		return getNumberOfTypeElement(ComplexBehaviorDefinition.class);
	}
	
	/**
	 * Metric: NCOND
	 * 
	 * @return number of Conditions
	 */
	/*public int getConditions() {
		return getNumberOfTypeElement(Condition.class);
	}*/
	
	/**
	 * Metric: NCONDEV
	 * 
	 * @return number of Conditional Event
	 */
	public int getConditionalEvent() {
		if(this.extraction.equals("Process")) {
			int ce = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof ConditionalEventDefinition)
						ce++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof ConditionalEventDefinition)
						ce++;
			return ce;
		}
		return getNumberOfTypeElement(ConditionalEventDefinition.class);
	}
	
	/**
	 * Metric: NCONDEX
	 * 
	 * @return number of Condition Expressions
	 */
	public int getConditionExpressions() {
		if(this.extraction.equals("Process")) {
			int ce = 0;
			for(ModelElementInstance fe : this.getCollectionOfElementType(SequenceFlow.class))
				if(((SequenceFlow) fe).getConditionExpression() != null)
				  ce++;
			return ce;
		}

		return getNumberOfTypeElement(ConditionExpression.class);
	}
	
	/**
	 * Metric: NCONV
	 * 
	 * @return number of Conversations
	 */
	/*public int getConversations() {
		return getNumberOfTypeElement(Conversation.class);
	}*/
	
	/**
	 * Metric: NCONVAS
	 * 
	 * @return number of Conversation Associations
	 */
	/*public int getConversationAssociations() {
		return getNumberOfTypeElement(ConversationAssociation.class);
	}*/
	
	/**
	 * Metric: NCONVL
	 * 
	 * @return number of Conversation Links
	 */
	/*public int getConversationLinks() {
		return getNumberOfTypeElement(ConversationLink.class);
	}*/
	
	/**
	 * Metric: NCONVN
	 * 
	 * @return number of Conversation Node
	 */
	/*public int getConversationNodes() {
		return getNumberOfTypeElement(ConversationNode.class);
	}*/
	
	/**
	 * Metric: NCORK
	 * 
	 * @return number of Correlation Keys
	 */
	/*public int getCorrelationKeys() {
		return getNumberOfTypeElement(CorrelationKey.class);
	}*/
	
	/**
	 * Metric: NCORP
	 * 
	 * @return number of Correlation Properties
	 */
	/*public int getCorrelationProperties() {
		return getNumberOfTypeElement(CorrelationProperty.class);
	}*/
	
	/**
	 * Metric: NCORPB
	 * 
	 * @return number of Correlation Property Bindings
	 */
	/*public int getCorrelationPropertyBindings() {
		return getNumberOfTypeElement(CorrelationPropertyBinding.class);
	}*/
	
	/**
	 * Metric: NCORPRE
	 * 
	 * @return number of Correlation Property Retrieval Expressions
	 */
	/*public int getCorrelationPropertyRetrievalExpressions() {
		return getNumberOfTypeElement(CorrelationPropertyRetrievalExpression.class);
	}*/
	
	/**
	 * Metric: NCORS
	 * 
	 * @return number of Correlation Subscriptions
	 */
	/*public int getCorrelationSubscriptions() {
		return getNumberOfTypeElement(CorrelationSubscription.class);
	}*/
	
	/**
	 * Metric: NDA
	 * 
	 * @return number of Data Associations
	 */
	public int getDataAssociations() {
		if(this.extraction.equals("Process"))
			return this.getDataInputAssociations() + this.getDataOutputAssociations();
		return getNumberOfTypeElement(DataAssociation.class);
	}
	
	/**
	 * Metric: NDInA
	 * 
	 * @return number of Data Input Associations
	 */
	public int getDataInputAssociations() {
		if(this.extraction.equals("Process")) {
			int DataObjectsInput = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				DataObjectsInput += ((Activity) a).getDataInputAssociations().size();
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				DataObjectsInput += ((ThrowEvent) e).getDataInputAssociations().size();
			return DataObjectsInput;
		}
		return getNumberOfTypeElement(DataInputAssociation.class);
	}
	
	/**
	 * Metric: NDO
	 * 
	 * @return number of Data Objects
	 */
	public int getDataObjects() {
		return getNumberOfTypeElement(DataObject.class);
	}
	
	/**
	 * Metric: NDOR
	 * 
	 * @return number of Data Object References
	 */
	public int getDataObjectReferences() {
		return getNumberOfTypeElement(DataObjectReference.class);
	}
	
	/**
	 * Metric: NDOutA
	 * 
	 * @return number of Data Output Associations
	 */
	public int getDataOutputAssociations() {
		if(this.extraction.equals("Process")) {
			int DataObjectsOutput = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				DataObjectsOutput += ((Activity) a).getDataOutputAssociations().size();
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				DataObjectsOutput += ((CatchEvent) e).getDataOutputAssociations().size();
			return DataObjectsOutput;	
		}
		return getNumberOfTypeElement(DataOutputAssociation.class);
	}
	
	/**
	 * Metric: NDSTA
	 * 
	 * @return number of Data States
	 */
	public int getDataStates() {
		if(this.extraction.equals("Process")) {
			int ds = 0;
			for(ModelElementInstance iae : this.getCollectionOfElementType(ItemAwareElement.class))
				if(((ItemAwareElement) iae).getDataState() != null)
					ds++;
			return ds;
		}	
		return getNumberOfTypeElement(DataState.class);
	}
	
	/**
	 * Metric: NDSTO
	 * 
	 * @return number of Data Stores
	 */
	public int getDataStores() {
		if(this.extraction.equals("Process")) {
			int DataStore = 0;
			for(ModelElementInstance ds: this.getCollectionOfElementType(DataStoreReference.class)) 
				if(((DataStoreReference) ds).getDataStore() != null)
					DataStore++;
			return DataStore;	
		}
		return getNumberOfTypeElement(DataStore.class);
	}
	
	/**
	 * Metric: NDEF
	 * 
	 * @return number of Definitions
	 */
	public int getDefinitions() {
		return getNumberOfTypeElement(Definitions.class);
	}
	
	/**
	 * Metric: NDOC
	 * 
	 * @return number of Documentations
	 */
	public int getDocumentations() {
		if(this.extraction.equals("Process")) {
			int d = 0;
			for(ModelElementInstance be : this.getCollectionOfElementType(BaseElement.class))
				d+= ((BaseElement) be).getDocumentations().size();
			return d;
		}
		return getNumberOfTypeElement(Documentation.class);
	}
	
	/**
	 * Metric: TNIE
	 * Total number of Intermediate Events
	 * Number of Intermediate None Events  + Number of Intermediate Timer Events + Number of Intermediate Message Events + Number of Intermediate Error Events + 
	 *     + Number of Intermediate Cancel Events + Number of Intermediate Compensation Event + Number of Intermediate Rule Events +
	 *     + Number of Intermediate Link Events + Number of Intermediate Multiple Events
	 * (TNIE = NINE + NITE + NIMsE + NIEE + NICaE + NICoE + NIRE + NILE + NIMuE)
	 * @return
	 */
	public int getTotalNumberOfIntermediateEvents() {
		return getEvents() - getStartEvents() - getEndEvents();
	}
	
	/**
	 * Metric: NENDEV
	 * 
	 * @return number of End Events
	 */
	public int getEndEvents() {
		return getNumberOfTypeElement(EndEvent.class);
	}
	
	/**
	 * Metric: NENDMEV
	 * 
	 * @return number of End Message Events
	 */
	public int getEndMessageEvents() {
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.MessageEventDefinitionImpl");

	}
	
	/**
	 * Metric: NENDESCEV
	 * 
	 * @return number of End Escalation Events
	 */
	public int getEndEscalationEvents(){
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.EscalationEventDefinitionImpl");

	}
	
	/**
	 * Metric: NENDSIGEV
	 * 
	 * @return number of End Signal Events
	 */
	public int getEndSignalEvents(){
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.SignalEventDefinitionImpl");

	}
	
	/**
	 * Metric: NENDERREV
	 * 
	 * @return number of End Error Events
	 */
	public int getEndErrorEvents(){
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.ErrorEventDefinitionImpl");

	}
	
	/**
	 * Metric: NENDCEV
	 * 
	 * @return number of End Cancel Events
	 */
	public int getEndCancelEvents(){
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.CancelEventDefinitionImpl");

	}
	
	/**
	 * Metric: NENDCOMEV
	 * 
	 * @return number of End Compensation Events
	 */
	public int getEndCompensationEvents(){
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.CompensateEventDefinitionImpl");

	}
	
	/**
	 * Metric: NTEEV
	 * 
	 * @return number of Terminate Events
	 */
	public int getEndTerminateEvents(){
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.TerminateEventDefinitionImpl");
	}
	
	/**
	 * Metric: NENDMUEV
	 * @return number of End Multiple Events
	 */
	public int getEndMultipleEvents() {
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getThrowEventsMultipleDefinitions(events, "org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl");
	}
	
	/**
	 * Metric: NENDP
	 * 
	 * @return number of End Points
	 */
	public int getEndPoints() {
		return getNumberOfTypeElement(EndPoint.class);
	}
	
	/**
	 * Metric: NERR
	 * 
	 * @return number of Errors 
	 */
	/*public int getErrors() {
		return getNumberOfTypeElement(Error.class);
	}*/
	
	/**
	 * Metric: NERREV
	 * 
	 * @return number of Error Events
	 */
	public int getErrorEvents() {
		if(this.extraction.equals("Process")) {
			int ee = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof ErrorEventDefinition)
						ee++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof ErrorEventDefinition)
						ee++;
			return ee;
		}
		return getNumberOfTypeElement(ErrorEventDefinition.class);
	}
	
	/**
	 * Metric: NESC
	 * 
	 * @return number of Escalations 
	 */
	/*public int getEscalations() {
		return getNumberOfTypeElement(Escalation.class);
	}*/
	
	/**
	 * Metric: NESCEV
	 * 
	 * @return number of Escalation Events
	 */
	public int getEscalationEvents() {
		if(this.extraction.equals("Process")) {
			int ee = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof EscalationEventDefinition)
						ee++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof EscalationEventDefinition)
						ee++;
			return ee;
		}
		return getNumberOfTypeElement(EscalationEventDefinition.class);
	}
	
	/**
	 * Metric: NEV
	 * 
	 * @return number of Events
	 */
	public int getEvents() {
		return getNumberOfTypeElement(Event.class);
	}
	
	/**
	 * Metric: NEVDEF
	 * 
	 * @return number of Event Definitions
	 */
	public int getEventDefinitions() {
		if(this.extraction.equals("Process")) {
			int ed = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				ed += ((ThrowEvent) e).getEventDefinitions().size();
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				ed += ((CatchEvent) e).getEventDefinitions().size();
			return ed;
		}
		return getNumberOfTypeElement(EventDefinition.class);
	}
	
	/**TODO
	 * Metric: NEXP
	 * 
	 * @return number of Expressions
	 */
	public int getExpressions() {
		return getNumberOfTypeElement(Expression.class);
	}
	
	/**
	 * Metric: NEXT
	 * 
	 * @return number of Extensions
	 */
	public int getExtensions() {
		return getNumberOfTypeElement(Extension.class);
	}
	
	/**
	 * Metric: NEXTEL
	 * 
	 * @return number of Extension Elements 
	 */
	public int getExtensionElements() {
		if(this.extraction.equals("Process")) {
			int e = 0;
			for(ModelElementInstance be : this.getCollectionOfElementType(BaseElement.class))
				if(((BaseElement) be).getExtensionElements() != null)
				e++;
			return e;
		}
		return getNumberOfTypeElement(ExtensionElements.class);
	}
	
	/**
	 * Metric: NFLEL
	 * 
	 * @return number of Flow Elements
	 */
	public int getFlowElements() {
		return getNumberOfTypeElement(FlowElement.class);
	}
	
	/**
	 * Metric: NFLNO
	 * 
	 * @return number of Flow Nodes
	 */
	public int getFlowNodes() {
		return getNumberOfTypeElement(FlowNode.class);
	}
	
	/**TODO
	 * Metric: NFOREXP
	 * 
	 * @return number of Formal Expressions
	 */
	public int getFormalExpressions() {
		return getNumberOfTypeElement(FormalExpression.class);
	}
	
	/**
	 * Metric: NGA
	 * 
	 * @return number of Gateway
	 */
	public int getGateways() {
		return getNumberOfTypeElement(Gateway.class);
	}
	
	/**
	 * Metric: NGC
	 * 
	 * @return number of Global Conversations
	 */
	/*public int getGlobalConversations() {
		return getNumberOfTypeElement(GlobalConversation.class);
	}*/
	
	/**
	 * Metric: NHP
	 * 
	 * @return number of Human Performers
	 */
	/*public int getHumanPerformers() {
		return getNumberOfTypeElement(HumanPerformer.class);
	}*/
	
	/**
	 * Metric: NIMP
	 * 
	 * @return number of Import
	 */
	public int getImports() {
		return getNumberOfTypeElement(Import.class);
	}
	
	/**
	 * Metric: NInDI
	 * 
	 * @return number of Input Data Items
	 */
	public int getInputDataItems() {
		if(this.extraction.equals("Process")) {
			int idi = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) {
				if(((Activity)a).getLoopCharacteristics() instanceof MultiInstanceLoopCharacteristics)
					if(((MultiInstanceLoopCharacteristics) ((Activity)a).getLoopCharacteristics()).getInputDataItem() != null)
						idi++;
				}
			return idi;
		}
		return getNumberOfTypeElement(InputDataItem.class);
	}
	
	/**
	 * Metric: NInS
	 * 
	 * @return number of Input Sets
	 */
	public int getInputSets() {
		if(this.extraction.equals("Process")) {
			int is = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(ThrowEvent.class))
				if(((ThrowEvent) a).getInputSet() != null)
				is++;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class))
				if(((Activity) a).getIoSpecification() != null)
					is+=((Activity) a).getIoSpecification().getInputSets().size();
			return is;
		}
		return getNumberOfTypeElement(InputSet.class);
	}
	
	/**
	 * Metric: NINTNO
	 * 
	 * @return number of Interaction Nodes
	 */
	public int getInteractionNodes() {
		return getNumberOfTypeElement(InteractionNode.class);
	}
	
	/**
	 * Metric: NINTE
	 * 
	 * @return number of Interfaces
	 */
	public int getInterfaces() {
		return getNumberOfTypeElement(Interface.class);
	}
	
	/**
	 * Metric: NICEV
	 * 
	 * @return number of Intermediate Catch Events
	 */
	public int getIntermediateCatchEvents() {
		return getNumberOfTypeElement(IntermediateCatchEvent.class);
	}
	
	/**
	 * Metric: NIMCEV
	 * 
	 * @return number degli Intermediate Message Catch Events
	 */
	public int getIntermediateMessageCatchEvents(){
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateCatchEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.MessageEventDefinitionImpl");

	}
	
	/**
	 * Metric: NILCEV
	 * 
	 * @return number degli Intermediate Link Catch Events
	 */	
	public int getIntermediateLinkCatchEvents(){
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateCatchEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.LinkEventDefinitionImpl");

	}
	
	/**
	 * Metric: NISIGCEV
	 * 
	 * @return number degli Intermediate Signal Catch Events
	 */
	public int getIntermediateSignalCatchEvents(){
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateCatchEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.SignalEventDefinitionImpl");

	}
	
	/**
	 * Metric: NICONCEV
	 * 
	 * @return number degli Intermediate Conditional Catch Events
	 */
	public int getIntermediateConditionalCatchEvents(){
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateCatchEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.ConditionalEventDefinitionImpl");

	}
	
	/**
	 * Metric: NIMUCEV
	 * @return the number of Intermediate Multiple Catch Events
	 */
	public int getIntermediateMultipleCatchEvents() {
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getCatchEventsMultipleDefinitions(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateCatchEventImpl");
	}
	
	/**
	 * Metric: NIPMUCEV
	 * @return the number of Intermediate Parallel Multiple Catch Events
	 */
	public int getIntermediateParallelMultipleCatchEvents() {
		int toReturn = 0;
		Collection<IntermediateCatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(IntermediateCatchEvent.class);
		else {
			events = this.process.getChildElementsByType(IntermediateCatchEvent.class);
			ec.getInterEventsSubProcess(events, process);
		}
		for (IntermediateCatchEvent event: events) {
			if (event.isParallelMultiple()) {
				toReturn++;
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Metric: NITEV
	 * 
	 * @return number of Intermediate Throw Events
	 */
	public int getIntermediateThrowEvents() {
		return getNumberOfTypeElement(IntermediateThrowEvent.class);
	}
	
	/**
	 * Metric: NIMTEV
	 * 
	 * @return number degli Intermediate Message Throw Events
	 */
	
	public int getIntermediateMessageThrowEvents() {
		Collection<ThrowEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		else {
			events = this.process.getChildElementsByType(ThrowEvent.class);
			ec.getThrowEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateThrowEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.MessageEventDefinitionImpl");
	}
	
	/**
	 * Metric: NISIGTEV
	 * 
	 * @return number degli Intermediate Signal Throw Events
	 */
	 public int getIntermediateSignalThrowEvents() {
		 Collection<ThrowEvent> events;
		 if(this.extraction.equals("Model"))
			 events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		 else {
			 events = this.process.getChildElementsByType(ThrowEvent.class);
			 ec.getThrowEventsSubProcess(events, process);
		 }
		 return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateThrowEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.SignalEventDefinitionImpl");
	 }
	 
	 /**
	  * Metric: NIESCTEV
	  * 
	  * @return number degli Intermediate Escalation Throw Events
	  */
	 public int getIntermediateEscalationThrowEvents() {
		 Collection<ThrowEvent> events;
		 if(this.extraction.equals("Model"))
			 events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		 else {
			 events = this.process.getChildElementsByType(ThrowEvent.class);
			 ec.getThrowEventsSubProcess(events, process);
		 }
		 return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateThrowEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.EscalationEventDefinitionImpl");
	 }
	 
	 /**
	  * Metric: NILTEV
	  * 
	  * @return number degli Intermediate Link Throw Events
	  */
	 public int getIntermediateLinkThrowEvents() {
		 Collection<ThrowEvent> events;
		 if(this.extraction.equals("Model"))
			 events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		 else {
			 events = this.process.getChildElementsByType(ThrowEvent.class);
			 ec.getThrowEventsSubProcess(events, process);
		 }
		 return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateThrowEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.LinkEventDefinitionImpl");
	 }
	 
	 /**
	  * Metric: NICOMTEV
	  * 
	  * @return number degli Intermediate Compensation Throw Events
	  */
	  public int getIntermediateCompensationThrowEvents() {
		  Collection<ThrowEvent> events;
			 if(this.extraction.equals("Model"))
				 events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
			 else {
				 events = this.process.getChildElementsByType(ThrowEvent.class);
				 ec.getThrowEventsSubProcess(events, process);
			 }
		 return this.getNumberOfEventDefinitionsOfThrowEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateThrowEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.CompensateEventDefinitionImpl");
	 }
	 
	 /**
	  * Metric: NIMUTEV
	  * @return number of Intermediate Multiple Throw Events
	  */
	 public int getIntermediateMultipleThrowEvents(){
		 Collection<ThrowEvent> events;
		 if(this.extraction.equals("Model"))
			 events = this.modelInstance.getModelElementsByType(ThrowEvent.class);
		 else {
			 events = this.process.getChildElementsByType(ThrowEvent.class);
			 ec.getThrowEventsSubProcess(events, process);
		 }
		 return this.getThrowEventsMultipleDefinitions(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateThrowEventImpl");
	 }
	 
	 /**
	 * Metric: NIOB
	 * 
	 * @return number degli Io Bindings
	 */
	public int getIoBindings() {
		return getNumberOfTypeElement(IoBinding.class);
	}
	
	/**
	 * Metric: NIOS
	 * 
	 * @return number of Io Specifications
	 */
	public int getIoSpecifications() {
		if(this.extraction.equals("Process")) {
			int io = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class))
				if(((Activity) a).getIoSpecification() != null)
					io++;
			return io;
		}
		return getNumberOfTypeElement(IoSpecification.class);
	}
	
	/**
	 * Metric: NIAEL
	 * 
	 * @return number of Item Aware Elements
	 */
	public int getItemAwareElements() {
		return getNumberOfTypeElement(ItemAwareElement.class);
	}
	
	/**
	 * Metric: NIDEF
	 * 
	 * @return number of Item Definitions
	 */
	/*public int getItemDefinitions() {
		return getNumberOfTypeElement(ItemDefinition.class);
	}*/
	
	/**
	 * Metric: NLEV
	 * 
	 * @return number of Link Event Definitions
	 */
	public int getLinkEvents() {
		if(this.extraction.equals("Process")) {
			int le = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof LinkEventDefinition)
						le++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof LinkEventDefinition)
						le++;
			return le;
		}
		return getNumberOfTypeElement(LinkEventDefinition.class);
	}
	
	/**
	 * Metric: NLOOPCA
	 * 
	 * @return number of Loop Cardinalities
	 */
	public int getLoopCardinalities() {
		if(this.extraction.equals("Process")) {
			int multi = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) {
				if(((Activity)a).getLoopCharacteristics() instanceof MultiInstanceLoopCharacteristics)
					if(((MultiInstanceLoopCharacteristics) ((Activity)a).getLoopCharacteristics()).getLoopCardinality() != null)
						multi++;
				}
			return multi;
		}
		return getNumberOfTypeElement(LoopCardinality.class);
	}
	
	/**
	 * Metric: NLOOPCH
	 * 
	 * @return number of Task with Loop Characteristics
	 */
	public int getLoopCharacteristics() {
		if(this.extraction.equals("Process")) {
			int standard = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) {
				if(((Activity)a).getLoopCharacteristics() instanceof LoopCharacteristics)
					standard++;
			}
			return standard;
		}
		return getNumberOfTypeElement(LoopCharacteristics.class);
	}
	
	/**
	 * Metric: NMT
	 * 
	 * @return number of Manual Tasks
	 */
	public int getManualTasks() {
		return getNumberOfTypeElement(ManualTask.class);
	}
	
	/**
	 * Metric: NMES
	 * 
	 * @return number of Messages
	 */
	/*public int getMessages() {
		return getNumberOfTypeElement(Message.class);
	}*/
	
	/**
	 * Metric: NMESEV
	 * 
	 * @return number of Message Event Definition
	 */
	public int getMessageEvents() {
		if(this.extraction.equals("Process")) {
			int me = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof MessageEventDefinition)
						me++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof MessageEventDefinition)
						me++;
			return me;
		}
		return getNumberOfTypeElement(MessageEventDefinition.class);
	}
	
	/**
	 * Metric: NMESFA
	 * 
	 * @return number of Message Flow Associations
	 */
	public int getMessageFlowAssociations() {
		return getNumberOfTypeElement(MessageFlowAssociation.class);
	}
	
	/**
	 * Metric: NMON
	 * 
	 * @return number of Monitorings 
	 */
	public int getMonitorings() {
		if(this.extraction.equals("Process")) {
			int a = 0;
			if(this.process.getMonitoring() != null)
				  a++;
			for(ModelElementInstance fe : this.getCollectionOfElementType(FlowElement.class))
				if(((FlowElement) fe).getMonitoring() != null)
				  a++;
			return a;
		}
		return getNumberOfTypeElement(Monitoring.class);
	}
	
	/**
	 * Metric: NMILCH
	 * 
	 * @return number of tasks whit Multi Instance Loop Characteristics
	 */
	public int getMultiInstanceLoopCharacteristics() {
		if(this.extraction.equals("Process")) {
			int multi = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) {
				if(((Activity)a).getLoopCharacteristics() instanceof MultiInstanceLoopCharacteristics)
					multi++;
			}
			return multi;
		}
		return getNumberOfTypeElement(MultiInstanceLoopCharacteristics.class);
	}
	
	/**
	 * Metric: NOP
	 * 
	 * @return number of Operazioni
	 */
	public int getOperations() {
		return getNumberOfTypeElement(Operation.class);
	}
	
	/**
	 * Metric: NOutDI
	 * 
	 * @return number of Output Data Items
	 */
	public int getOutputDataItems() {
		if(this.extraction.equals("Process")) {
			int odi = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) {
				if(((Activity)a).getLoopCharacteristics() instanceof MultiInstanceLoopCharacteristics)
					if(((MultiInstanceLoopCharacteristics) ((Activity)a).getLoopCharacteristics()).getOutputDataItem() != null)
						odi++;
				}
			return odi;
		}
		return getNumberOfTypeElement(OutputDataItem.class);
	}
	
	/**
	 * Metric: NOutS
	 * 
	 * @return number of Output Sets
	 */
	public int getOutputSets() {
		if(this.extraction.equals("Process")) {
			int os = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(CatchEvent.class))
				if(((CatchEvent) a).getOutputSet() != null)
				os++;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class))
				if(((Activity) a).getIoSpecification() != null)
					os+=((Activity) a).getIoSpecification().getOutputSets().size();
			return os;
		}
		return getNumberOfTypeElement(OutputSet.class);
	}
	
	/**
	 * Metric: NPG
	 * 
	 * @return number of Parallel Gateways
	 */
	public int getParallelGateways() {
		return getNumberOfTypeElement(ParallelGateway.class);
	}
	
	/**
	 * Metric: NPAS
	 * 
	 * @return number of Participant Associations
	 */
	public int getParticipantAssociations() {
		return getNumberOfTypeElement(ParticipantAssociation.class);
	}
	
	/**
	 * Metric: NPM
	 * 
	 * @return number of Participant Multiplicities
	 */
	public int getParticipantMultiplicities() {
		return getNumberOfTypeElement(ParticipantMultiplicity.class);
	}
	
	/**
	 * Metric: NPER
	 * 
	 * @return number of Performers 
	 */
	/*public int getPerformers() {
		return getNumberOfTypeElement(Performer.class);
	}*/
	
	/**
	 * Metric: NPO
	 * 
	 * @return number of Potential Owners
	 */
	/*public int getPotentialOwners() {
		return getNumberOfTypeElement(PotentialOwner.class);
	}*/
	
	/**
	 * Metric: NPROC
	 * 
	 * @return number of Processes
	 */
	public int getProcesses() {
		return getNumberOfTypeElement(Process.class);
	}
	
	/**
	 * Metric: NPROP
	 * 
	 * @return number of Proprerties
	 */
	public int getProperties() {
		if(this.extraction.equals("Process")) {
			int p = this.process.getProperties().size();
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				p+= ((Activity) a).getProperties().size();
			for(ModelElementInstance a: this.getCollectionOfElementType(Event.class)) 
				p+= ((Event) a).getProperties().size();
			return p;
			}
		return getNumberOfTypeElement(Property.class);
	}
	
	/**
	 * Metric: NRT
	 * 
	 * @return number of Receive Tasks
	 */
	public int getReceiveTasks() {
		return getNumberOfTypeElement(ReceiveTask.class);
	}
	
	/**
	 * Metric: NREL
	 * 
	 * @return number of Relationships
	 */
	public int getRelationships() {
		return getNumberOfTypeElement(Relationship.class);
	}
	
	/**
	 * Metric: NREN
	 * 
	 * @return number of Renderings
	 */
	public int getRenderings() {
		if(this.extraction.equals("Process")) {
			int r = 0;
			for(ModelElementInstance a: this.getCollectionOfElementType(UserTask.class)) 
				r+= ((UserTask) a).getRenderings().size();
			return r;
		}
		return getNumberOfTypeElement(Rendering.class);
	}
	
	/**
	 * Metric: NRES
	 * 
	 * @return number of Resources
	 */
	public int getResources() {
		if(this.extraction.equals("Process")) {
			int r = 0;
			for(ResourceRole rr : this.process.getResourceRoles())
				if(rr.getResource() != null)
					r++;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				for(ResourceRole rr : ((Activity) a).getResourceRoles())
					if(rr.getResource() != null)
						r++;
			return r;
			}
		return getNumberOfTypeElement(Resource.class);
	}
	
	/**
	 * Metric: NRESAEX
	 * 
	 * @return number of Resource Assignment Expressions
	 */
	public int getResourceAssignmentExpressions() {
		if(this.extraction.equals("Process")) {
			int rae = 0;
			for(ResourceRole r : this.process.getResourceRoles())
				if(r.getResourceAssignmentExpression() != null)
					rae++;
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				for(ResourceRole r : ((Activity) a).getResourceRoles())
					if(r.getResourceAssignmentExpression() != null)
						rae++;
			return rae;
			}
		return getNumberOfTypeElement(ResourceAssignmentExpression.class);
	}
	
	/**
	 * Metric: NRESP
	 * 
	 * @return number of Resource Parameters
	 */
	/*public int getResourceParameters() {
		return getNumberOfTypeElement(ResourceParameter.class);
	}*/
	
	/**
	 * Metric: NRESPB
	 * 
	 * @return number of Resource Parameter Bindings
	 */
	public int getResourceParameterBindings() {
		if(this.extraction.equals("Process")) {
			int rpb = 0;
			for(ResourceRole r : this.process.getResourceRoles())
				rpb+=r.getResourceParameterBinding().size();
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				for(ResourceRole r : ((Activity) a).getResourceRoles())
					rpb+=r.getResourceParameterBinding().size();
			return rpb;
			}
		return getNumberOfTypeElement(ResourceParameterBinding.class);
	}
	
	/**
	 * Metric: NRESR
	 * 
	 * @return number of Resource Roles
	 */
	public int getResourceRoles() {
		if(this.extraction.equals("Process")) {
			int rr = this.process.getResourceRoles().size();
			for(ModelElementInstance a: this.getCollectionOfElementType(Activity.class)) 
				rr+= ((Activity) a).getResourceRoles().size();
			return rr;
			}
		return getNumberOfTypeElement(ResourceRole.class);
	}
	
	/**
	 * Metric: NRE
	 * 
	 * @return number of Root Elements
	 */
	public int getRootElements() {
		return getNumberOfTypeElement(RootElement.class);
	}
	
	/**
	 * Metric: NSC
	 * 
	 * @return number of Scripts
	 */
	/*public int getScripts() {
		return getNumberOfTypeElement(Script.class);
	}*/
	
	/**
	 * Metric: NSCT
	 * 
	 * @return number of Script Tasks
	 */
	public int getScriptTasks() {
		return getNumberOfTypeElement(ScriptTask.class);
	}
	
	/**
	 * Metric: NSENT
	 * 
	 * @return number of Send Tasks 
	 */
	public int getSendTasks() {
		return getNumberOfTypeElement(SendTask.class);
	}
	
	/**
	 * Metric: NSEQF
	 * 
	 * @return number of Sequence Flows
	 */
	public int getSequenceFlows() {
		return getNumberOfTypeElement(SequenceFlow.class);
	}
	
	/**
	 * Metric: NSERT
	 * 
	 * @return number of Service Tasks
	 */
	public int getServiceTasks() {
		return getNumberOfTypeElement(ServiceTask.class);
	}
	
	/**
	 * Metric: NSI
	 * 
	 * @return number of Signals
	 */
	/*public int getSignals() {
		return getNumberOfTypeElement(Signal.class);
	}*/
	
	/**
	 * Metric: NSIEV
	 * 
	 * @return number of Signal Events
	 */
	public int getSignalEvent() {
		if(this.extraction.equals("Process")) {
			int se = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof SignalEventDefinition)
						se++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof SignalEventDefinition)
						se++;
			return se;
		}
		return getNumberOfTypeElement(SignalEventDefinition.class);
	}
	
	/**
	 * Metric: NSTEV
	 * 
	 * @return number of Start Events
	 */
	public int getStartEvents() {
		return getNumberOfTypeElement(StartEvent.class);
	}
	
	/**
	 * Metric: NSTMEV
	 * 
	 * @return number of Start Message Events
	 */
	public int getStartMessageEvents() {
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.StartEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.MessageEventDefinitionImpl");
	}
	
	/**
	 * Metric: NSTTEV
	 * 
	 * @return number of Start Timer Events
	 */
	public int getStartTimerEvents() {
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.StartEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.TimerEventDefinitionImpl");		
	}
	
	/**
	 * Metric: NSTCOEV
	 * 
	 * @return number of Start Conditional Events
	 */
	public int getStartConditionalEvents(){
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.StartEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.ConditionalEventDefinitionImpl");

	}
	
	/**
	 * Metric: NSTSIGEV
	 * 
	 * @return number of Start Signal Events
	 */
	public int getStartSignalEvents(){
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.StartEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.SignalEventDefinitionImpl");

	}
	
	/**
	 * Metric: NSTMUEV
	 * @return the number of Start Multiple Events
	 */
	public int getStartMultipleEvents() {
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getCatchEventsMultipleDefinitions(events, "org.camunda.bpm.model.bpmn.impl.instance.StartEventImpl");
	}
	
	/**
	 * Metric: NSTPMUEV
	 * @return the number of Start Parallel Multiple Events
	 */
	public int getStartParallelMultipleEvents() {
		int toReturn = 0;
		Collection<StartEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(StartEvent.class);
		else {
			events = this.process.getChildElementsByType(StartEvent.class);
			ec.getStartEventsSubProcess(events, process);
		}
		for (StartEvent event: events) {
			if (event.isParallelMultiple()) {
				toReturn++;
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Metric: NSUB
	 * 
	 * @return number of Sub-Processes
	 */
	public int getSubprocesses() {
		return getNumberOfTypeElement(SubProcess.class);
	}
	
	/**
	 * Metric: NCSUB
	 * 
	 * @return number of collapsed Sub-Processes
	 */
	public int getCollapsedSubprocesses() {
		int ncsub = 0;
		for(ModelElementInstance sub : getCollectionOfElementType(SubProcess.class))
			if(((SubProcess) sub).getFlowElements().size()==0)
				ncsub++;
		return ncsub;
	}
	
	/**
	 * Metric: NESUB
	 * 
	 * @return number of Event Sub-Processes
	 */
	public int getEventSubprocesses() {
		int nesub = 0;
		for(ModelElementInstance sub : getCollectionOfElementType(SubProcess.class))
			if(((SubProcess) sub).triggeredByEvent())
				nesub++;
		return nesub;
	}
	
	/**
	 * Metric: NCESUB
	 * 
	 * @return number of Collapsed Event Sub-Processes
	 */
	public int getCollapsedEventSubprocesses() {
		int nesub = 0;
		for(ModelElementInstance sub : getCollectionOfElementType(SubProcess.class))
			if(((SubProcess) sub).triggeredByEvent())
				if(((SubProcess) sub).getFlowElements().size()==0)
					nesub++;
		return nesub;
	}
	
	/**
	 * Metric: NSC
	 * 
	 * @return number of Sub Conversations
	 */
	/*public int getSubConversations() {
		return getNumberOfTypeElement(SubConversation.class);
	}*/
	
	/**
	 * Metric: NTEX
	 * 
	 * @return number of Texts 
	 */
	public int getTexts() {
		if(this.extraction.equals("Process")) {
			int t = 0;
			for(ModelElementInstance te : this.getCollectionOfElementType(TextAnnotation.class))
				if(((TextAnnotation) te).getText() != null)
					t++;
			return t;
		}
		return getNumberOfTypeElement(Text.class);
	}
	
	/**
	 * Metric: NTEXA
	 * 
	 * @return number of Text Annotations
	 */
	public int getTextAnnotations() {
		return getNumberOfTypeElement(TextAnnotation.class);
	}
	
	/**
	 * Metric: NG
	 * 
	 * @return number of Groups
	 */
	public int getGroups() {
		return getNumberOfTypeElement(Group.class);
	}
	
	/**
	 * Metric: NTHEV
	 * 
	 * @return number of Throw Events
	 */
	public int getThrowEvents() {
		return getNumberOfTypeElement(ThrowEvent.class);
	}
	
	/**
	 * Metric: NTC
	 * 
	 * @return number of Time Cycles
	 */
	public int getTimeCycles() {
		if(this.extraction.equals("Process")) {
			int te = 0;
			/*for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						if(((TimerEventDefinition) ed).getTimeCycle() != null)
						te++;*/
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						if(((TimerEventDefinition) ed).getTimeCycle() != null)
							te++;
			return te;
		}
		return getNumberOfTypeElement(TimeCycle.class);
	}
	
	/**
	 * Metric: NTDA
	 * 
	 * @return number of Time Dates
	 */
	public int getTimeDates() {
		if(this.extraction.equals("Process")) {
			int te = 0;
			/*for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						if(((TimerEventDefinition) ed).getTimeDate() != null)
						te++;*/
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						if(((TimerEventDefinition) ed).getTimeDate() != null)
							te++;
			return te;
		}
		return getNumberOfTypeElement(TimeDate.class);
	}
	
	/**
	 * Metric: NTDU
	 * 
	 * @return number of Time Durations
	 */
	public int getTimeDurations() {
		if(this.extraction.equals("Process")) {
			int te = 0;
			/*for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						if(((TimerEventDefinition) ed).getTimeDuration() != null)
						te++;*/
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						if(((TimerEventDefinition) ed).getTimeDuration() != null)
							te++;
			return te;
		}
		return getNumberOfTypeElement(TimeDuration.class);
	}
	
	/**
	 * Metric: NTEVD
	 * 
	 * @return number of Timer Event Definitions
	 */
	public int getTimerEventDefinitions() {
		if(this.extraction.equals("Process")) {
			int te = 0;
			for(ModelElementInstance e: this.getCollectionOfElementType(ThrowEvent.class)) 
				for(EventDefinition ed : ((ThrowEvent) e).getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						te++;
			for(ModelElementInstance e: this.getCollectionOfElementType(CatchEvent.class)) 
				for(EventDefinition ed : ((CatchEvent) e).getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						te++;
			return te;
		}
		return getNumberOfTypeElement(TimerEventDefinition.class);
	}

	/**
	 * Metric: NTR
	 * 
	 * @return number of Transactions
	 */
	public int getTransactions() {
		return getNumberOfTypeElement(Transaction.class);
	}
	
	/**
	 * Metric: NUT
	 * 
	 * @return number of User Tasks
	 */
	public int getUserTasks() {
		return getNumberOfTypeElement(UserTask.class);
	}
	
	/**
	 * Metric: NFDCG
	 * 
	 * @return number of flow dividing complex gateways 
	 */
	public int getFlowDividingComplexGateways() {
		return getFlowDividingElementsOfType(ComplexGateway.class);
	}
	
	/**
	 * Metric: NFDEBG
	 * 
	 * @return number of flow dividing event based gateways 
	 */
	public int getFlowDividingEventBasedGateways() {
		return getFlowDividingElementsOfType(EventBasedGateway.class);
	}
	
	/**
	 * Metric: NFDEXG
	 * 
	 * @return number of flow dividing exclusive gateways
	 */
	public int getFlowDividingExclusiveGateways() {
		return getFlowDividingElementsOfType(ExclusiveGateway.class);
	}
	
	/**
	 * Metric: NFDIG
	 * 
	 * @return number of flow dividing inclusive gateways 
	 */
	public int getFlowDividingInclusiveGateways() {
		return getFlowDividingElementsOfType(InclusiveGateway.class);
	}
	
	/**
	 * Metric: NFDPG
	 * 
	 * @return number of flow dividing parallel gateways 
	 */
	public int getFlowDividingParallelGateways() {
		return getFlowDividingElementsOfType(ParallelGateway.class);
	}
	
	/**
	 * Metric: NFDT
	 * 
	 * @return number of flow dividing tasks 
	 */
	public int getFlowDividingTasks() {
		return getFlowDividingElementsOfType(Task.class);
	}
	
	/**
	 * Metric: NFJCG
	 * 
	 * @return number of flow joining complex gateways
	 */
	public int getFlowJoiningComplexGateways() {
		return getFlowJoiningElementsOfType(ComplexGateway.class);
	}
	
	/**
	 * Metric: NFJEBG
	 * 
	 * @return number of flow joining event based gateways 
	 */
	public int getFlowJoiningEventBasedGateways() {
		return getFlowJoiningElementsOfType(EventBasedGateway.class);
	}
	
	/**
	 * Metric: NFJEXG
	 * 
	 * @return number of flow joining exclusive gateways 
	 */
	public int getFlowJoiningExclusiveGateways() {
		return getFlowJoiningElementsOfType(ExclusiveGateway.class);
	}
	
	/**
	 * Metric: NFJIG
	 * 
	 * @return number of flow joining inclusive gateways 
	 */
	public int getFlowJoiningInclusiveGateways() {
		return getFlowJoiningElementsOfType(InclusiveGateway.class);
	}
	
	/**
	 * Metric: NFJPG
	 * 
	 * @return number of flow joining parallel gateways 
	 */
	public int getFlowJoiningParallelGateways() {
		return getFlowJoiningElementsOfType(ParallelGateway.class);
	}
	
	/**
	 * Metric: NFJT
	 * 
	 * @return number of flow joining tasks 
	 */
	public int getFlowJoiningTasks() {
		return getFlowJoiningElementsOfType(Task.class);
	}
	
	/**
	 * Metric: NFJDCG
	 * 
	 * @return number of joining and dividing complex gateways  
	 */
	public int getFlowJoiningAndDividingComplexGateways() {
		return getFlowJoiningAndDividingElementsOfType(ComplexGateway.class);
	}
	
	/**
	 * Metric: NFJDEBG
	 * 
	 * @return number of joining and dividing event based gateways  
	 */
	public int getFlowJoiningAndDividingEventBasedGateways() {
		return getFlowJoiningAndDividingElementsOfType(EventBasedGateway.class);
	}
	
	/**
	 * Metric: NFJDEXG
	 * 
	 * @return number of joining and dividing exclusive gateways  
	 */
	public int getFlowJoiningAndDividingExclusiveGateways() {
		return getFlowJoiningAndDividingElementsOfType(ExclusiveGateway.class);
	}
	
	/**
	 * Metric: NFJDIG
	 * 
	 * @return number of joining and dividing inclusive gateways  
	 */
	public int getFlowJoiningAndDividingInclusiveGateways() {
		return getFlowJoiningAndDividingElementsOfType(InclusiveGateway.class);
	}
	
	/**
	 * Metric: NFJDPG
	 * 
	 * @return number of joining and dividing parallel gateways  
	 */
	public int getFlowJoiningAndDividingParallelGateways() {
		return getFlowJoiningAndDividingElementsOfType(ParallelGateway.class);
	}
	
	/**
	 * Metric: NFJDT
	 * 
	 * @return number of joining and dividing tasks  
	 */
	public int getFlowJoiningAndDividingTasks() {
		return getFlowJoiningAndDividingElementsOfType(Task.class);
	}
	
	/**
	 * Metric: NFDG
	 * 
	 * @return number of dividing gateways
	 */
	public int getFlowDividingGateways() {
		return getFlowDividingElementsOfType(Gateway.class);
	}
	
	/**
	 * Metric: NFJT
	 *  
	 * @return number of joining gateways
	 */
	public int getFlowJoiningGateways() {
		return getFlowJoiningElementsOfType(Gateway.class);
	}
	
	/**
	 * Metric: NFJDG
	 * 
	 * @return number of gateways  
	 */
	public int getFlowJoiningAndDividingGateways() {
		return getFlowJoiningAndDividingElementsOfType(Gateway.class);
	}
	
	/**
	 * Metodo che cerca tutti gli elementi della classe type presenti nel modello e conta quanti dividono flusso
	 * @param type: classe del tipo of elemento che si vuole analizzare
	 * @return number of elementi della classe type che dividono flusso
	 */
	@SuppressWarnings("rawtypes")
	private int getFlowDividingElementsOfType(Class type) {
		int toReturn = 0;
		Collection<ModelElementInstance> modelElementInstances = getCollectionOfElementType(type);
		for (ModelElementInstance instance : modelElementInstances) {
			if (((FlowNode) instance).getOutgoing().size() > 1) {
				toReturn += 1;
			}
		}
		return toReturn;
	}
	
	/**
	 * Metodo che cerca tutti gli elementi della classe type presenti nel modello e conta quanti uniscono flusso
	 * @param type: classe del tipo of elemento che si vuole analizzare
	 * @return number of elementi della classe type che uniscono flusso
	 */
	@SuppressWarnings("rawtypes")
	private int getFlowJoiningElementsOfType(Class type) {
		int toReturn = 0;
		Collection<ModelElementInstance> modelElementInstances = getCollectionOfElementType(type);
		for (ModelElementInstance instance : modelElementInstances) {
			if (((FlowNode) instance).getIncoming().size() > 1) {
				toReturn += 1;
			}
		}
		return toReturn;
	}
	
	/**
	 * Metodo che cerca tutti gli elementi della classe type presenti nel modello e conta quanti uniscono e dividono flusso
	 * @param type: classe del tipo of elemento che si vuole analizzare
	 * @return number of elementi della classe type  
	 */
	@SuppressWarnings("rawtypes")
	private int getFlowJoiningAndDividingElementsOfType(Class type) {
		int toReturn = 0;
		Collection<ModelElementInstance> modelElementInstances = getCollectionOfElementType(type);
		for (ModelElementInstance instance : modelElementInstances) {
			if (((FlowNode) instance).getIncoming().size() > 1 && ((FlowNode) instance).getOutgoing().size() > 1) {
				toReturn += 1;
			}
		}
		return toReturn;
	}
	
	/**
	 * Metodo che cerca nel modello tutti gli elementi del tipo "type" per
	 * ottenerne number complessivo
	 * 
	 * @param type:
	 *            la classe del tipo of elementi of cui si vuole conoscere il
	 *            number
	 * @return number of elementi del tipo "type"
	 */
	@SuppressWarnings("rawtypes")
	public int getNumberOfTypeElement(Class type) {
		return getCollectionOfElementType(type).size();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<ModelElementInstance> getCollectionOfElementType(Class type) {
		ModelElementType modelElementType = modelInstance.getModel().getType(type);
		//check for extraction type option
		if(this.extraction.equals("Process"))
			if(this.conversion.equals("BlackBox"))
				return this.process.getChildElementsByType(type);
			else {
				Collection<ModelElementInstance> c = this.process.getChildElementsByType(type);
				ec.getCollectionOfElementTypeWBProcess(c, this.process, type);
				return c;
			}
		return this.modelInstance.getModelElementsByType(modelElementType);
	}
	
	/**
	 * Metodo che cerca number of eventDefinitions del tipo specificato dal parametro definitionClassPathName per gli eventi del tipo specificato dal parametro eventClassPathName.
	 * @param events - La collezione of CatchEvent of cui si vuole contare number of definizioni
	 * @param eventClassPathName - pathName della classe degli eventi of cui si vuole contare le definizioni
	 * @param definitionClassPathName - pathName della classe delle definizioni da contare
	 * @return number of definizioni
	 */
	private int getNumberOfEventDefinitionsOfCatchEvents(Collection<CatchEvent> events, String eventClassPathName, String definitionClassPathName) {
		Class<?> eventClass;
		Class<?> definitionClass;
		int toReturn = 0;
		try {
			eventClass = Class.forName(eventClassPathName);
			definitionClass = Class.forName(definitionClassPathName);
			for (CatchEvent e: events) {
				if (e.getClass().equals(eventClass)) {
					for (EventDefinition ed: e.getEventDefinitions()) {
						if (ed.getClass().equals(definitionClass)) {
							toReturn += 1;
						}
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	/**
	 * Metodo che cerca number of eventDefinitions del tipo specificato dal parametro definitionClassPathName per gli eventi del tipo specificato dal parametro eventClassPathName.
	 * @param events - La collezione of ThrowEvent of cui si vuole contare number of definizioni
	 * @param eventClassPathName - pathName della classe degli eventi of cui si vuole contare le definizioni
	 * @param definitionClassPathName - pathName della classe delle definizioni da contare
	 * @return number of definizioni
	 */
	private int getNumberOfEventDefinitionsOfThrowEvents(Collection<ThrowEvent> events, String eventClassPathName, String definitionClassPathName) {
		Class<?> eventClass;
		Class<?> definitionClass;
		int toReturn = 0;
		try {
			eventClass = Class.forName(eventClassPathName);
			definitionClass = Class.forName(definitionClassPathName);
			for (ThrowEvent e: events) {
				if (e.getClass().equals(eventClass)) {
					for (EventDefinition ed: e.getEventDefinitions()) {
						if (ed.getClass().equals(definitionClass)) {
							toReturn += 1;
						}
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	private int getCatchEventsMultipleDefinitions(Collection<CatchEvent> events, String eventClassPathName) {
		Class<?> eventClass;
		int toReturn = 0;
		try {
			eventClass = Class.forName(eventClassPathName);
			for (CatchEvent event: events) {
				if (event.getClass().equals(eventClass) && event.getEventDefinitions().size() > 1) {
					toReturn += 1;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	private int getThrowEventsMultipleDefinitions(Collection<ThrowEvent> events, String eventClassPathName) {
		Class<?> eventClass;
		int toReturn = 0;
		try {
			eventClass = Class.forName(eventClassPathName);
			for (ThrowEvent event: events) {
				if (event.getClass().equals(eventClass) && event.getEventDefinitions().size() > 1) {
					toReturn += 1;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	private int getNonInterruptingBoundaryEventsEventDefinitions(Collection<BoundaryEvent> events, String definitionClassPathName) {
		Class<?> definitionClass;
		int toReturn = 0;
		try {
			definitionClass = Class.forName(definitionClassPathName);
			for (BoundaryEvent e: events) {
				if (!e.cancelActivity()) {
					for (EventDefinition ed: e.getEventDefinitions()) {
						if (ed.getClass().equals(definitionClass)) {
							toReturn += 1;
						}
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	private int getInterruptingBoundaryEventsEventDefinitions(Collection<BoundaryEvent> events, String definitionClassPathName) {
		Class<?> definitionClass;
		int toReturn = 0;
		try {
			definitionClass = Class.forName(definitionClassPathName);
			for (BoundaryEvent e: events) {
				if (e.cancelActivity()) {
					for (EventDefinition ed: e.getEventDefinitions()) {
						if (ed.getClass().equals(definitionClass)) {
							toReturn += 1;
						}
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	/**
	 * Model Instance's getter
	 * @return
	 */
	public BpmnModelInstance getModelInstance() {
		return this.modelInstance;
	}
	
	public Process getProcess() {
	return this.process;
	}

	
	/**
	 * Proposed Metrics
	 * @param 
	 * @return number of empty pools
	 */
	private int getEmptyPools() {
		int numberOfEmptyPools = 0;
		try {
			if(this.extraction.equals("Model")) {
				Collection<Participant> participants = this.modelInstance.getModelElementsByType(Participant.class);
				for (Participant p : participants) {
					Collection<FlowElement> flowElementSet = p.getProcess().getFlowElements();
					if(flowElementSet.size()==0)
						numberOfEmptyPools++;
				}
			} else {
				Collection<FlowElement> flowElementSet = this.process.getFlowElements();
				if(flowElementSet.size()==0)
					numberOfEmptyPools++;
			}
		} catch (Exception e) {}
	return numberOfEmptyPools;
	}
	
	/**
	 * Metrica:NCP 
	 * 
	 * @return Number of collapsed pools
	 */
	public int getCollapsedPools() {
		int ncp = 0;
		if(this.extraction.equals("Model")) {
			Collection<Participant> participants = this.modelInstance.getModelElementsByType(Participant.class);
			for (Participant p : participants) {
				if(p.getProcess() == null)
					ncp++;
			}
		} else {
			if(this.process == null)
				ncp++;
		}
	return ncp;
	}
	
	/**
	 * Metric: NITEV
	 * 
	 * @return number of Intermediate Timer Events
	 */
	public int getIntermediateTimerEvents() {
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.IntermediateCatchEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.TimerEventDefinitionImpl");		
	}
	
	/**
	 * Metric: NSTESEV
	 * 
	 * @return number of Start Escalation Events
	 */
	public int getStartEscalationEvents() {
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.StartEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.EscalationEventDefinitionImpl");
	}
	
	/**
	 * Metric: NSTEREV
	 * 
	 * @return number of Start Error Events
	 */
	public int getStartErrorEvents() {
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.StartEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.ErrorEventDefinitionImpl");
	}
	
	/**
	 * Metric: NSTCOMEV
	 * 
	 * @return number of Start Compensation Events
	 */
	public int getStartCompensationEvents() {
		Collection<CatchEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(CatchEvent.class);
		else {
			events = this.process.getChildElementsByType(CatchEvent.class);
			ec.getCatchEventsSubProcess(events, process);
		}
		return this.getNumberOfEventDefinitionsOfCatchEvents(events, "org.camunda.bpm.model.bpmn.impl.instance.StartEventImpl", "org.camunda.bpm.model.bpmn.impl.instance.CompensateEventDefinitionImpl");
	}
	
	/**
	 * Metric: NENNNEV
	 * 
	 * @return number of End None Events
	 */
	public int getEndNoneEvents() {
		return this.getEndEvents() - this.getEndMessageEvents()- this.getEndSignalEvents() - this.getEndErrorEvents()
				- this.getEndEscalationEvents() - this.getEndTerminateEvents() - this.getEndCompensationEvents() 
				- this.getEndCancelEvents() - this.getEndMultipleEvents();

	}
	
	/**
	 * Metric: NSTNNEV
	 * 
	 * @return number of Start None Events
	 */
	public int getStartNoneEvents() {
		return this.getStartEvents() - this.getStartMessageEvents() - this.getStartTimerEvents() - this.getStartConditionalEvents()
				- this.getStartSignalEvents() - this.getStartMultipleEvents() - this.getStartParallelMultipleEvents()
				- this.getStartCompensationEvents() - this.getStartErrorEvents() - this.getEscalationEvents();
	}
	
	/**
	 * Metric: NINNEV
	 * 
	 * @return number of Intermediate None Events
	 */
	public int getIntermediateNoneEvents() {
		return this.getIntermediateCatchEvents() + this.getIntermediateThrowEvents() - this.getIntermediateCompensationThrowEvents() 
				- this.getIntermediateConditionalCatchEvents() - this.getIntermediateEscalationThrowEvents() 
				- this.getIntermediateLinkCatchEvents() - this.getIntermediateLinkThrowEvents() - this.getIntermediateMessageCatchEvents() 
				- this.getIntermediateMessageThrowEvents() - this.getIntermediateMultipleCatchEvents() - this.getIntermediateMultipleThrowEvents() 
				- this.getIntermediateParallelMultipleCatchEvents() - this.getIntermediateSignalCatchEvents() - this.getIntermediateSignalThrowEvents() 
				- this.getIntermediateTimerEvents();
		}
	
	/**
	 * Metric: NBIMUEV
	 * @return the number of interrupting boundary multiple events
	 */
	public int getInterruptingBoundaryMultipleEvents() {
		int toReturn = 0;
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		for (BoundaryEvent event: boundaryEvents) {
			if (event.getEventDefinitions().size() > 1) {
				toReturn += 1;
			}
		}
		
		return toReturn;
	}
	
	
	/**
	 * Metric: NBIPMUEV
	 * @return the number of interrupting boundary parallel multiple events
	 */
	public int getInterruptingBoundaryParallelMultipleEvents() {
		int toReturn = 0;
		Collection<BoundaryEvent> boundaryEvents;
		if(this.extraction.equals("Model"))
			boundaryEvents = this.modelInstance.getModelElementsByType(BoundaryEvent.class);
		else {
			boundaryEvents = this.process.getChildElementsByType(BoundaryEvent.class);
			ec.getBoundaryEventsSubProcess(boundaryEvents, process);
		}
		for (BoundaryEvent event: boundaryEvents) {
			if (event.isParallelMultiple()) {
				toReturn++;
			}
		}
		
		return toReturn;
	}
	
	public int getNonInterruptingStartMessageEvents() {
		int ni = 0;
		Collection<StartEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(StartEvent.class);
		else {
			events = this.process.getChildElementsByType(StartEvent.class);
			ec.getStartEventsSubProcess(events, process);
		}
		for(StartEvent e : events)
			if(!e.isInterrupting())
				for(EventDefinition ed : e.getEventDefinitions())
					if(ed instanceof MessageEventDefinition)
						ni++;
		return ni;
	}
	
	public int getNonInterruptingStartTimerEvents() {
		int ni = 0;
		Collection<StartEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(StartEvent.class);
		else {
			events = this.process.getChildElementsByType(StartEvent.class);
			ec.getStartEventsSubProcess(events, process);
		}
		for(StartEvent e : events)
			if(!e.isInterrupting())
				for(EventDefinition ed : e.getEventDefinitions())
					if(ed instanceof TimerEventDefinition)
						ni++;
		return ni;
	}
	
	public int getNonInterruptingStartConditionalEvents() {
		int ni = 0;
		Collection<StartEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(StartEvent.class);
		else {
			events = this.process.getChildElementsByType(StartEvent.class);
			ec.getStartEventsSubProcess(events, process);
		}
		for(StartEvent e : events)
			if(!e.isInterrupting())
				for(EventDefinition ed : e.getEventDefinitions())
   				    if(ed instanceof ConditionalEventDefinition)
						ni++;
		return ni;
	}
	
	public int getNonInterruptingStartSignalEvents() {
		int ni = 0;
		Collection<StartEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(StartEvent.class);
		else {
			events = this.process.getChildElementsByType(StartEvent.class);
			ec.getStartEventsSubProcess(events, process);
		}
		for(StartEvent e : events)
			if(!e.isInterrupting())
				for(EventDefinition ed : e.getEventDefinitions())
					if(ed instanceof SignalEventDefinition)
						ni++;
		return ni;
	}
	
	public int getNonInterruptingStartEscalationEvents() {
		int ni = 0;
		Collection<StartEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(StartEvent.class);
		else {
			events = this.process.getChildElementsByType(StartEvent.class);
			ec.getStartEventsSubProcess(events, process);
		}
		for(StartEvent e : events)
			if(!e.isInterrupting())
				for(EventDefinition ed : e.getEventDefinitions())
					if(ed instanceof EscalationEventDefinition)
						ni++;
		return ni;
	}
	
	public int getNonInterruptingStartMultipleEvents() {
		int ni = 0;
		Collection<StartEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(StartEvent.class);
		else {
			events = this.process.getChildElementsByType(StartEvent.class);
			ec.getStartEventsSubProcess(events, process);
		}
		for(StartEvent e : events)
			if((!e.isInterrupting()) && e.getEventDefinitions().size() > 1)
				ni++;
		return ni;
	}
	
	public int getNonInterruptingStartMultipleParallelEvents() {
		int ni = 0;
		Collection<StartEvent> events;
		if(this.extraction.equals("Model"))
			events = this.modelInstance.getModelElementsByType(StartEvent.class);
		else {
			events = this.process.getChildElementsByType(StartEvent.class);
			ec.getStartEventsSubProcess(events, process);
		}
		for(StartEvent e : events)
			if(!e.isInterrupting() && e.isParallelMultiple())
				ni++;
		return ni;
	}
	
}

