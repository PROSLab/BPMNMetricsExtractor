package bpmnMetadataExtractor;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.camunda.bpm.engine.impl.util.json.*;
/**
 * 
 * @author PROSLabTeam
 *	Classe per la creazione di un file .json per esportare le metriche estratte 
 */
public class JsonEncoder {
	
	private JSONObject json;
	private String fileName;
	private Map<String, String[]> metricsInfos; 
	private Vector<Result> results;
	
	/**
	 * Costruttore per l'encoder Json
	 */
	public JsonEncoder(String fileName){
		json = new JSONObject();
		this.fileName = fileName;
		this.results = new Vector<Result>();
		this.initializeJSON();
		this.initializeMetricsInfos();
	}

	public JSONObject getJson() {
		return json;
	}
	
	/**
	 * Si inizializza il JSONObject per avere il seguente formato:
	 * {
	 * "header":{},
	 * "Basic Metrics":{},
	 * "Advanced Metrics":{}
	 * }
	 */
	/*private void initializeJSON(){
		JSONObject header = new JSONObject();
		JSONObject basicMetrics = new JSONObject();
		JSONObject advancedMetrics = new JSONObject();
		this.json.put("header", header).put("basic_metrics", basicMetrics).put("advanced_metrics", advancedMetrics);
		this.json.getJSONObject("basic_metrics").put("NT", 0);
	}*/
	
	private void initializeJSON(){
		JSONObject header = new JSONObject();
		JSONObject process = new JSONObject();
		this.json.put("header", header).put("process", process);
	}
	
	/*
	 * Method that builds JSON for process type extraction
	 */
	public void buildJSON(int i, String id, String name, String processId){
		JSONObject newProcess = new JSONObject();
		JSONObject basicMetrics = new JSONObject();
		JSONObject advancedMetrics = new JSONObject();
		JSONObject participant = new JSONObject();
		newProcess.put("basic_metrics", basicMetrics).put("advanced_metrics", advancedMetrics);
		participant.put("id", id);
		participant.put("name", name);
		participant.put("process", processId);
		newProcess.put("participant", participant);
		this.json.getJSONObject("process").put(Integer.toString(i), newProcess);
		//to access nested elements of json object using getJSONArray method
		this.json.getJSONObject("process").getJSONObject(Integer.toString(i)).getJSONObject("basic_metrics").put("NT", 0);
	}
	
	/*
	 * Method that builds JSON for model type extraction
	 */
	public void buildJSON(int i){
		JSONObject newProcess = new JSONObject();
		JSONObject basicMetrics = new JSONObject();
		JSONObject advancedMetrics = new JSONObject();
		newProcess.put("basic_metrics", basicMetrics).put("advanced_metrics", advancedMetrics);
		this.json.getJSONObject("process").put(Integer.toString(i), newProcess);
		//to access nested elements of json object using getJSONArray method
		this.json.getJSONObject("process").getJSONObject(Integer.toString(i)).getJSONObject("basic_metrics").put("NT", 0);
	}
	
	private void initializeMetricsInfos() {
		String path = "";
		try {
			path = Paths.get(this.getClass().getClassLoader().getResource("metriche_complete.txt").toURI()).toString();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		//System.out.println(path);
		metricsInfos = new HashMap<String, String[]>();
		List<String> metrics = new ArrayList<String>();
		try {
			metrics = Files.readAllLines(Paths.get(path), StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("Error reading metrics file");
		}
		String name, desc, source;
		String infos[];
		for (String m : metrics) {
			infos = new String[2];
			name = m.substring(0, m.indexOf('%'));
			name = name.trim();
			desc = m.substring(m.indexOf('%') + 1, m.indexOf('{'));
			desc = desc.trim();
			source = m.substring(m.indexOf('{') + 1, m.length() - 1);
			source = source.trim();
			infos[0] = desc;
			infos[1] = source;
			metricsInfos.put(name, infos);
		}
	}
	
	/**
	 * Metodo per aggiungere le metriche di base al json
	 * @param metricName nome della metrica da aggiungere
	 * @param n numero delle metriche
	 */
	public void addBasicMetric(String metricName, int n){
		JSONObject basicMetric = new JSONObject();
		String metricInfos[] = metricsInfos.get(metricName);
		basicMetric.put("value", n);
		basicMetric.put("description", metricInfos[0]);
		basicMetric.put("source", metricInfos[1]);
		//edited
		this.json.getJSONObject("process").getJSONObject("0").getJSONObject("basic_metrics").put(metricName, basicMetric);
	}
	
	/*
	 * Method that adds basic metrics to JSON for process type extraction
	 */
	public void addBasicMetric(String metricName, int n, int i){
		JSONObject basicMetric = new JSONObject();
		String metricInfos[] = metricsInfos.get(metricName);
		basicMetric.put("value", n);
		basicMetric.put("description", metricInfos[0]);
		basicMetric.put("source", metricInfos[1]);
		this.json.getJSONObject("process").getJSONObject(Integer.toString(i)).getJSONObject("basic_metrics").put(metricName, basicMetric);
	}
	
	/**
	 * Metodo per aggiungere le metriche avazate al json
	 * @param metricName nome della metrica da aggiungere
	 * @param n numero delle metriche
	 */
	public void addAdvancedMetric(String metricName, double n){
		JSONObject advMetric = new JSONObject();
		String metricInfos[] = metricsInfos.get(metricName);
		if (!Double.isFinite(n))
			n = 0;
		n = Math.round(n * 1000.0) / 1000.0;
		advMetric.put("value", n);
		advMetric.put("description", metricInfos[0]);
		advMetric.put("source", metricInfos[1]);
		//edited
		this.json.getJSONObject("process").getJSONObject("0").getJSONObject("advanced_metrics").put(metricName, advMetric);
		this.results.add(new Result("N/A", metricName, metricInfos[0], Double.toString(n)));
	}
	
	/*
	 * Method that adds advanced metrics to JSON for process type extraction
	 */
	public void addAdvancedMetric(String metricName, double n, int i){
		JSONObject advMetric = new JSONObject();
		String metricInfos[] = metricsInfos.get(metricName);
		if (!Double.isFinite(n))
			n = 0;
		n = Math.round(n * 1000.0) / 1000.0;
		advMetric.put("value", n);
		advMetric.put("description", metricInfos[0]);
		advMetric.put("source", metricInfos[1]);
		this.json.getJSONObject("process").getJSONObject(Integer.toString(i)).getJSONObject("advanced_metrics").put(metricName, advMetric);
		String process = this.json.getJSONObject("process").getJSONObject(Integer.toString(i)).getJSONObject("participant").getString("process");
		this.results.add(new Result(process, metricName, metricInfos[0], Double.toString(n)));
	}
	
	public ArrayList<String> getBasicMetricsNames() {
		//TODO
		JSONArray namesArray = this.json.getJSONObject("basic_metrics").names();
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < namesArray.length(); ++i) {
			names.add(namesArray.getString(i));
        }
		return names;
	}
	
	public ArrayList<String>  getAdvancedMetricsNames() {
		//TODO
		JSONArray namesArray = this.json.getJSONObject("advanced_metrics").names();
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < namesArray.length(); ++i) {
			names.add(namesArray.getString(i));
        }
		return names;
	}
	
	public ArrayList<Integer> getBasicMetricsValues() {
		//TODO
		JSONObject basicMetrics = this.json.getJSONObject("basic_metrics");
		JSONArray namesArray = basicMetrics.names();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < namesArray.length(); ++i) {
			values.add(basicMetrics.getJSONObject(namesArray.getString(i)).getInt("value"));
        }
		return values;
	}
	
	public ArrayList<Double> getAdvancedMetricsValues() {
		//TODO
		JSONObject advancedMetrics = this.json.getJSONObject("advanced_metrics");
		JSONArray namesArray = advancedMetrics.names();
		ArrayList<Double> values = new ArrayList<Double>();
		for (int i = 0; i < namesArray.length(); ++i) {
			values.add(advancedMetrics.getJSONObject(namesArray.getString(i)).getDouble("value"));
        }
		return values;
	}
	
	public ArrayList<String> getHeaderValues() {
		JSONObject header = this.json.getJSONObject("header");
		JSONArray namesArray = header.names();
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < namesArray.length(); ++i) {
			values.add(header.getString(namesArray.getString(i)));
        }
		return values;
	}
	
	public String getModelId() {
		JSONObject header = this.json.getJSONObject("header");
		return header.getString("id");
	}

	/**
	 * 
	 * @return file json
	 */
	public String getString(){
		return this.json.toString();
	}
	
	/**
	 * Metodo per esportare il json in un file il cui nome contiene il nome del modello bpmn e un timestamp
	 */
	public void exportJson(){
		LocalDateTime now = LocalDateTime.now();
		String outputFileName = fileName + " - " + now.getDayOfMonth() + "-" + now.getMonthValue() + "-" + now.getYear() + "--" + now.getHour() + "-" + now.getMinute();
		this.populateHeader(now);
		try(FileWriter file = new FileWriter(outputFileName + ".json"))
		{
			file.write(json.toString());
			file.flush();		
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void populateHeader(LocalDateTime now) {
		this.json.getJSONObject("header").put("file_name", fileName);
		String hour = appendLeadingZero(now.getHour());
		String minute = appendLeadingZero(now.getMinute());
		String second = appendLeadingZero(now.getSecond());
		String time = hour + ":" + minute + ":" + second;
		this.json.getJSONObject("header").put("creation_time", time);
		String month = appendLeadingZero(now.getMonthValue());
		String day = appendLeadingZero(now.getDayOfMonth());
		String date = now.getYear() + "-" + month + "-" + day;
		this.json.getJSONObject("header").put("creation_date", date);
		String id = createFileId(now);
		this.json.getJSONObject("header").put("id", id);
	}
	
	public void populateHeader(LocalDateTime now, int processNumber) {
		this.json.getJSONObject("header").put("file_name", fileName);
		String hour = appendLeadingZero(now.getHour());
		String minute = appendLeadingZero(now.getMinute());
		String second = appendLeadingZero(now.getSecond());
		String time = hour + ":" + minute + ":" + second;
		this.json.getJSONObject("header").put("creation_time", time);
		String month = appendLeadingZero(now.getMonthValue());
		String day = appendLeadingZero(now.getDayOfMonth());
		String date = now.getYear() + "-" + month + "-" + day;
		this.json.getJSONObject("header").put("creation_date", date);
		String id = createFileId(now);
		this.json.getJSONObject("header").put("id", id);
		this.json.getJSONObject("header").put("number of process", processNumber);
	}

	private String appendLeadingZero(int number) {
		return number < 10 ? "0" + Integer.toString(number) : Integer.toString(number);
	}

	/**
	 * Function that creates an ID based on date, time and file name.
	 * TODO Files created in the same moment, with the same letters in their name have the same ID
	 * @param now: LocalDateTime initialized during the json finalization
	 * @return the id for the file
	 */
	private String createFileId(LocalDateTime now) {
		int baseIdLen = 32;
		String idStr = Integer.toString(((int)(Math.floor(Math.random() * 50)))) + "_";
		idStr += Long.toString(System.currentTimeMillis()) + "_";
		do {
			idStr += Integer.toString(((int)(Math.floor(Math.random() * 35))));
		} while (idStr.length() < baseIdLen);
		return idStr;
	}
	
	public Vector<Result> getResults(){
		return this.results;
	}
	
	public class Result{
		private String process;
		private String id;
		private String description;
		private String value;
		
		Result(String p, String i, String d, String v){
			this.process = p;
			this.id = i;
			this.description = d;
			this.value = v;
		}
		
		public String getProcess() {
			return this.process;
		}
		
		public String getID() {
			return this.id;
		}
		
		public String getInfo() {
			return this.description;
		}
		
		public String getValue() {
			return this.value;
		}
		
	}
	
	
}
