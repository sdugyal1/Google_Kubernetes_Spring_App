package interview.jfrog.sse.codingchallenge.connection;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ArtifactoryRequest;
import org.jfrog.artifactory.client.ArtifactoryResponse;
import org.jfrog.artifactory.client.impl.ArtifactoryRequestImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import interview.jfrog.sse.codingchallenge.response.Result;
import interview.jfrog.sse.codingchallenge.response.SearchResponse;
import interview.jfrog.sse.codingchallenge.response.StatsResponse;



@Component
public class ArtifactoryConnection {

	@Value("${artifactory.baseUrl}")
	private  String baseUrl;

	@Value("${artifactory.username}")
	private String userName;
	
	@Value("${artifactory.password}")
	private String userPassword;
	
	@Value("${artifactory.searchUrl}")
	private String searchUrl;

	@Value("${artifactory.statsUrl}")
	private String statsUrl;
	
	private  Artifactory artifactory;


	@PostConstruct
    public void createArtifactoryClient(){
	
		
		artifactory = ArtifactoryClientBuilder.create().setUsername(userName).setPassword(userPassword).setUrl(baseUrl).build();
    }


	private  String getStatsUrl(String repo, String path, String fileName) {
		
		String updatedStatsUrl;
		updatedStatsUrl = StringUtils.replace(statsUrl, "$repo", repo);
		updatedStatsUrl = StringUtils.replace(updatedStatsUrl, "$path", path);
		updatedStatsUrl = StringUtils.replace(updatedStatsUrl, "$fileName", fileName);
		return updatedStatsUrl;
		
	}

	public List<Result> searchRepo() throws IOException {
		String aqlQuery = "items.find({\"repo\":{\"$eq\":\"libs-snapshot-local\"}})";

		ArtifactoryRequest aqlRequest = new ArtifactoryRequestImpl().method(ArtifactoryRequest.Method.POST)
				.apiUrl(searchUrl).requestType(ArtifactoryRequest.ContentType.TEXT).requestBody(aqlQuery)
				.responseType(ArtifactoryRequest.ContentType.JSON);
		ArtifactoryResponse artifactoryResponse = artifactory.restCall(aqlRequest);
		assertOnFailure(artifactoryResponse);
		SearchResponse searchResponse = artifactoryResponse.parseBody(SearchResponse.class);
		List<Result> resultList = searchResponse.getResults();
		return resultList;
	}
	
	
	public StatsResponse getDownloadStats(String repo,String path, String fileName) throws IOException {
		ArtifactoryRequest aqlRequest = new ArtifactoryRequestImpl()
            .method(ArtifactoryRequest.Method.GET)
            .apiUrl(getStatsUrl(repo,path,fileName))
            .requestType(ArtifactoryRequest.ContentType.TEXT)
            .responseType(ArtifactoryRequest.ContentType.JSON);
		ArtifactoryResponse artifactoryResponse =  artifactory.restCall(aqlRequest);
        assertOnFailure(artifactoryResponse);
        StatsResponse statsResponse = artifactoryResponse.parseBody(StatsResponse.class);
        return statsResponse;
	}
	
	static private void assertOnFailure(ArtifactoryResponse artifactoryResponse) {
        if (! artifactoryResponse.isSuccessResponse()) {
            throw new RuntimeException("Artifactory responded with: '${artifactoryResponse.getStatusLine()}'");
        }
    }
	
	
	


}