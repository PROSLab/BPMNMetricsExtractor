package bpmnMetadataExtractor;

import java.io.File;
//import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Participant;

import graphElements.ModelConverter;

import org.camunda.bpm.model.bpmn.Bpmn;

/**
 * Classe principale, in cui vengono richiamati il MetadataExtractor e, eventualmente, il DatabaseController, per l'estrazione delle metriche e loro salvataggio, a partire da un file bpmn
 * @author PROSLabTeam
 *
 */
public class BpmnModelReader {
	//file da cui vengono lette le metriche
	private File loadedFile;
	private String conversionType;
	private String extractionType;
	
	/**
	 * Costruttore in cui viene instanziato il loadedFile tramite il suo path
	 * @param filePath
	 */
	public BpmnModelReader(String filePath) {
		loadedFile = new File(filePath);
	}
	
	public BpmnModelReader() {
		
	}
	
	public BpmnModelReader(String filePath, String c, String e) {
		loadedFile = new File(filePath);
		this.conversionType = c;
		this.extractionType = e;
	}
	
	public BpmnModelReader(String c, String e) {
		this.conversionType = c;
		this.extractionType = e;
	}
	
	/**
	 * Getter di loadedFile
	 * @return il file caricato
	 */
	public File getLoadedFile() {
		return loadedFile;
	}
	
	/**
	 * Setter di loadedFile
	 * @param filePath: il path in cui si trova il nuovo file
	 */
	public void setLoadedFile(String filePath) {
		this.loadedFile = new File(filePath);
	}
	
	public String getJsonMetrics(InputStream fileStream, String fileName) {
		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(fileStream);
		JsonEncoder jsonEncoder = new JsonEncoder(fileName);
		int numberProcess = 0;
		if(this.extractionType.equals("Process")){
			Collection<Participant> participant = modelInstance.getModelElementsByType(Participant.class);
			for(Participant p: participant) {
				jsonEncoder.buildJSON(numberProcess, p.getId(), p.getName(), p.getProcess().getId());
				BpmnBasicMetricsExtractor basicExtractor = new BpmnBasicMetricsExtractor(modelInstance, p.getProcess(), jsonEncoder, numberProcess, this.extractionType);
				BpmnAdvancedMetricsExtractor advExtractor = new BpmnAdvancedMetricsExtractor(new ModelConverter(modelInstance), basicExtractor, jsonEncoder, numberProcess);
				numberProcess++;
				basicExtractor.runMetricsProcess();
				advExtractor.runMetricsProcess(this.conversionType);
			}
			jsonEncoder.populateHeader(LocalDateTime.now(), participant.size());
		} else {
			jsonEncoder.buildJSON(numberProcess);
			BpmnBasicMetricsExtractor basicExtractor = new BpmnBasicMetricsExtractor(modelInstance, jsonEncoder, numberProcess, this.extractionType);
			BpmnAdvancedMetricsExtractor advExtractor = new BpmnAdvancedMetricsExtractor(new ModelConverter(modelInstance), basicExtractor, jsonEncoder, numberProcess);
			basicExtractor.runMetrics();
			advExtractor.runMetrics(this.conversionType);
			jsonEncoder.populateHeader(LocalDateTime.now());
		}
		/*MySqlInterface db = new MySqlInterface();
		db.connect();
		db.saveMetrics(jsonEncoder);
		db.closeConnection();*/
		return jsonEncoder.getJson().toString();
	}
	
}