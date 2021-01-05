package bpmnMetadataExtractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.DataObject;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.InclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.Process;

import metricsOnGraph.BindingStructure;
import metricsOnGraph.Diameter;
import metricsOnGraph.ProcessBreadth;
import graphElements.GraphMatrixes;
import graphElements.ModelConverter;
import graphElements.StartEndNodesCollector;
import metricsOnGraph.ComplexityIndex;
import metricsOnGraph.TreesNumber;
import metricsOnGraph.RestrictivenessEstimator;
import metricsOnGraph.Separability;
import metricsOnGraph.AggregateIndicator;
import graphElements.CycleDetector;
import graphElements.GraphAdjacencyList;
import metricsOnGraph.StructureDiversity;

public class BpmnAdvancedMetricsExtractor {
	
	//modello da cui estrarre le metriche
	private BpmnBasicMetricsExtractor basicMetricsExtractor;
	private JsonEncoder json;
	private CrossConnectivityMetricExtractor ccExtractor;
	private ConnectorInterplayMetricsExtractor connectorInterplayMetricsExtractor;
	private DurfeeSquareMetricExtractor dsmExtractor;
	private PartitionabilityMetricsExtractor partExtractor;
	//private SizeMetricsExtractor sizeExtractor;
	private NestingDepthMetricsExtractor ndExtractor;
	private StronglyConnectedComponentsMetricExtractor sccExtractor;
	private CognitiveWeightMetricExtractor cwExtractor;
	private LayoutMetricsExtractor lmExtractor;
	private ModelConverter mc;
	private DOPMetricsExtractor dopExtractor;
	private int numberProcess;
	private StructurednessMetricExtractor F;
	
	public BpmnAdvancedMetricsExtractor(ModelConverter mc, BpmnBasicMetricsExtractor basicMetricsExtractor, JsonEncoder jsonEncoder, int i) {
		this.basicMetricsExtractor = basicMetricsExtractor;
		this.json = jsonEncoder;
		this.ccExtractor = new CrossConnectivityMetricExtractor(basicMetricsExtractor);
		this.connectorInterplayMetricsExtractor = new ConnectorInterplayMetricsExtractor(basicMetricsExtractor);
		this.dsmExtractor = new DurfeeSquareMetricExtractor(basicMetricsExtractor);
		this.partExtractor = new PartitionabilityMetricsExtractor(basicMetricsExtractor);
		//this.sizeExtractor = new SizeMetricsExtractor(basicMetricsExtractor);
		this.ndExtractor = new NestingDepthMetricsExtractor(basicMetricsExtractor);
		this.sccExtractor = new StronglyConnectedComponentsMetricExtractor(basicMetricsExtractor);
		this.cwExtractor = new CognitiveWeightMetricExtractor(basicMetricsExtractor);
		this.lmExtractor = new LayoutMetricsExtractor(basicMetricsExtractor);
		this.mc = mc;
		this.numberProcess = i;
	}
	
	public void runMetrics(String conversion) {
		json.addAdvancedMetric("NOAC", getNumberOfActivitiesAndControlFlowElements());
		json.addAdvancedMetric("NOAJS", this.getNumberOfActivitiesJoinsAndSplits());
		json.addAdvancedMetric("VOL", getVolume());
		json.addAdvancedMetric("CNC^2", this.getCoefficientOfNetworkComplexity());
		json.addAdvancedMetric("CNC", this.getCoefficientComplexity());
		json.addAdvancedMetric("PLT", getProportionOfLanesAndTasks());
		json.addAdvancedMetric("PLA", getProportionOfLanesAndActivities());
		json.addAdvancedMetric("PPT", getProportionOfPoolsAndTasks());
		json.addAdvancedMetric("PPA", getRatioRolesActivities());
		json.addAdvancedMetric("CLA", getConnectivityLevelBetweenActivities());
		json.addAdvancedMetric("DEN", getDensity());
		json.addAdvancedMetric("CLP", getConnectivityLevelBetweenPartecipants());
		json.addAdvancedMetric("PDOPin", getProportionOfIncomingDataObjectsAndTotalDataObjects());
		json.addAdvancedMetric("PDOPout", getProportionOfOutgoingDataObjectsAndTotalDataObjects());
		json.addAdvancedMetric("PDOTout", getProportionOfDataObjectsAsOutgoingProducts());
		json.addAdvancedMetric("AAI", getAverageActivityInput());
		json.addAdvancedMetric("AAO", getAverageActivityOutput());
		json.addAdvancedMetric("FIO", getStructuralComplexity());
		json.addAdvancedMetric("IC", getInterfaceComplexityOfActivityMetric());
		json.addAdvancedMetric("CFC", getControlFlowComplexity());
		json.addAdvancedMetric("JC", getJoinComplexity());
		json.addAdvancedMetric("CFC(rel)", getRelativeControlFlowComplexity());
		json.addAdvancedMetric("JSR", getJoinSplitRatio());
		json.addAdvancedMetric("PFC", getProcessFlowComplexity());
		json.addAdvancedMetric("PCFC", getParallelControlFlowComplexity());
		json.addAdvancedMetric("ACD", this.getAverageConnectorDegree());
		json.addAdvancedMetric("MCD", this.getMaximumConnectorDegree());
		json.addAdvancedMetric("SEQ", getSequentiality());
		json.addAdvancedMetric("TS", this.getTokenSplit());
		json.addAdvancedMetric("DEPTH", partExtractor.getDepth());
		json.addAdvancedMetric("MaxND", ndExtractor.getMaxNestingDepth());
		json.addAdvancedMetric("MeanND", ndExtractor.getMeanNestingDepth());
		json.addAdvancedMetric("PC", getProcessComplexity());
		json.addAdvancedMetric("CH", this.connectorInterplayMetricsExtractor.getConnectorsHeterogeneityMetric());
		json.addAdvancedMetric("GM", this.connectorInterplayMetricsExtractor.getGatewaysMismatchMetric());
		json.addAdvancedMetric("ICP",getImportedCouplingOfProcess());
		json.addAdvancedMetric("ECP",getExportedCouplingOfProcess());
		json.addAdvancedMetric("CP", getProcessCoupling());
		json.addAdvancedMetric("WCP", getWeightedProcessCoupling());
		json.addAdvancedMetric("PF", this.sccExtractor.getCycle());
		json.addAdvancedMetric("CYC", this.sccExtractor.getCyclicity());
		json.addAdvancedMetric("ECyM", this.sccExtractor.getEcym());
		json.addAdvancedMetric("HPC_D", getHalsteadBasedProcessComplexityDifficulty());
		json.addAdvancedMetric("HPC_N", getHalsteadBasedProcessComplexityLength());
		json.addAdvancedMetric("HPC_V", getHalsteadBasedProcessComplexityVolume());	
		json.addAdvancedMetric("CC", ccExtractor.calculateCrossConnectivity());
		json.addAdvancedMetric("DSM", dsmExtractor.getDurfeeMetric());
		json.addAdvancedMetric("PSM", dsmExtractor.getPerfectSquareMetric());
		json.addAdvancedMetric("W", cwExtractor.getCognitiveWeight());
		json.addAdvancedMetric("CADAC", getArcCognitiveComplexity());
		json.addAdvancedMetric("LC", dsmExtractor.getLayoutComplexityMetric());
		json.addAdvancedMetric("LM", this.lmExtractor.getLayoutMeasure());
		//System.out.println("JSON adv: " + this.json.getString());
		GraphMatrixes gm = this.mc.convertEntireModel(conversion);
		if(gm != null) {
			GraphAdjacencyList gal = new GraphAdjacencyList(gm.getAdjacencyMatrix());
			StartEndNodesCollector senc = new StartEndNodesCollector(gm.getAdjacencyMatrix(), gal.getAdj());
			boolean cyclical = new CycleDetector(gal.getAdj()).isCyclic();
			//compute all metrics on graph
			BindingStructure b = new BindingStructure(gm.getEdge(),gm.getVertix());
			this.json.addAdvancedMetric("B", b.getB());
			Diameter l = new Diameter(gm.getAdjacencyMatrix(), senc.getInitialNodes());
			this.json.addAdvancedMetric("L", l.getL());
			StructureDiversity d = new StructureDiversity(gm.getReachabilityMatrix(), gal.getAdj(), senc.getInitialNodes(), senc.getFinalNodes());
			this.json.addAdvancedMetric("D", d.getD());
			AggregateIndicator ac = new AggregateIndicator(l.getL(),b.getB(),d.getD());
			this.json.addAdvancedMetric("AC", ac.getAC());
			RestrictivenessEstimator rt = new RestrictivenessEstimator(gm.getVertix(),gm.getReachabilityMatrix());
			this.json.addAdvancedMetric("RT", rt.getRT());
			TreesNumber t = new TreesNumber(gm.getAdjacencyMatrix());
			this.json.addAdvancedMetric("T", t.getT());
			Separability s = new Separability(gm.getAdjacencyMatrix());
			//compute metrics on acyclical graph
			this.dopExtractor = new DOPMetricsExtractor("WhiteBox");
			if(!cyclical) {
				ProcessBreadth pb = new ProcessBreadth(gm.getReachabilityMatrix(), gal.getAdj(), senc.getInitialNodes(), senc.getFinalNodes());
				//sizeExtractor moved here, it uses adjacency list
				SizeMetricsExtractor sizeExtractor = new SizeMetricsExtractor(gm.getVertix(), gal.getAdj());
				for(Process p : mc.getProcesses())
					this.dopExtractor.setDop(p);
				this.json.addAdvancedMetric("DOP", this.dopExtractor.getDop());
				ComplexityIndex ci = new ComplexityIndex(gm.getAdjacencyMatrix(), gal.getAdj());
				this.json.addAdvancedMetric("CI",ci.getCI());
				this.json.addAdvancedMetric("PB", pb.getProcessBreadth());
				json.addAdvancedMetric("DIAM", sizeExtractor.getDiam());
				json.addAdvancedMetric("PAR", getParallelism(sizeExtractor.getDiam()));
				}
			json.addAdvancedMetric("MCC", this.getMCC(gm.getEdge(), gm.getVertix()));
			this.json.addAdvancedMetric("SEP",s.getSeparability());
			this.F = new StructurednessMetricExtractor(basicMetricsExtractor, conversion);
			json.addAdvancedMetric("F", this.F.getS());
		}
		json.addAdvancedMetric("DE", this.getDuplicatedElements());
	}
	
	public void runMetricsProcess(String conversion) {
		json.addAdvancedMetric("NOAC", getNumberOfActivitiesAndControlFlowElements(), this.numberProcess);
		json.addAdvancedMetric("NOAJS", this.getNumberOfActivitiesJoinsAndSplits(), this.numberProcess);
		json.addAdvancedMetric("VOL", getVolume(), this.numberProcess);
		json.addAdvancedMetric("CNC^2", this.getCoefficientOfNetworkComplexity(), this.numberProcess);
		json.addAdvancedMetric("CNC", this.getCoefficientComplexity(), this.numberProcess);
		json.addAdvancedMetric("PLT", getProportionOfLanesAndTasks(), this.numberProcess);
		json.addAdvancedMetric("PPT", getProportionOfPoolsAndTasks(), this.numberProcess);
		json.addAdvancedMetric("PLA", getProportionOfLanesAndActivities(), this.numberProcess);
		json.addAdvancedMetric("PPA", getRatioRolesActivities(), this.numberProcess);
		json.addAdvancedMetric("CLA", getConnectivityLevelBetweenActivities(), this.numberProcess);
		json.addAdvancedMetric("DEN", getDensity(), this.numberProcess);
		json.addAdvancedMetric("CLP", getConnectivityLevelBetweenPartecipants(),  this.numberProcess);
		json.addAdvancedMetric("PDOPin", getProportionOfIncomingDataObjectsAndTotalDataObjects(), this.numberProcess);
		json.addAdvancedMetric("PDOPout", getProportionOfOutgoingDataObjectsAndTotalDataObjects(), this.numberProcess);
		json.addAdvancedMetric("PDOTout", getProportionOfDataObjectsAsOutgoingProducts(), this.numberProcess);
		json.addAdvancedMetric("AAI", getAverageActivityInput(), this.numberProcess);
		json.addAdvancedMetric("AAO", getAverageActivityOutput(), this.numberProcess);
		json.addAdvancedMetric("FIO", getStructuralComplexity(), this.numberProcess);
		json.addAdvancedMetric("IC", getInterfaceComplexityOfActivityMetric(), this.numberProcess);
		json.addAdvancedMetric("CFC", getControlFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("JC", getJoinComplexity(), this.numberProcess);
		json.addAdvancedMetric("CFC(rel)", getRelativeControlFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("JSR", getJoinSplitRatio(), this.numberProcess);
		json.addAdvancedMetric("PFC", getProcessFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("PCFC", getParallelControlFlowComplexity(), this.numberProcess);
		json.addAdvancedMetric("ACD", this.getAverageConnectorDegree(), this.numberProcess);
		json.addAdvancedMetric("MCD", this.getMaximumConnectorDegree(), this.numberProcess);
		json.addAdvancedMetric("SEQ", getSequentiality(), this.numberProcess);
		json.addAdvancedMetric("TS", this.getTokenSplit(), this.numberProcess);
		json.addAdvancedMetric("DEPTH", partExtractor.getDepth(), this.numberProcess);
		json.addAdvancedMetric("MaxND", ndExtractor.getMaxNestingDepth(), this.numberProcess);
		json.addAdvancedMetric("MeanND", ndExtractor.getMeanNestingDepth(), this.numberProcess);
		json.addAdvancedMetric("PC", getProcessComplexity(), this.numberProcess);
		json.addAdvancedMetric("CH", this.connectorInterplayMetricsExtractor.getConnectorsHeterogeneityMetric(), this.numberProcess);
		json.addAdvancedMetric("GM", this.connectorInterplayMetricsExtractor.getGatewaysMismatchMetric(), this.numberProcess);
		json.addAdvancedMetric("ICP",getImportedCouplingOfProcess(), this.numberProcess);
		json.addAdvancedMetric("ECP",getExportedCouplingOfProcess(), this.numberProcess);
		json.addAdvancedMetric("CP", getProcessCoupling(), this.numberProcess);
		json.addAdvancedMetric("WCP", getWeightedProcessCoupling(), this.numberProcess);
		json.addAdvancedMetric("PF", this.sccExtractor.getCycle(), this.numberProcess);
		json.addAdvancedMetric("CYC", this.sccExtractor.getCyclicity(), this.numberProcess);
		json.addAdvancedMetric("ECyM", this.sccExtractor.getEcym(), this.numberProcess);
		json.addAdvancedMetric("HPC_D", getHalsteadBasedProcessComplexityDifficulty(), this.numberProcess);
		json.addAdvancedMetric("HPC_N", getHalsteadBasedProcessComplexityLength(), this.numberProcess);
		json.addAdvancedMetric("HPC_V", getHalsteadBasedProcessComplexityVolume(), this.numberProcess);
		json.addAdvancedMetric("CC", ccExtractor.calculateCrossConnectivity(), this.numberProcess);		
		json.addAdvancedMetric("DSM", dsmExtractor.getDurfeeMetric(), this.numberProcess);
		json.addAdvancedMetric("PSM", dsmExtractor.getPerfectSquareMetric(), this.numberProcess);
		json.addAdvancedMetric("W", cwExtractor.getCognitiveWeight(), this.numberProcess);
		json.addAdvancedMetric("CADAC", getArcCognitiveComplexity(), this.numberProcess);
		json.addAdvancedMetric("LC", dsmExtractor.getLayoutComplexityMetric(), this.numberProcess);
		json.addAdvancedMetric("LM", this.lmExtractor.getLayoutMeasure(), this.numberProcess);
		//System.out.println("JSON adv: " + this.json.getString());
		GraphMatrixes gm = this.mc.convertModel(this.basicMetricsExtractor.getProcess(), conversion);
		if(gm != null) {
			GraphAdjacencyList gal = new GraphAdjacencyList(gm.getAdjacencyMatrix());
			StartEndNodesCollector senc = new StartEndNodesCollector(gm.getAdjacencyMatrix(), gal.getAdj());
			boolean cyclical = new CycleDetector(gal.getAdj()).isCyclic();
			//compute all metrics on graph
			BindingStructure b = new BindingStructure(gm.getEdge(),gm.getVertix());
			this.json.addAdvancedMetric("B", b.getB(), this.numberProcess);
			Diameter l = new Diameter(gm.getAdjacencyMatrix(), senc.getInitialNodes());
			this.json.addAdvancedMetric("L", l.getL(), this.numberProcess);
			StructureDiversity d = new StructureDiversity(gm.getReachabilityMatrix(), gal.getAdj(), senc.getInitialNodes(), senc.getFinalNodes());
			this.json.addAdvancedMetric("D", d.getD(), this.numberProcess);
			AggregateIndicator ac = new AggregateIndicator(l.getL(),b.getB(),d.getD());
			this.json.addAdvancedMetric("AC", ac.getAC(), this.numberProcess);
			RestrictivenessEstimator rt = new RestrictivenessEstimator(gm.getVertix(),gm.getReachabilityMatrix());
			this.json.addAdvancedMetric("RT", rt.getRT(), this.numberProcess);
			TreesNumber t = new TreesNumber(gm.getAdjacencyMatrix());
			this.json.addAdvancedMetric("T", t.getT(), this.numberProcess);
			Separability s = new Separability(gm.getAdjacencyMatrix());
			//compute metrics on acyclical graph
			this.dopExtractor = new DOPMetricsExtractor(conversion);
			if(!cyclical) {
				ProcessBreadth pb = new ProcessBreadth(gm.getReachabilityMatrix(), gal.getAdj(), senc.getInitialNodes(), senc.getFinalNodes());
				//sizeExtractor moved here, it uses adjacency list
				SizeMetricsExtractor sizeExtractor = new SizeMetricsExtractor(gm.getVertix(), gal.getAdj());
				this.dopExtractor.setDop(this.basicMetricsExtractor.getProcess());
				this.json.addAdvancedMetric("DOP", this.dopExtractor.getDop(), this.numberProcess);
				ComplexityIndex ci = new ComplexityIndex(gm.getAdjacencyMatrix(), gal.getAdj());
				this.json.addAdvancedMetric("CI",ci.getCI(), this.numberProcess);
				this.json.addAdvancedMetric("PB", pb.getProcessBreadth(), this.numberProcess);
				json.addAdvancedMetric("DIAM", sizeExtractor.getDiam(), this.numberProcess);
				json.addAdvancedMetric("PAR", getParallelism(sizeExtractor.getDiam()), this.numberProcess);
				}
			json.addAdvancedMetric("MCC", this.getMCC(gm.getEdge(), gm.getVertix()), this.numberProcess);
			this.json.addAdvancedMetric("SEP", s.getSeparability(), this.numberProcess);
			this.F = new StructurednessMetricExtractor(basicMetricsExtractor, conversion);
			json.addAdvancedMetric("F", this.F.getS(), this.numberProcess);
		}
		json.addAdvancedMetric("DE", this.getDuplicatedElements(), this.numberProcess);
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
			float toReturn = (float)basicMetricsExtractor.getDataObjectsInput() / (basicMetricsExtractor.getDataObjectsInput() + basicMetricsExtractor.getDataObjectsOutput());
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
			float toReturn = (float)basicMetricsExtractor.getDataObjectsOutput() / (basicMetricsExtractor.getDataObjectsInput() + basicMetricsExtractor.getDataObjectsOutput());
			if (Float.isFinite(toReturn)) {
				return toReturn;
			}
			return 0.0f;
		} catch (ArithmeticException e) {
			return 0.0f;
		}
	}
	
	/**
	 * Metric: PDOTout
	 * Proportion of data objects as outgoing product of activities of the model
	 * Number of data objects which are outputs of activities / Total number of Tasks (PDOTOut = NDOOut/TNT)
	 * @return
	 */
	public float getProportionOfDataObjectsAsOutgoingProducts() {
		try {
			float toReturn = (float)basicMetricsExtractor.getDataObjectsOutput() / (basicMetricsExtractor.getTasks());
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
			float toReturn = (float)basicMetricsExtractor.getLanes() / basicMetricsExtractor.getTasks();
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
	 * Number of Pools / Total number of Tasks (PPT = NP/TNT)
	 * @return
	 */
	public float getProportionOfPoolsAndTasks() {
		try {
			if(basicMetricsExtractor.getPools() > 0) {
				float toReturn = (float)basicMetricsExtractor.getPools() / basicMetricsExtractor.getTasks();
				if (Float.isFinite(toReturn)) {
					return toReturn;
				}
				return 0.0f;
			} else {
				float toReturn = (float) 1 / basicMetricsExtractor.getTasks();
				if (Float.isFinite(toReturn)) {
					return toReturn;
				}
				return 0.0f;
			}
			
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
		//Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
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
		//Collection<ModelElementInstance> activities = basicMetricsExtractor.getCollectionOfElementType(Activity.class);
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
			toReturn = ((double)getNumberOfUniqueElements() / 2) * ((double)basicMetricsExtractor.getDataObjects() / getNumberOfUniqueDataObjects());
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
			toReturn = (getNumberOfElements() + basicMetricsExtractor.getDataObjects()) 
			* (logBase2(getNumberOfUniqueElements() + getNumberOfUniqueDataObjects()));
		} catch (ArithmeticException e) {
		}
		if (Double.isFinite(toReturn))
			return toReturn;
		else 
			return 0.0;
	}
	
	/**
	 * Metric: AAI
	 *  Average activity inputs, Total Number of Activity input / Total Number of Activity
	 * @return 
	 */
	public double getAverageActivityInput() {
		try {
			double result = (double)this.basicMetricsExtractor.getDataInputAssociations()/this.basicMetricsExtractor.getActivities();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.basicMetricsExtractor.getDataInputAssociations()/this.basicMetricsExtractor.getActivities();
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
	}
	
	/**
	 * Metric: AAO
	 *  Average activity inputs, Total Number of Activity output / Total Number of Activity
	 * @return 
	 */
	public double getAverageActivityOutput() {
		try {
			double result = (double) this.basicMetricsExtractor.getDataOutputAssociations() / this.basicMetricsExtractor.getActivities();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double) this.basicMetricsExtractor.getDataOutputAssociations() / this.basicMetricsExtractor.getActivities();
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
			return (double)(this.getControlFlowComplexity() + this.ndExtractor.getMaxNestingDepth());
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
			return (double)(this.basicMetricsExtractor.getActivities() + (this.ndExtractor.getMaxNestingDepth()) * 14) + (basicMetricsExtractor.getCollectionOfElementType(InclusiveGateway.class).size()*7) + (basicMetricsExtractor.getCollectionOfElementType(ExclusiveGateway.class).size()*2) + (basicMetricsExtractor.getCollectionOfElementType(ParallelGateway.class).size()*4) + (this.getStructuralComplexity()*4) + (this.basicMetricsExtractor.getSequenceFlows());
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
			return (double) (this.getControlFlowComplexity()*(Math.pow(this.basicMetricsExtractor.getDataAssociations(),2)));
		} 
		catch (ArithmeticException e) {
			return 0;	
		}
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
		return this.basicMetricsExtractor.getActivities() * (this.getStructuralComplexity());
	}
	
	/**
	 * Metric: FIO
	 * Structural Complexity FIO = (NoI * NoO)^2,  NoI and NoO are 
	 *  the number of inputs and outputs.
	 * @return
	 */
	public double getStructuralComplexity() {
		return Math.pow((this.basicMetricsExtractor.getDataInputAssociations() * this.basicMetricsExtractor.getDataOutputAssociations()), 2);
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
			toReturn = toReturn/(activities.size() * (activities.size() - 1));
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
		toReturn = toReturn/(activities.size() * (activities.size() - 1));
		return toReturn;
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
			double result = (double)this.basicMetricsExtractor.getSequenceFlows()*this.basicMetricsExtractor.getSequenceFlows()/this.getNumberOfActivitiesJoinsAndSplits();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.basicMetricsExtractor.getSequenceFlows()*this.basicMetricsExtractor.getSequenceFlows()/this.getNumberOfActivitiesJoinsAndSplits();
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
			double result = (double)this.basicMetricsExtractor.getSequenceFlows()/this.getNumberOfActivitiesJoinsAndSplits();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)this.basicMetricsExtractor.getSequenceFlows()/this.getNumberOfActivitiesJoinsAndSplits();
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
	public double getParallelism(double diam) {
		try {
			double result = (double)this.basicMetricsExtractor.getFlowNodes()/diam;
			if (!Double.isFinite(result)) 
				return 0;
			else
				return  (double) this.basicMetricsExtractor.getFlowNodes()/diam;
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
	 * Metric: PPA
	 * Ratio of Roles and Activities
	 * @return NP/NACT
	 */
	public double getRatioRolesActivities() {
		try {
			if(basicMetricsExtractor.getPools() > 0) {
			double result = (double)basicMetricsExtractor.getPools()/basicMetricsExtractor.getActivities();
			if (!Double.isFinite(result)) 
				return 0;
			else
				return (double)basicMetricsExtractor.getPools()/basicMetricsExtractor.getActivities();
			} else {
				double result = (double) 1 / basicMetricsExtractor.getActivities();
				if (!Double.isFinite(result)) 
					return 0;
				else
					return (double) 1 / basicMetricsExtractor.getActivities();
			}
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
		if (basicMetricsExtractor.getStartCompensationEvents() + basicMetricsExtractor.getInterruptingBoundaryCompensationEvents() +
				basicMetricsExtractor.getIntermediateCompensationThrowEvents() + basicMetricsExtractor.getEndCompensationEvents() > 0)
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
			basicMetricsExtractor.getStartCompensationEvents() + basicMetricsExtractor.getInterruptingBoundaryCompensationEvents() +
			basicMetricsExtractor.getIntermediateCompensationThrowEvents() + basicMetricsExtractor.getEndCompensationEvents()  + 
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
	
	/**
	 * Metric: MCC
	 * McCabes Cyclomatic Number counts the number of linearly independent paths through a process 
	 * @return
	 */
	public int getMCC(int edges, int nodes) {
		return edges - nodes + 2;
	}
	
	/**
	 * Metric DE
	 * Duplicated elements is defined as the sum of participants, lanes, message flows or flow elements 
	 * (but not sequence flows) of the same type with the same label
	 * @return
	 */
	public int getDuplicatedElements() {
		int n = 0;
		Collection<ModelElementInstance> elements = this.basicMetricsExtractor.getCollectionOfElementType(FlowElement.class);
		Vector<String> ispectioned = new Vector<String>();
		for(ModelElementInstance original : this.basicMetricsExtractor.getCollectionOfElementType(FlowElement.class)) {
			if(((FlowElement) original).getName() != null && !((FlowElement) original).getName().trim().isEmpty() && !ispectioned.contains(((FlowElement) original).getName()))
				for(ModelElementInstance duplicated : elements)
					if(!(duplicated instanceof SequenceFlow))
						if(((FlowElement) original).getName().equals(((FlowElement) duplicated).getName()) 
								&& !((FlowElement) original).getId().equals(((FlowElement) duplicated).getId())) {
							n++;
							ispectioned.add(((FlowElement) original).getName());
						}
		}	
		elements = this.basicMetricsExtractor.getCollectionOfElementType(Participant.class);
		ispectioned = new Vector<String>();
		for(ModelElementInstance original : this.basicMetricsExtractor.getCollectionOfElementType(Participant.class)) {
			if(((Participant) original).getName() != null && !((Participant) original).getName().trim().isEmpty() && !ispectioned.contains(((Participant) original).getName()))
				for(ModelElementInstance duplicated : elements)
					if(((Participant) original).getName().equals(((Participant) duplicated).getName()) 
							&& !((Participant) original).getId().equals(((Participant) duplicated).getId())) {
							n++;
							ispectioned.add(((Participant) original).getName());
						}
		}	
		elements = this.basicMetricsExtractor.getCollectionOfElementType(Lane.class);
		ispectioned = new Vector<String>();
		for(ModelElementInstance original : this.basicMetricsExtractor.getCollectionOfElementType(Lane.class)) {
			if(((Lane) original).getName() != null && !((Lane) original).getName().trim().isEmpty() && !ispectioned.contains(((Lane) original).getName()))
				for(ModelElementInstance duplicated : elements)
					if(((Lane) original).getName().equals(((Lane) duplicated).getName()) 
							&& !((Lane) original).getId().equals(((Lane) duplicated).getId())) {
							n++;
							ispectioned.add(((Lane) original).getName());
						}
		}	
		elements = this.basicMetricsExtractor.getCollectionOfElementType(MessageFlow.class);
		ispectioned = new Vector<String>();
		for(ModelElementInstance original : this.basicMetricsExtractor.getCollectionOfElementType(MessageFlow.class)) {
			if(((MessageFlow) original).getName() != null && !((MessageFlow) original).getName().trim().isEmpty() && !ispectioned.contains(((MessageFlow) original).getName()))
				for(ModelElementInstance duplicated : elements)
					if(((MessageFlow) original).getName().equals(((MessageFlow) duplicated).getName()) 
							&& !((MessageFlow) original).getId().equals(((MessageFlow) duplicated).getId())) {
							n++;
							ispectioned.add(((MessageFlow) original).getName());
						}
		}	
		return n;
			
	}

}