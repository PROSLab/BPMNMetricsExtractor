package extractorRestAPI;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import bpmnMetadataExtractor.BpmnModelReader;

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
		// exception handling
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
			@FormDataParam("extraction") String extraction) {
		// exception handling
		BpmnModelReader metricsExtractor = new BpmnModelReader(conversion, extraction);
		//String fileName = fileDetail.getFileName().substring(0, fileDetail.getFileName().lastIndexOf('.'));
		String fileName = "ExtractedMetadata";
		String json = metricsExtractor.getJsonMetrics(uploadedInputStream, fileName);
		return  json;
	}
}


