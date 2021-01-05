package extractorRestAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import bpmnMetadataExtractor.BpmnModelReader;
import bpmnMetadataExtractor.JsonEncoder.Result;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/api")
public class MetricsExtractorRouter {
	@Path("/fileUpload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response extractMetrics(
			@FormDataParam("model") InputStream uploadedInputStream,
			@FormDataParam("model") FormDataContentDisposition fileDetail,
			@FormDataParam("conversion") String conversion,
			@FormDataParam("extraction") String extraction) {
		BpmnModelReader metricsExtractor = new BpmnModelReader(conversion, extraction);
		//String fileName = fileDetail.getFileName().substring(0, fileDetail.getFileName().lastIndexOf('.'));
		String fileName = "ExtractedMetadata";
		String json = metricsExtractor.getJsonMetrics(uploadedInputStream, fileName);
		return  Response.ok((Object)json).
				header("Content-Disposition","attachment; filename = " + fileName + ".json").
				build();
	}
	
	@Path("/results")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	public String getResults(
			@FormDataParam("model") InputStream uploadedInputStream,
			@FormDataParam("model") FormDataContentDisposition fileDetail,
			@FormDataParam("conversion") String conversion,
			@FormDataParam("extraction") String extraction) throws IOException {
		BpmnModelReader metricsExtractor = new BpmnModelReader(conversion, extraction);
		Vector<Result> results= metricsExtractor.getResultsMetrics(uploadedInputStream, "ExtractedMetadata");
		URL urlHeader = getClass().getClassLoader().getResource("header.html");
		URL urlFooter = getClass().getClassLoader().getResource("footer.html");
		String header = IOUtils.toString(urlHeader, "UTF-8");
		String footer = IOUtils.toString(urlFooter, "UTF-8");
		// it builds the string to return as html page with all advanced metrics
		String table = "";
		String alerts = "";
		for(Result r : results)
			table +="<tr><td>"+r.getProcess()+"</th><td>"+r.getID()+"</td><td>"+r.getInfo()+"</td><td>"+r.getValue()+"</td><tr>";
		table+="</tbody></table>";
		if(!metricsExtractor.getAlerts().isEmpty()) {
			alerts += "<h4 class = text-danger><label>Warnings:</label></h4><ul>";
			for(String s : metricsExtractor.getAlerts())
				alerts+="<li><p> <mark>"+s+"</mark></p></li>";
			alerts+="</ul>";
		}
		return  header+table+alerts+footer;
	}
}


