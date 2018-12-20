package bpmnMetadataExtractor;

import java.util.ArrayList;
import java.util.Collection;

import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.DataObject;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.InclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class BpmnAdvancedMetricsExtractor {
	
	//modello da cui estrarre le metriche
	private BpmnBasicMetricsExtractor basicMetricsExtractor;
	private JsonEncoder json;
	private CrossConnectivityMetricExtractor ccExtractor;
	private ConnectorInterplayMetricsExtractor connectorInterplayMetricsExtractor;
	private DurfeeSquareMetricExtractor dsmExtractor;
	private PartitionabilityMetricsExtractor partExtractor;
	private SizeMetricsExtractor sizeExtractor;
	private NestingDepthMetricsExtractor ndExtractor;
	private StronglyConnectedComponentsMetricExtractor sccExtractor;
	private CognitiveWeightMetricExtractor cwExtractor;
	private LayoutMetricsExtractor lmExtractor;
	
	public BpmnAdvancedMetricsExtractor(BpmnBasicMetricsExtractor basicMetricsExtractor, JsonEncoder jsonEncoder) {
		this.basicMetricsExtractor = basicMetricsExtractor;
		this.json = jsonEncoder;
		this.ccExtractor = new CrossConnectivityMetricExtractor(basicMetricsExtractor);
		this.connectorInterplayMetricsExtractor = new ConnectorInterplayMetricsExtractor(basicMetricsExtractor);
		this.dsmExtractor = new DurfeeSquareMetricExtractor(basicMetricsExtractor);
		this.partExtractor = new PartitionabilityMetricsExtractor(basicMetricsExtractor);
		this.sizeExtractor = new SizeMetricsExtractor(basicMetricsExtractor);
		this.ndExtractor = new NestingDepthMetricsExtractor(basicMetricsExtractor);
		this.sccExtractor = new StronglyConnectedComponentsMetricExtractor(basicMetricsExtractor);
		this.cwExtractor = new CognitiveWeightMetricExtractor(basicMetricsExtractor);
		this.lmExtractor = new LayoutMetricsExtractor(basicMetricsExtractor);
	}
	
	public void runMetrics() {
		json.addAdvancedMetric("CLA", getConnectivityLevelBetweenActivities());
		json.addAdvancedMetric("CLP", getConnectivityLevelBetweenPartecipants());
		json.addAdvancedMetric("PDOPin", getProportionOfIncomingDataObjectsAndTotalDataObjects());
		json.addAdvancedMetric("PDOPout", getProportionOfOutgoingDataObjectsAndTotalDataObjects());
		json.addAdvancedMetric("TNT", getTotalNumberOfTasks());
		json.addAdvancedMetric("PDOTout", getProportionOfDataObjectsAsOutgoingProducts());
		json.addAdvancedMetric("PLT", getProportionOfPoolsOrLanesAndActivities());
		json.addAdvancedMetric("TNCS", getNumberOfCollapsedSubProcesses());
		json.addAdvancedMetric("TNA", getTotalNumberOfActivities());
		json.addAdvancedMetric("TNDO", getTotalNumberOfDataObjects());
		json.addAdvancedMetric("TNG", getTotalNumberOfGateways());
		json.addAdvancedMetric("TNEE", getTotalNumberOfEndEvents());
		json.addAdvancedMetric("TNIE", getTotalNumberOfIntermediateEvents());
		json.addAdvancedMetric("TNSE", getTotalNumberOfStartEvents());
		json.addAdvancedMetric("TNE", getTotalNumberOfEvents());
		json.addAdvancedMetric("CFC", getControlFlowComplexity());
		json.addAdvancedMetric("NOAC", getNumberOfActivitiesAndControlFlowElements());
		json.addAdvancedMetric("NOAJS", this.getNumberOfActivitiesJoinsAndSplits());
		json.addAdvancedMetric("HPC_D", getHalsteadBasedProcessComplexityDifficulty());
		json.addAdvancedMetric("HPC_N", getHalsteadBasedProcessComplexityLength());
		json.addAdvancedMetric("HPC_V", getHalsteadBasedProcessComplexityVolume());
		json.addAdvancedMetric("NoI", getNumberOfActivityInputs());
		json.addAdvancedMetric("NoO", getNumberOfActivityOutputs());
		json.addAdvancedMetric("Length", getActivityLength());
		json.addAdvancedMetric("IC", getInterfaceComplexityOfActivityMetric());
		json.addAdvancedMetric("NOF", getNumberOfControlFlow());	
		json.addAdvancedMetric("TNSF", getTotalNumberOfSequenceFlow());	
		json.addAdvancedMetric("CC", ccExtractor.calculateCrossConnectivity());
		json.addAdvancedMetric("ICP",getImportedCouplingOfProcess());
		json.addAdvancedMetric("ECP",getExportedCouplingOfProcess());
		json.addAdvancedMetric("W", cwExtractor.getCognitiveWeight());
		json.addAdvancedMetric("MaxND", ndExtractor.getMaxNestingDepth());
		json.addAdvancedMetric("CP", getProcessCoupling());
		json.addAdvancedMetric("CNC", this.getCoefficientOfNetworkComplexity());
		json.addAdvancedMetric("MeanND", ndExtractor.getMeanNestingDepth());
		json.addAdvancedMetric("Sn", getNumberOfNodes());
		json.addAdvancedMetric("Sequentiality", getSequentiality());
		json.addAdvancedMetric("diam", sizeExtractor.getDiam());
		json.addAdvancedMetric("Depth", partExtractor.getDepth());
//		json.addAdvancedMetric("Structuredness", partExtractor.getStructuredness());
		json.addAdvancedMetric("CYC", this.sccExtractor.getCyclicity());
		json.addAdvancedMetric("TS", this.getTokenSplit());
		json.addAdvancedMetric("Density", getDensity());
		json.addAdvancedMetric("ACD", this.getAverageConnectorDegree());
		json.addAdvancedMetric("MCD", this.getMaximumConnectorDegree());
		json.addAdvancedMetric("GM", this.connectorInterplayMetricsExtractor.getGatewaysMismatchMetric());
		json.addAdvancedMetric("CH", this.connectorInterplayMetricsExtractor.getConnectorsHeterogeneityMetric());
		json.addAdvancedMetric("ECaM", this.getExtendedCardosoMetric());
		json.addAdvancedMetric("ECyM", this.sccExtractor.getEcym());
		json.addAdvancedMetric("DSM", dsmExtractor.getDurfeeMetric());
		json.addAdvancedMetric("PSM", dsmExtractor.getPerfectSquareMetric());
		json.addAdvancedMetric("Layout_Complexity", dsmExtractor.getLayoutComplexityMetric());
		json.addAdvancedMetric("Layout_Measure", this.lmExtractor.getLayoutMeasure());
		System.out.println("JSON adv: " + this.json.getString());
	}

	/**
	 * Metric: CLA
	 * Total Number of Activities / Number of Sequence Flows between these Activities 
	 * (CLA = TNA / NSFA)
	 * @return 
	 */
	public float getConnectivityLevelBetweenActivities() {
		try {
			return getTotalNumberOfActivities() /  basicMetricsExtractor.getSequenceFlowsBetweenActivities();
		} catch (ArithmeticException e) {
			return 0;
		}
	}
	
	/**
	 * Metric: CLP
	 * Number of message flows / Number of Pools 
	 * (CLP = NMF/NP)
	 * @return
	 */
	public float getConnectivityLevelBetweenPartecipants() {
		try {
			return basicMetricsExtractor.getMessageFlows() / basicMetricsExtractor.getPools();
		} catch (ArithmeticException e) {
			return 0;
		}
	}

	/**
	 * Metric: PDOPin
	 * PDOPin Proportion of data objects as incoming products and total data objects
	 * Number of Data Objects which are input of activities / Total number of Data Objects in the model (PDOPIn = NDOIn/TNDO)
	 * @return 
	 */
	public float getProportionOfIncomingDataObjectsAndTotalDataObjects() {
		try {
			return basicMetricsExtractor.getDataObjectsInput() / getTotalNumberOfDataObjects();
		} catch (ArithmeticException e) {
			return 0;
		}
	}
	
	/**
	 * Metric: PDOPout
	 * PDOPout Proportion of data objects as outgoing products and total data objects
	 * Number of Data Objects which are output of activities / Total number of Data Objects in the model (PDOPOut = NDOOut/TNDO)
	 * @return 
	 */
	public float getProportionOfOutgoingDataObjectsAndTotalDataObjects() {
		try {
			return basicMetricsExtractor.getDataObjectsOutput() / getTotalNumberOfDataObjects();
		} catch (ArithmeticException e) {
			return 0;
		}
	}
	
	/**TODO gi� presente in quelle base
	 * Metric: TNT
	 * Total number of task 
	 * Number of Tasks + Number of Task Looping + Number of Task Multiple Instances + Number of Task Compensation (TNT = NT + NTL + NTMI + NTC)
	 * @return
	 */
	public int getTotalNumberOfTasks() {
		return basicMetricsExtractor.getTasks();
	}
	
	/**
	 * Metric: PDOTout
	 * Proportion of data objects as outgoing product of activities of the model
	 * Number of data objects which are outputs of activities / Total number of Tasks (PDOTOut = NDOOut/TNT)
	 * @return
	 */
	public float getProportionOfDataObjectsAsOutgoingProducts() {
		try {
			return basicMetricsExtractor.getDataObjectsOutput() / getTotalNumberOfTasks();
		} catch (ArithmeticException e) {
			return 0;
		}
	}
	
	/**
	 * Metric: PLT
	 * Proportion of pools/lanes and activities
	 * Number of Lanes / Total number of Tasks (PLT = NL/TNT)
	 * @return
	 */
	public float getProportionOfPoolsOrLanesAndActivities() {
		try {
			return basicMetricsExtractor.getLanes() / getTotalNumberOfTasks();
		} catch (ArithmeticException e) {
			return 0;
		}
	}
	
	/**
	 * Metric: TNCS
	 * Total number of collapsed sub-processes
	 * Number of Collapsed Sub-Processes + Number of Collapsed Sub-Processes Looping + Number of Collapsed Sub-Processes Multiple Instance
	 *    + Number of Collapsed Sub-Processes Compensation + Number of Collapsed Sub-Processes Ad-Hoc
	 * (TNCS = NCS + NCSL + NCSMI + NCSC + NCSA)
	 * @return
	 */
	public int getNumberOfCollapsedSubProcesses() {
		return basicMetricsExtractor.getSubprocesses();
	}
	
	/**
	 * Metric: TNA
	 * Total number of activities
	 * Total number of Tasks + Total number of Collapsed Sub-Processes(TNA = TNT + TNCS)
	 * @return
	 */
	public int getTotalNumberOfActivities() {
		return getTotalNumberOfTasks() + getNumberOfCollapsedSubProcesses();
	}
	
	/**
	 * Metric: TNDO
	 * Total number of Data Objects in the model
	 * Number of data objects which are input of activities + Number of data objects which are outputs of activities (TNDO = NDOIn + NDOOut)
	 * @return
	 */
	public int getTotalNumberOfDataObjects() {
		return basicMetricsExtractor.getDataObjectsInput() + basicMetricsExtractor.getDataObjectsOutput();
	}
	
	/**TODO
	 * Metric: TNG
	 * Total number of gateways
	 * Number of exclusive data-based decision + Number of exclusive event-based decision + Number of inclusive decision + Number of complex decision + Number of parallel forking
	 * (TNG = NEDDB + NEDEB + NID + NCD + NPF)
	 * @return
	 */
	public int getTotalNumberOfGateways() {
		return basicMetricsExtractor.getExclusiveDataBasedDecisions() + basicMetricsExtractor.getExclusiveEventBasedDecisions() + basicMetricsExtractor.getInclusiveDecisions() + 
				basicMetricsExtractor.getComplexDecisions() + basicMetricsExtractor.getParallelGateways();
		//return basicMetricsExtractor.getGateways();
	}
	
	/**TODO i vari tipi di End Events non sono presenti 
	 * Metric: TNEE
	 * Total number of End Events
	 * Number of End None Events + Number of End Message Events + Number of End Error Events + Number of End Cancel Events +
	 *      + Number of End Compensation Events + Number of End Link Events + Number of End Multiple Events + Number of End Terminate Events  
	 * (TNEE = NENE + NEMsE + NEEE + NECaE + NECoE + NELE + NEMuE + NETE)
	 * @return
	 */
	public int getTotalNumberOfEndEvents() {
		return basicMetricsExtractor.getEndEvents();
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
		return basicMetricsExtractor.getEvents() - basicMetricsExtractor.getStartEvents() - basicMetricsExtractor.getEndEvents();
	}
	
	/**TODO i vari tipi di Start Events non sono presenti 
	 * Metric: TNSE
	 * Total number of Start Events
	 * Number of Start None Events + Number of Start Timer Events + Number of Start Message Events + Number of Start Rule Events + Number of Start Link Events + 
	 *     + Number of Start Multiple Events 
	 * (TNSE = NSNE + NSTE + NSMsE + NSRE + NSLE + NSMuE)
	 * @return
	 */
	public int getTotalNumberOfStartEvents() {
		return basicMetricsExtractor.getStartEvents();
	}
	
	/**TODO 
	 * Metric: TNE
	 * Total number of Events
	 * Total number of Start Events + Total number of Intermediate Events + Total number of End Events
	 * (TNE = TNSE + TNIE + TNEE)
	 * @return
	 */
	public int getTotalNumberOfEvents() {
		return getTotalNumberOfStartEvents() + getTotalNumberOfIntermediateEvents() + getTotalNumberOfEndEvents();
	}
	
	/**
	 * Metric: CFC
	 * Control-flow Complexity metric. It captures a weighted sum of all connectors that are used in a process model.
	 * @return
	 */
	public double getControlFlowComplexity() {
		double toReturn = 0;
		int tempSize = 0;
		Collection<ModelElementInstance> exclusiveGateways = basicMetricsExtractor.getCollectionOfElementType(ExclusiveGateway.class);
		Collection<ModelElementInstance> inclusiveGateways = basicMetricsExtractor.getCollectionOfElementType(InclusiveGateway.class);
		Collection<ModelElementInstance> parallelGateways = basicMetricsExtractor.getCollectionOfElementType(ParallelGateway.class);
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		//The CFC of a xor-split is given by the number of his outgoing flows
		for (ModelElementInstance exGateway : exclusiveGateways) {
			if (((FlowNode) exGateway).getOutgoing().size() > 1)
				toReturn += ((FlowNode) exGateway).getOutgoing().size();
		}
		
		//The CFC of an or-split is given by the formula 2^n - 1, where n is equals to the number of the gateway's outgoing flow
		for (ModelElementInstance inGateway : inclusiveGateways) {
			if (((FlowNode) inGateway).getOutgoing().size() > 1) {
				tempSize = ((FlowNode) inGateway).getOutgoing().size();
				toReturn += Math.pow(2, tempSize) - 1;
			}
		}
		//The CFC of an and-split is just 1
		for (ModelElementInstance parGateway : parallelGateways) {
			if (((FlowNode) parGateway).getOutgoing().size() > 1) {
				toReturn++;
			}
		}
		//Uncontrolled splits are included in the calculation as and-splits
		for (ModelElementInstance activity : activities) {
			if (((FlowNode) activity).getOutgoing().size() > 1) {
				toReturn ++;
			}
		}
		return toReturn;
	}
	
	/**
	 * Metric: NOAC
	 * Number of activities and control-flow elements in a process. 
	 * @return
	 */
	public int getNumberOfActivitiesAndControlFlowElements() {
		return basicMetricsExtractor.getActivities() + basicMetricsExtractor.getGateways();
	}
	
	/**
	 * Metric: NOAJS
	 * Number of activities, joins, and splits in a process
	 */
	public int getNumberOfActivitiesJoinsAndSplits() {
		return basicMetricsExtractor.getActivities() + basicMetricsExtractor.getFlowDividingGateways() + 
				basicMetricsExtractor.getFlowJoiningGateways() + basicMetricsExtractor.getFlowDividingTasks() + 
				basicMetricsExtractor.getFlowJoiningTasks();
	}
	
	/**
	 * Metric: HPC_D
	 * Halstead-based Process Complexity (process difficulty)
	 * Process Difficulty: D = (n1/2)*(N2/n2)
	 * @return
	 */
	public double getHalsteadBasedProcessComplexityDifficulty() {
		float toReturn = 0;
		try {
			toReturn = (getNumberOfUniqueElements() / 2) * (basicMetricsExtractor.getDataObjects() / getNumberOfUniqueDataObjects());
		} catch (ArithmeticException e) {
		}
		return toReturn;
	}
	
	/**
	 * Metric: HPC_N
	 * Halstead-based Process Complexity (process length)
	 * Process Length: N = n1*log2(n1) + n2*log2(n2)
	 * @return
	 */
	public double getHalsteadBasedProcessComplexityLength() {
		double toReturn = 0;
		try {
			toReturn = getNumberOfUniqueElements() * logBase2(getNumberOfUniqueElements()) + 
					getNumberOfUniqueDataObjects() * logBase2(getNumberOfUniqueDataObjects());
		} catch (ArithmeticException e) {
		}
		if (Double.isFinite(toReturn)) 
			return toReturn;
		else 
			return 0;
	}
	
	/**
	 * Metric: HPC_V
	 * Halstead-based Process Complexity (process volume)
	 * Process Volume: V = (N1+N2)*log2(n1+n2)
	 * @return
	 */
	public double getHalsteadBasedProcessComplexityVolume() {
		double toReturn = 0;
		try {
			toReturn = (getNumberOfElements() + basicMetricsExtractor.getDataObjects() 
			* (logBase2(getNumberOfUniqueElements() + getNumberOfUniqueDataObjects())));
		} catch (ArithmeticException e) {
		}
		if (Double.isFinite(toReturn))
			return toReturn;
		else 
			return 0.0;
	}
	
	/**
	 * Metric: NoI or Fanin
	 * Number of activity inputs. The fan-in of a procedure A is the number of local flows
	 *  into procedure A plus the number of data structures from which procedure A retrieves information.
	 * @return
	 */
	public int getNumberOfActivityInputs() {
		return basicMetricsExtractor.getDataInputAssociations();
	}
	
	/**
	 * Metric: NoO or Fanout
     * Number of activity outputs. The fan-out of a procedure A is the number of local flows
     *  from procedure A plus the number of data structures which procedure A updates.
	 * @return
	 */
	public int getNumberOfActivityOutputs() {
		return basicMetricsExtractor.getDataOutputAssociations();
	}
	
	/**
	 * Metric: Length
	 * Activity length. The length is 1 if the activity is a black box; if it is a white box,
	 *  the length can be calculated using traditional software engineering metrics
	 *  that have been previously presented, namely the LOC (line of code) and
	 *  MCC (McCabe�s cyclomatic complexity).
	 *  @return
	 */
	public int getActivityLength() {
		return basicMetricsExtractor.getActivities();
	}
	
	/**
	 * Metric: IC
	 * Interface complexity of an activity metric. IC = Length * (NoI * NoO)^2, where 
	 *  the length of the activity can be calculated using traditional Software Engineering metrics
	 *  such as LOC (1 if the activity source code is unknown) and NoI and NoO are 
	 *  the number of inputs and outputs.
	 * @return
	 */
	public double getInterfaceComplexityOfActivityMetric() {
		return getActivityLength() * Math.pow((getNumberOfActivityInputs() * getNumberOfActivityOutputs()), 2);
	}
	
	/**
	 * Metric: NOF
	 * Number of control flow connections (number of arcs)
	 * @return
	 */
	public int getNumberOfControlFlow() {
		int toReturn = 0;
		toReturn = basicMetricsExtractor.getSequenceFlows();
		return toReturn;
	}
	
	/**TODO Same as basic metric
	 * Metric: TNSF
	 * Total Number of Sequence Flows
	 * @return
	 */
	public int getTotalNumberOfSequenceFlow(){
		return basicMetricsExtractor.getSequenceFlows();
	}

	/**
	 * Metric: ICP
	 * It counts, for each (sub-)process, the number of message/sequence flows sent by either the
	 * tasks of the (sub-) process or the (sub-) process itself.
	 * @return
	 */
	public int getImportedCouplingOfProcess(){
		int toReturn = 0;
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		Collection<ModelElementInstance> modelMessageFlows = basicMetricsExtractor.getCollectionOfElementType(MessageFlow.class);
		for (ModelElementInstance a : activities){
			toReturn += ((FlowNode) a).getOutgoing().size();
		}
		for (ModelElementInstance mMessageFlow : modelMessageFlows) {
			if (((MessageFlow) mMessageFlow).getSource() instanceof Activity) {
				toReturn++;
			}
		}
		return toReturn;
	}
	
	/**
	 * Metric: ECP
	 * It counts, for each (sub-)process, the number of message/sequence flows received by either
	 * the tasks of the (sub-) process or the (sub-) process itself.
	 * @return
	 */
	public int getExportedCouplingOfProcess(){
		int toReturn = 0;
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		Collection<ModelElementInstance> modelMessageFlows = basicMetricsExtractor.getCollectionOfElementType(MessageFlow.class);
		for (ModelElementInstance a : activities){
			toReturn += ((FlowNode) a).getIncoming().size();
		}
		for (ModelElementInstance mMessageFlow : modelMessageFlows) {
			if (((MessageFlow) mMessageFlow).getTarget() instanceof Activity) {
				toReturn++;
			}
		}
		return toReturn;
	}
	
	/**
	 * Metric: CP
	 * The metric calculates the degree of coupling. Coupling is related to the number of interconnections among the
	 * tasks of a process model.
	 * @return
	 */
	public float getProcessCoupling(){
		float toReturn = 0;
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		if (activities.size() > 1){
			for (ModelElementInstance t: activities){
				Collection<SequenceFlow> outgoing = ((FlowNode) t).getOutgoing();
				for (SequenceFlow s: outgoing){
					if (s.getTarget() instanceof Activity){
						toReturn++;
					}
				}
			}
			toReturn = toReturn/(activities.size() * activities.size() - 1);
		}
		return toReturn;
	}
	
	/**
	 * Metric: Sn
	 * Number of nodes (activities + routing elements)
	 * @return
	 */
	public int getNumberOfNodes(){
		return basicMetricsExtractor.getFlowNodes();
	}
	
	/**
	 * Metric: Lambda
	 * "The density of the process graph refers to the number of arcs divided by the number of the maximum number
	 *  of arcs for the same number of nodes"
	 * @return
	 */
	public float getDensity() {
		float nodes = basicMetricsExtractor.getFlowNodes();
		return basicMetricsExtractor.getSequenceFlows() / (nodes * (nodes - 1));
	}
	
	/**
	 * Metric: CNC
	 * Coefficient of Network Complexity (total number of sequence flows(NSEQF)/NOAJS)
	 * @return NSEQF/NOAJS
	 */
	public double getCoefficientOfNetworkComplexity() {
		try {
			return (double)this.getNumberOfControlFlow()/this.getNumberOfActivitiesJoinsAndSplits();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: Xi
	 * "The sequentiality ratio is the number of arcs between non-connector nodes 
	 *  divided by the number of arcs."
	 * @return
	 */
	public float getSequentiality() {
		Collection<ModelElementInstance> sequenceFlowsModel = basicMetricsExtractor.getCollectionOfElementType(SequenceFlow.class);
		float arcBetweenNonConnectorsNode = sequenceFlowsModel.size();
		for (ModelElementInstance sFModel : sequenceFlowsModel) {
			SequenceFlow flow = (SequenceFlow) sFModel;
			if (flow.getSource() instanceof Gateway || flow.getTarget() instanceof Gateway) {
				arcBetweenNonConnectorsNode--;
			}
		}
		return arcBetweenNonConnectorsNode / sequenceFlowsModel.size();
		
	}
	/**
	 * Metric ACD
	 * Average connector degree is defined as the average incoming and outgoing sequence flows of all gateways and activities
	 * with at least two incoming or outgoing sequence flows
	 * @return
	 */
	public float getAverageConnectorDegree(){
		float toReturn = 0.0f;
		float sum = 0.0f, n = 0.0f;
		try {
			Collection<ModelElementInstance> gateways = basicMetricsExtractor.getCollectionOfElementType(Gateway.class);
			Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
			for (ModelElementInstance modelGateway : gateways){
				FlowNode g = (FlowNode) modelGateway;
				if (g.getOutgoing().size() > 1 || g.getIncoming().size() > 1){
					n++;
					sum += g.getOutgoing().size() + g.getIncoming().size();
				}
			}
			for (ModelElementInstance activityModel: activities){
				FlowNode t = (FlowNode) activityModel;
				if (t.getOutgoing().size() > 1 || t.getIncoming().size() > 1){
					n++;
					sum += t.getOutgoing().size() + t.getIncoming().size();	
				}
			}
			toReturn = sum/n;
		}catch (ArithmeticException e){
			return 0.0f;
		}
		if (Float.isFinite(toReturn))
			return toReturn;
		else 
			return 0.0f;
	}
	
	/**
	 * Metric ECaM
	 * It is the extension of CFC metric for Petri Nets.
	 * @return
	 */
	public double getExtendedCardosoMetric(){
		return this.getControlFlowComplexity();
	}
	
	/**
	 * Metric: TS
	 * Token Split counts the number of tokens introduced by an AND-Split or an OR-Split
	 * 
	 * @return the token split degree
	 */
	public int getTokenSplit() {
		int tokenSplitDegree = 0;
		Collection<ModelElementInstance> gateways = this.basicMetricsExtractor.getCollectionOfElementType(Gateway.class);
		Gateway temp;
		for (ModelElementInstance modelGateway: gateways) {
			temp = (Gateway) modelGateway;
			if ((temp instanceof ParallelGateway && temp.getOutgoing().size() > 1) || (temp instanceof InclusiveGateway && temp.getOutgoing().size() > 1)) {
				//Each time an Or-Split or an And-Split is encountered, the token split degree is increased by the out-degree of the gateway - 1.
				//This corresponds to the number of tokens newly introduced by the split
				tokenSplitDegree += (temp.getOutgoing().size() - 1);
			}
		}
		return tokenSplitDegree;
	}
	
	/**
	 * Metric MCD
	 * Maximum connector degree is defined as the sum of the incoming and outgoing sequence flows of the gateway or activity with
	 * the most incoming and outgoing sequence flows
	 * @return
	 */
	public float getMaximumConnectorDegree(){
		float toReturn = 0;
		Collection<ModelElementInstance> gateways = basicMetricsExtractor.getCollectionOfElementType(Gateway.class);
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		for (ModelElementInstance g: gateways){
			if (((FlowNode) g).getOutgoing().size() + ((FlowNode) g).getIncoming().size() > toReturn){
				toReturn = ((FlowNode) g).getOutgoing().size() + ((FlowNode)g).getIncoming().size();
			}
		}
		for (ModelElementInstance t: activities){
			if (((FlowNode) t).getOutgoing().size() + ((FlowNode) t).getIncoming().size() > toReturn){
				toReturn = ((FlowNode) t).getOutgoing().size() + ((FlowNode)t).getIncoming().size();
			}
		}
		return toReturn;
	}
	

	/**
	 * The number of unique activities, splits and joins, and control-flow elements 
	 * Per le metriche di Halstead corrisponde a n1
	 * @return
	 */
	private int getNumberOfUniqueElements() {
		int toReturn = 0;
		//attivit�
		if (basicMetricsExtractor.getReceiveTasks() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getScriptTasks() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getManualTasks() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getServiceTasks() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getUserTasks() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getSendTasks() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getBusinessRuleTasks() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getCallActivities() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getMultiInstanceLoopCharacteristics() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getLoopCharacteristics() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getCompensateEvents() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getSubprocesses() > 0)
			toReturn +=1;
		//splits, joins e control flow => gateways?
		if (basicMetricsExtractor.getParallelGateways() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getComplexDecisions() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getExclusiveDataBasedDecisions() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getExclusiveEventBasedDecisions() > 0)
			toReturn +=1;
		if (basicMetricsExtractor.getInclusiveDecisions() > 0)
			toReturn +=1;
		
		return toReturn;
	}
	
	/**
	 * Per le metriche di Halstead corrisponde a N1
	 * @return
	 */
	private int getNumberOfElements() {
		return basicMetricsExtractor.getReceiveTasks()  + 
			basicMetricsExtractor.getScriptTasks()  + 
			basicMetricsExtractor.getManualTasks()  + 
			basicMetricsExtractor.getServiceTasks()  + 
			basicMetricsExtractor.getUserTasks()  + 
			basicMetricsExtractor.getSendTasks()  + 
			basicMetricsExtractor.getBusinessRuleTasks()  + 
			basicMetricsExtractor.getCallActivities()  + 
			basicMetricsExtractor.getMultiInstanceLoopCharacteristics()  + 
			basicMetricsExtractor.getLoopCharacteristics()  + 
			basicMetricsExtractor.getCompensateEvents()  + 
			basicMetricsExtractor.getSubprocesses()  + 
			basicMetricsExtractor.getParallelGateways()  + 
			basicMetricsExtractor.getComplexDecisions()  + 
			basicMetricsExtractor.getExclusiveDataBasedDecisions()  + 
			basicMetricsExtractor.getExclusiveEventBasedDecisions()  + 
			basicMetricsExtractor.getInclusiveDecisions();
	}
	
	/**
	 * Per le metriche di Halstead corrisponde a n2
	 * @return
	 */
	private int getNumberOfUniqueDataObjects() {
		String name = "";
		Collection<String> objectNames = new ArrayList<String>();
		Collection<ModelElementInstance> dataObjects = basicMetricsExtractor.getCollectionOfElementType(DataObject.class);
		for (ModelElementInstance obj : dataObjects) {
			DataObject dataObj = (DataObject) obj;
			name = dataObj.getName();
			if (!objectNames.contains(name))
				objectNames.add(name);
		}
		return objectNames.size();
	}
	
	/**
	 * Implementazione del logaritmo in base 2
	 * @param number
	 * @return
	 */
	private double logBase2(int number) {
		double toReturn = 0;
		try {
			toReturn = Math.log(number) / Math.log(2);
		} catch (ArithmeticException e) {
			System.out.println(e);
		}
		return toReturn;
	}

}