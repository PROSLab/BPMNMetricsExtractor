package bpmnMetadataExtractor;

import java.util.ArrayList;
import java.util.Collection;

import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.DataObject;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.InclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import metricsOnGraphs.BindingStructure;
import metricsOnGraphs.Diameter;
import metricsOnGraphs.GraphMatrixes;
import metricsOnGraphs.ModelConverter;

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
	//new addition
	private ModelConverter mc;
	private DOPMetricsExtractor dopExtractor;
	private int numberProcess;
	
	/*public BpmnAdvancedMetricsExtractor(ModelConverter mc, BpmnBasicMetricsExtractor basicMetricsExtractor, JsonEncoder jsonEncoder) {
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
		//!!!
		this.dopExtractor = new DOPMetricsExtractor();
		this.mc = mc;
	}*/
	
	public BpmnAdvancedMetricsExtractor(ModelConverter mc, BpmnBasicMetricsExtractor basicMetricsExtractor, JsonEncoder jsonEncoder, int i) {
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
		this.dopExtractor = new DOPMetricsExtractor();
		this.mc = mc;
		this.numberProcess = i;
	}
	
	/*public void runMetrics() {
		json.addAdvancedMetric("CLA", getConnectivityLevelBetweenActivities());
		json.addAdvancedMetric("CLP", getConnectivityLevelBetweenPartecipants());
		json.addAdvancedMetric("PDOPin", getProportionOfIncomingDataObjectsAndTotalDataObjects());
		json.addAdvancedMetric("PDOPout", getProportionOfOutgoingDataObjectsAndTotalDataObjects());
		json.addAdvancedMetric("TNT", getTotalNumberOfTasks());
		json.addAdvancedMetric("VOL", getVolume());
		json.addAdvancedMetric("RRPA", getRatioRolesActivities());
		json.addAdvancedMetric("PDOTout", getProportionOfDataObjectsAsOutgoingProducts());
		json.addAdvancedMetric("PLT", getProportionOfLanesAndTasks());
		json.addAdvancedMetric("TNCS", getNumberOfCollapsedSubProcesses());
		//json.addAdvancedMetric("TNA", getTotalNumberOfActivities());
		json.addAdvancedMetric("TNDO", getTotalNumberOfDataObjects());
		json.addAdvancedMetric("TNG", getTotalNumberOfGateways());
		json.addAdvancedMetric("S(df)", getDataFlowSize());
		json.addAdvancedMetric("C(df)", getDataFlowComplexity());
		json.addAdvancedMetric("TNEE", getTotalNumberOfEndEvents());
		json.addAdvancedMetric("TNIE", getTotalNumberOfIntermediateEvents());
		json.addAdvancedMetric("TNSE", getTotalNumberOfStartEvents());
		json.addAdvancedMetric("TNE", getTotalNumberOfEvents());
		json.addAdvancedMetric("CFC", getControlFlowComplexity());
		json.addAdvancedMetric("CFC(rel)", getRelativeControlFlowComplexity());
		json.addAdvancedMetric("PCFC", getParallelControlFlowComplexity());
		json.addAdvancedMetric("JC", getJoinComplexity());
		json.addAdvancedMetric("JSR", getJoinSplitRatio());
		json.addAdvancedMetric("PC", getProcessComplexity());
		json.addAdvancedMetric("PFC", getProcessFlowComplexity());
		json.addAdvancedMetric("CADAC", getArcCognitiveComplexity());
		json.addAdvancedMetric("NOAC", getNumberOfActivitiesAndControlFlowElements());
		json.addAdvancedMetric("NOAJS", this.getNumberOfActivitiesJoinsAndSplits());
		json.addAdvancedMetric("HPC_D", getHalsteadBasedProcessComplexityDifficulty());
		json.addAdvancedMetric("HPC_N", getHalsteadBasedProcessComplexityLength());
		json.addAdvancedMetric("HPC_V", getHalsteadBasedProcessComplexityVolume());
		json.addAdvancedMetric("NoI", getNumberOfActivityInputs());
		json.addAdvancedMetric("AAI", getAverageActivityInput());
		json.addAdvancedMetric("NoO", getNumberOfActivityOutputs());
		json.addAdvancedMetric("AAO", getAverageActivityOutput());
		json.addAdvancedMetric("Length", getActivityLength());
		json.addAdvancedMetric("IC", getInterfaceComplexityOfActivityMetric());
		json.addAdvancedMetric("FIO", getStructuralComplexity());
		json.addAdvancedMetric("NOF", getNumberOfControlFlow());	
		json.addAdvancedMetric("TNSF", getTotalNumberOfSequenceFlow());	
		json.addAdvancedMetric("CC", ccExtractor.calculateCrossConnectivity());
		json.addAdvancedMetric("ICP",getImportedCouplingOfProcess());
		json.addAdvancedMetric("ECP",getExportedCouplingOfProcess());
		json.addAdvancedMetric("W", cwExtractor.getCognitiveWeight());
		json.addAdvancedMetric("MaxND", ndExtractor.getMaxNestingDepth());
		json.addAdvancedMetric("CP", getProcessCoupling());
		json.addAdvancedMetric("WCP", getWeightedProcessCoupling());
		json.addAdvancedMetric("Par", getParallelism());
		json.addAdvancedMetric("CNC^2", this.getCoefficientOfNetworkComplexity());
		json.addAdvancedMetric("CNC", this.getCoefficientComplexity());
		json.addAdvancedMetric("NCA", this.getActivityCoupling());
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
		json.addAdvancedMetric("PF", this.sccExtractor.getCycle());
		json.addAdvancedMetric("DSM", dsmExtractor.getDurfeeMetric());
		json.addAdvancedMetric("PSM", dsmExtractor.getPerfectSquareMetric());
		json.addAdvancedMetric("Layout_Complexity", dsmExtractor.getLayoutComplexityMetric());
		json.addAdvancedMetric("Layout_Measure", this.lmExtractor.getLayoutMeasure());
		//System.out.println("JSON adv: " + this.json.getString());
		//!!!
		int cont = 0;
		double sumB= 0;
		double sumL = 0;
		for(Process p : this.mc.getProcesses()) {
			cont++;
			GraphMatrixes gm = this.mc.convertModel(p, "WhiteBox");
			BindingStructure b = new BindingStructure(gm.getEdge(),gm.getVertix());
			sumB+= (double) Math.round(b.getB() * 1000d) / 1000d;
			Diameter d = new Diameter(gm.getAdjacencyMatrix());
			sumL += d.getL();
			this.dopExtractor.setDop(p);
		}
		this.json.addAdvancedMetric("DOP", this.dopExtractor.getDop());
		this.json.addAdvancedMetric("B", ((double) Math.round(sumB * 1000d) / 1000d) / cont);
	}*/
	
	public void runMetrics(String conversion) {
		json.addAdvancedMetric("CLA", getConnectivityLevelBetweenActivities(), this.numberProcess);
		json.addAdvancedMetric("CLP", getConnectivityLevelBetweenPartecipants(),  this.numberProcess);
		json.addAdvancedMetric("PDOPin", getProportionOfIncomingDataObjectsAndTotalDataObjects(), this.numberProcess);
		json.addAdvancedMetric("PDOPout", getProportionOfOutgoingDataObjectsAndTotalDataObjects(), this.numberProcess);
		json.addAdvancedMetric("TNT", getTotalNumberOfTasks(), this.numberProcess);
		json.addAdvancedMetric("VOL", getVolume(), this.numberProcess);
		json.addAdvancedMetric("RRPA", getRatioRolesActivities(), this.numberProcess);
		json.addAdvancedMetric("PDOTout", getProportionOfDataObjectsAsOutgoingProducts(), this.numberProcess);
		json.addAdvancedMetric("PLT", getProportionOfLanesAndTasks(), this.numberProcess);
		json.addAdvancedMetric("TNCS", getNumberOfCollapsedSubProcesses(), this.numberProcess);
		//json.addAdvancedMetric("TNA", getTotalNumberOfActivities());
		json.addAdvancedMetric("TNDO", getTotalNumberOfDataObjects(), this.numberProcess);
		json.addAdvancedMetric("TNG", getTotalNumberOfGateways(), this.numberProcess);
		json.addAdvancedMetric("S(df)", getDataFlowSize(), this.numberProcess);
		json.addAdvancedMetric("C(df)", getDataFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("TNEE", getTotalNumberOfEndEvents(), this.numberProcess);
		json.addAdvancedMetric("TNIE", getTotalNumberOfIntermediateEvents(), this.numberProcess);
		json.addAdvancedMetric("TNSE", getTotalNumberOfStartEvents(), this.numberProcess);
		json.addAdvancedMetric("TNE", getTotalNumberOfEvents(), this.numberProcess);
		json.addAdvancedMetric("CFC", getControlFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("CFC(rel)", getRelativeControlFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("PCFC", getParallelControlFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("JC", getJoinComplexity(), this.numberProcess);
		json.addAdvancedMetric("JSR", getJoinSplitRatio(), this.numberProcess);
		json.addAdvancedMetric("PC", getProcessComplexity(), this.numberProcess);
		json.addAdvancedMetric("PFC", getProcessFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("CADAC", getArcCognitiveComplexity(), this.numberProcess);
		json.addAdvancedMetric("NOAC", getNumberOfActivitiesAndControlFlowElements(), this.numberProcess);
		json.addAdvancedMetric("NOAJS", this.getNumberOfActivitiesJoinsAndSplits(), this.numberProcess);
		json.addAdvancedMetric("HPC_D", getHalsteadBasedProcessComplexityDifficulty(), this.numberProcess);
		json.addAdvancedMetric("HPC_N", getHalsteadBasedProcessComplexityLength(), this.numberProcess);
		json.addAdvancedMetric("HPC_V", getHalsteadBasedProcessComplexityVolume(), this.numberProcess);
		json.addAdvancedMetric("NoI", getNumberOfActivityInputs(), this.numberProcess);
		json.addAdvancedMetric("AAI", getAverageActivityInput(), this.numberProcess);
		json.addAdvancedMetric("NoO", getNumberOfActivityOutputs(), this.numberProcess);
		json.addAdvancedMetric("AAO", getAverageActivityOutput(), this.numberProcess);
		json.addAdvancedMetric("Length", getActivityLength(), this.numberProcess);
		json.addAdvancedMetric("IC", getInterfaceComplexityOfActivityMetric(), this.numberProcess);
		json.addAdvancedMetric("FIO", getStructuralComplexity(), this.numberProcess);
		json.addAdvancedMetric("NOF", getNumberOfControlFlow(), this.numberProcess);	
		json.addAdvancedMetric("TNSF", getTotalNumberOfSequenceFlow(), this.numberProcess);	
		json.addAdvancedMetric("CC", ccExtractor.calculateCrossConnectivity(), this.numberProcess);
		json.addAdvancedMetric("ICP",getImportedCouplingOfProcess(), this.numberProcess);
		json.addAdvancedMetric("ECP",getExportedCouplingOfProcess(), this.numberProcess);
		json.addAdvancedMetric("W", cwExtractor.getCognitiveWeight(), this.numberProcess);
		json.addAdvancedMetric("MaxND", ndExtractor.getMaxNestingDepth(), this.numberProcess);
		json.addAdvancedMetric("CP", getProcessCoupling(), this.numberProcess);
		json.addAdvancedMetric("WCP", getWeightedProcessCoupling(), this.numberProcess);
		json.addAdvancedMetric("Par", getParallelism(), this.numberProcess);
		json.addAdvancedMetric("CNC^2", this.getCoefficientOfNetworkComplexity(), this.numberProcess);
		json.addAdvancedMetric("CNC", this.getCoefficientComplexity(), this.numberProcess);
		json.addAdvancedMetric("NCA", this.getActivityCoupling(), this.numberProcess);
		json.addAdvancedMetric("MeanND", ndExtractor.getMeanNestingDepth(), this.numberProcess);
		json.addAdvancedMetric("Sn", getNumberOfNodes(), this.numberProcess);
		json.addAdvancedMetric("Sequentiality", getSequentiality(), this.numberProcess);
		json.addAdvancedMetric("diam", sizeExtractor.getDiam(), this.numberProcess);
		json.addAdvancedMetric("Depth", partExtractor.getDepth(), this.numberProcess);
//		json.addAdvancedMetric("Structuredness", partExtractor.getStructuredness());
		json.addAdvancedMetric("CYC", this.sccExtractor.getCyclicity(), this.numberProcess);
		json.addAdvancedMetric("TS", this.getTokenSplit(), this.numberProcess);
		json.addAdvancedMetric("Density", getDensity(), this.numberProcess);
		json.addAdvancedMetric("ACD", this.getAverageConnectorDegree(), this.numberProcess);
		json.addAdvancedMetric("MCD", this.getMaximumConnectorDegree(), this.numberProcess);
		json.addAdvancedMetric("GM", this.connectorInterplayMetricsExtractor.getGatewaysMismatchMetric(), this.numberProcess);
		json.addAdvancedMetric("CH", this.connectorInterplayMetricsExtractor.getConnectorsHeterogeneityMetric(), this.numberProcess);
		json.addAdvancedMetric("ECaM", this.getExtendedCardosoMetric(), this.numberProcess);
		json.addAdvancedMetric("ECyM", this.sccExtractor.getEcym(), this.numberProcess);
		json.addAdvancedMetric("PF", this.sccExtractor.getCycle(), this.numberProcess);
		json.addAdvancedMetric("DSM", dsmExtractor.getDurfeeMetric(), this.numberProcess);
		json.addAdvancedMetric("PSM", dsmExtractor.getPerfectSquareMetric(), this.numberProcess);
		json.addAdvancedMetric("Layout_Complexity", dsmExtractor.getLayoutComplexityMetric(), this.numberProcess);
		json.addAdvancedMetric("Layout_Measure", this.lmExtractor.getLayoutMeasure(), this.numberProcess);
		//System.out.println("JSON adv: " + this.json.getString());
		GraphMatrixes gm = this.mc.convertModel(this.basicMetricsExtractor.getProcess(), conversion);
		BindingStructure b = new BindingStructure(gm.getEdge(),gm.getVertix());
	    this.dopExtractor.setDop(this.basicMetricsExtractor.getProcess());
		this.json.addAdvancedMetric("DOP", this.dopExtractor.getDop(), this.numberProcess);
		this.json.addAdvancedMetric("B", (double) Math.round(b.getB() * 1000d) / 1000d, this.numberProcess);
	}

	/**
	 * Metric: CLA
	 * Total Number of Activities / Number of Sequence Flows between these Activities 
	 * (CLA = TNA / NSFA)
	 * @return 
	 */
	public float getConnectivityLevelBetweenActivities() {
		try {
			float toReturn = (float)basicMetricsExtractor.getActivities() /  basicMetricsExtractor.getSequenceFlowsBetweenActivities();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			} 
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
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
			float toReturn = (float)basicMetricsExtractor.getMessageFlows() / basicMetricsExtractor.getPools();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
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
			float toReturn = (float)basicMetricsExtractor.getDataObjectsInput() / getTotalNumberOfDataObjects();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
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
			float toReturn = (float)basicMetricsExtractor.getDataObjectsOutput() / getTotalNumberOfDataObjects();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
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
			float toReturn = (float)basicMetricsExtractor.getDataObjectsOutput() / getTotalNumberOfTasks();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
		}
	}
	
	/**
	 * Metric: PLT
	 * Proportion of lanes and tasks
	 * Number of Lanes / Total number of Tasks (PLT = NL/TNT)
	 * @return
	 */
	public float getProportionOfLanesAndTasks() {
		try {
			float toReturn = (float)basicMetricsExtractor.getLanes() / getTotalNumberOfTasks();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
		}
	}
	
	/**
	 * Metric: PPT
	 * Proportion of pools and tasks
	 * Number of Pools / Total number of Tasks (PLT = NP/TNT)
	 * @return
	 */
	public float getProportionOfPoolsAndTasks() {
		try {
			float toReturn = (float)basicMetricsExtractor.getPools() / getTotalNumberOfTasks();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
		}
	}
	
	/**
	 * Metric: PLA
	 * Proportion of lanes and activities
	 * Number of Lanes / Total number of Activities (PLA = NL/A)
	 * @return
	 */
	public float getProportionOfLanesAndActivities() {
		try {
			float toReturn = (float)basicMetricsExtractor.getLanes() / basicMetricsExtractor.getActivities();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
		}
	}
	
	/**
	 * Metric: PPA
	 * Proportion of pools and activities
	 * Number of Pools / Total number of Activities (PPA = NP/A)
	 * @return
	 */
	public float getProportionOfPoolsAndActivities() {
		try {
			float toReturn = (float)basicMetricsExtractor.getPools() / basicMetricsExtractor.getActivities();
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
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
	/*public int getTotalNumberOfActivities() {
		return getTotalNumberOfTasks() + getNumberOfCollapsedSubProcesses();
	}*/
	
	
	
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
	 * Control-flow Complexity metric. It captures a weighted sum of all split connectors that are used in a process model.
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
		/*for (ModelElementInstance activity : activities) {
			if (((FlowNode) activity).getOutgoing().size() > 1) {
				toReturn ++;
				System.out.println("no:" + toReturn);
			}
		}*/
		return toReturn;
		
	}
	
	/**
	 * Metric: JC
	 * Join Complexity metric. It captures a weighted sum of all join connectors that are used in a process model.
	 * @return
	 */
	public double getJoinComplexity() {
		double toReturn = 0;
		int tempSize = 0;
		Collection<ModelElementInstance> exclusiveGateways = basicMetricsExtractor.getCollectionOfElementType(ExclusiveGateway.class);
		Collection<ModelElementInstance> inclusiveGateways = basicMetricsExtractor.getCollectionOfElementType(InclusiveGateway.class);
		Collection<ModelElementInstance> parallelGateways = basicMetricsExtractor.getCollectionOfElementType(ParallelGateway.class);
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		//The JC of a xor-split is given by the number of his incoming flows
		for (ModelElementInstance exGateway : exclusiveGateways) {
			if (((FlowNode) exGateway).getIncoming().size() > 1)
				toReturn += ((FlowNode) exGateway).getIncoming().size();
		}
		
		//The JC of an or-split is given by the formula 2^n - 1, where n is equals to the number of the gateway's incoming flow
		for (ModelElementInstance inGateway : inclusiveGateways) {
			if (((FlowNode) inGateway).getIncoming().size() > 1) {
				tempSize = ((FlowNode) inGateway).getIncoming().size();
				toReturn += Math.pow(2, tempSize) - 1;
			}
		}
		//The JC of an and-split is just 1
		for (ModelElementInstance parGateway : parallelGateways) {
			if (((FlowNode) parGateway).getIncoming().size() > 1) {
				toReturn++;
			}
		}
		//Uncontrolled splits are included in the calculation as and-splits
		/*for (ModelElementInstance activity : activities) {
			if (((FlowNode) activity).getIncoming().size() > 1) {
				toReturn ++;
			}
		}*/
		return toReturn;
	}
	
	/**
	 * Metric: S(df)
	 * The sum of input data received by an activity plus the number of output data produced by the same activity minus the outgoing arcs
	 * towards the end event
	 * @return
	 */
	public int getDataFlowSize() {
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		int toReturn = 0;
		for (ModelElementInstance activity : activities) {
			toReturn+=((FlowNode) activity).getIncoming().size()+((FlowNode) activity).getOutgoing().size();
		}
		toReturn = toReturn - basicMetricsExtractor.getEndEvents();
		return toReturn;
	}
	
	/**
	 * Metric: C(df)
	 * The sum of input data received by an activity plus the number of output data produced by the same activity minus the outgoing arcs
	 * towards the end event plus the number of gateways
	 * @return
	 */
	public int getDataFlowComplexity() {
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		int toReturn = 0;
		for (ModelElementInstance activity : activities) {
			toReturn+=((FlowNode) activity).getIncoming().size()+((FlowNode) activity).getOutgoing().size();
		}
		toReturn = toReturn - basicMetricsExtractor.getEndEvents() + this.getTotalNumberOfGateways();
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
		double toReturn = 0;
		try {
			toReturn = (getNumberOfUniqueElements() / 2) * ((double)basicMetricsExtractor.getDataObjects() / getNumberOfUniqueDataObjects());
		} catch (ArithmeticException e) {
		}
		if (Double.isFinite(toReturn)) {
			return toReturn;
		} else {
			return 0.0;
		}
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
	 * Metric: AAI
	 *  Average activity inputs, Total Number of Activity input / Total Number of Activity
	 * @return 
	 */
	public double getAverageActivityInput() {
		try {
			double result = (double)this.getNumberOfActivityInputs()/this.basicMetricsExtractor.getActivities();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.getNumberOfActivityInputs()/this.basicMetricsExtractor.getActivities();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
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
	 * Metric: AAO
	 *  Average activity inputs, Total Number of Activity output / Total Number of Activity
	 * @return 
	 */
	public double getAverageActivityOutput() {
		try {
			double result = (double)this.getNumberOfActivityOutputs()/this.basicMetricsExtractor.getActivities();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.getNumberOfActivityOutputs()/this.basicMetricsExtractor.getActivities();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: PC
	 *  Process Complexity is obtained by adding the CFC to the Nesting Depth
	 * @return 
	 */
	public double getProcessComplexity() {
		try {
		
			return (double)(this.getControlFlowComplexity() + this.partExtractor.getDepth());
		} 
		catch (ArithmeticException e) {
			return 0;	
		}

	}
	
	/**
	 * Metric: CADAC
	 * Give the Cognitive Activity Depth Arc Control Flow
	 * @return 
	 */
	public double getArcCognitiveComplexity() {
		try {
		
			return (double)(this.basicMetricsExtractor.getActivities() + (this.ndExtractor.getMaxNestingDepth()) * 14) + (basicMetricsExtractor.getCollectionOfElementType(InclusiveGateway.class).size()*7) + (basicMetricsExtractor.getCollectionOfElementType(ExclusiveGateway.class).size()*2) + (basicMetricsExtractor.getCollectionOfElementType(ParallelGateway.class).size()*4) + (this.getStructuralComplexity()*4) + (this.getTotalNumberOfSequenceFlow());
		} 
		catch (ArithmeticException e) {
			return 0;	
		}

	}	
	
	/**
	 * Metric: PFC
	 *  Process Complexity is obtained by multiply the CFC to the another version of the FIO metrics (fan-in + fan-out)
	 * @return 
	 */
	public double getProcessFlowComplexity() {
		try {
		
			return (double) (this.getControlFlowComplexity()*(Math.pow(this.getNumberOfActivityInputs()+this.getNumberOfActivityOutputs(),2)));
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: Length
	 * Activity length. The length is 1 if the activity is a black box; if it is a white box,
	 *  the length can be calculated using traditional software engineering metrics
	 *  that have been previously presented, namely the LOC (line of code) and
	 *  MCC (McCabe's cyclomatic complexity).
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
	 * Metric: FIO
	 * Structural Complexity FIO = (NoI * NoO)^2,  NoI and NoO are 
	 *  the number of inputs and outputs.
	 * @return
	 */
	public double getStructuralComplexity() {
		return Math.pow((getNumberOfActivityInputs() * getNumberOfActivityOutputs()), 2);
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
	 * Metric: VOL
	 * Volume is the structural size of the process (TNSF + Sn)
	 * @return
	 */
	public int getVolume(){
		return (basicMetricsExtractor.getSequenceFlows()+basicMetricsExtractor.getFlowNodes());
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
			try {
				if (((MessageFlow) mMessageFlow).getSource() instanceof Activity) {
					toReturn++;
				}
			}catch(Exception e) {continue;}
			try {
				if (((MessageFlow) mMessageFlow).getSource() instanceof Participant) {
					toReturn++;
				}
			}catch(Exception e) {continue;}
			
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
			try {
				if (((MessageFlow) mMessageFlow).getTarget() instanceof Activity) {
					toReturn++;
				}
			}catch(Exception e) {continue;}
			try {
				if (((MessageFlow) mMessageFlow).getTarget() instanceof Participant) {
					toReturn++;
				}
			}catch(Exception e) {continue;}
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
					try {
						if (s.getTarget() instanceof Activity){
							toReturn++;
						}
					}catch(Exception e) {continue;}
				}
			}
			toReturn = toReturn/(activities.size() * activities.size() - 1);
		}
		return toReturn;
	}
	
	/**
	 * Metric: WCP
	 * The metric calculates the degree of coupling with weight assigned to connectors. Coupling is related to the number of interconnections among the
	 * tasks of a process model.
	 * @return
	 */
	//Calculate the normal Coupling Value
	public float getWeightedProcessCoupling(){
		float toReturn = 0;
		Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
		if (activities.size() > 1){
			for (ModelElementInstance t: activities){
				Collection<SequenceFlow> outgoing = ((FlowNode) t).getOutgoing();
				for (SequenceFlow s: outgoing){
					try {
						if (s.getTarget() instanceof Activity){
							toReturn++;
						}
					}catch(Exception e) {continue;}
				}
			}
		}
		// Calculate the value for Parallel Gateways
		if (activities.size() > 1){
			for (ModelElementInstance t: activities){
				Collection<SequenceFlow> outgoing = ((FlowNode) t).getOutgoing();
				for (SequenceFlow s: outgoing){
					try {
						if (s.getTarget() instanceof ParallelGateway){
							Collection<SequenceFlow> Paralleloutgoing = ((FlowNode) s.getTarget()).getOutgoing();
							for (SequenceFlow g: Paralleloutgoing){
								try {
									if (g.getTarget() instanceof Activity){
										toReturn++;
									}
								}catch(Exception e) {continue;}
							}
						}
					}catch(Exception e) {continue;}
				}
			}
		}
		// Calculate the value for Exclusive Gateways
		if (activities.size() > 1){
			for (ModelElementInstance t: activities){
				Collection<SequenceFlow> outgoing = ((FlowNode) t).getOutgoing();
				for (SequenceFlow s: outgoing){
					try {
						if (s.getTarget() instanceof ExclusiveGateway){
							Collection<SequenceFlow> Exclusiveoutgoing = ((FlowNode) s.getTarget()).getOutgoing();
							for (SequenceFlow g: Exclusiveoutgoing){
								try {
									if (g.getTarget() instanceof Activity){
										
										float Ris1=((FlowNode) s.getTarget()).getIncoming().size();
										float Ris2=((FlowNode) s.getTarget()).getOutgoing().size();
										toReturn=(float) (toReturn + 1/(Ris1*Ris2));
									}
								}catch(Exception e) {continue;}
							}
						}
					}catch(Exception e) {continue;}
				}
			}
		}
		// Calculate the value for Inclusive Gateways
		if (activities.size() > 1){
			for (ModelElementInstance t: activities){
				Collection<SequenceFlow> outgoing = ((FlowNode) t).getOutgoing();
				for (SequenceFlow s: outgoing){
					try {
						if (s.getTarget() instanceof InclusiveGateway){
							Collection<SequenceFlow> Inclusiveoutgoing = ((FlowNode) s.getTarget()).getOutgoing();
							for (SequenceFlow g: Inclusiveoutgoing){
								try {
									if (g.getTarget() instanceof Activity){
										float Ris1=((FlowNode) s.getTarget()).getIncoming().size();
										float Ris2=((FlowNode) s.getTarget()).getOutgoing().size();
										float RisBranch = (float)((Math.pow(2,Ris1)-1)*(Math.pow(2,Ris2)-1));
										toReturn= toReturn + (((float)1/((float)Ris1*(float)Ris2)) * (((float)RisBranch-(float)1)/(float)RisBranch)) + ((float)1/(float)RisBranch);
									}
								}catch(Exception e) {continue;}
							}
						}
					}catch(Exception e) {continue;}
				}
			}
		}
		toReturn = toReturn/(activities.size() * activities.size() - 1);
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
	 * Metric: CNC^2
	 * Coefficient of Network Complexity (total number of sequence flows(NSEQF*NSEQF)/NOAJS)
	 * @return NSEQF/NOAJS
	 */
	public double getCoefficientOfNetworkComplexity() {
		try {
			double result = (double)this.getNumberOfControlFlow()*this.getNumberOfControlFlow()/this.getNumberOfActivitiesJoinsAndSplits();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.getNumberOfControlFlow()*this.getNumberOfControlFlow()/this.getNumberOfActivitiesJoinsAndSplits();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: CNC
	 * Coefficient of Complexity (total number of sequence flows(NSEQF)/NOAJS)
	 * @return NSEQF/NOAJS
	 */
	public double getCoefficientComplexity() {
		try {
			double result = (double)this.getNumberOfControlFlow()/this.getNumberOfActivitiesJoinsAndSplits();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.getNumberOfControlFlow()/this.getNumberOfActivitiesJoinsAndSplits();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: CFC(rel)
	 * the Relative CFC is obtanied by divide the original CFC by the number of split
	 * @return CFC/FlowDividingTasks
	 */
	public double getRelativeControlFlowComplexity() {
		try {
			double result = (double)this.getControlFlowComplexity()/basicMetricsExtractor.getFlowDividingGateways();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.getControlFlowComplexity()/basicMetricsExtractor.getFlowDividingGateways();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	
	/**
	 * Metric: PCFC
	 * the Parallel CFC is obtanied by the sum of all outgoing arcs from splits
	 * @return CFC/FlowDividingTasks
	 */
	public int getParallelControlFlowComplexity() {
		Collection<ModelElementInstance> parallelGateways = basicMetricsExtractor.getCollectionOfElementType(ParallelGateway.class);
		int toReturn=1;
		try {
			for (ModelElementInstance parGateway : parallelGateways) {
				if (((FlowNode) parGateway).getOutgoing().size() > 1) {
					toReturn = toReturn * ((FlowNode) parGateway).getOutgoing().size();
				}
			}
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
		if (toReturn == 1)
			toReturn--;
		return toReturn;
	}
	
	
	/**
	 * Metric: Par
	 * Calculate the degree of Parallelism
	 * @return TNT/Diam
	 */
	public double getParallelism() {
		try {
			double result = (double)this.getTotalNumberOfTasks()/this.sizeExtractor.getDiam();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return  (double) this.getTotalNumberOfTasks()/this.sizeExtractor.getDiam();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: NCA
	 * Activity Coupling (total number of sequence flows NOAJS/NSEQF)
	 * @return NOAJS/NSEQF
	 */
	public double getActivityCoupling() {
		try {
			double result = (double)this.getNumberOfActivitiesJoinsAndSplits()/this.getNumberOfControlFlow();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.getNumberOfActivitiesJoinsAndSplits()/this.getNumberOfControlFlow();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: JSR
	 * Ratio between the join complexity, and the CFC by cardoso
	 * @return JSR = JC/SC
	 */
	public double getJoinSplitRatio() {
		try {
			double result = (double)this.getJoinComplexity()/this.getControlFlowComplexity();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.getJoinComplexity()/this.getControlFlowComplexity();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: RRPA
	 * Ratio of Roles and Activities
	 * @return NP/NACT
	 */
	public double getRatioRolesActivities() {
		try {
			double result = (double)basicMetricsExtractor.getPools()/basicMetricsExtractor.getActivities();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)basicMetricsExtractor.getPools()/basicMetricsExtractor.getActivities();
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
			try {
				if (flow.getSource() instanceof Gateway || flow.getTarget() instanceof Gateway) {
					arcBetweenNonConnectorsNode--;
				}
			}catch(Exception e) {continue;}
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