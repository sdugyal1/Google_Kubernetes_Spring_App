package interview.jfrog.sse.codingchallenge.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interview.jfrog.sse.codingchallenge.connection.ArtifactoryConnection;
import interview.jfrog.sse.codingchallenge.response.PopularFilesResponse;
import interview.jfrog.sse.codingchallenge.response.Result;
import interview.jfrog.sse.codingchallenge.response.StatsResponse;
import interview.jfrog.sse.codingchallenge.service.JFrogService;

@Service
public class JFrogServiceImpl implements JFrogService{

	@Autowired
	ArtifactoryConnection artifactoryConnection;
	
	@Override
	public PopularFilesResponse getMostDownloadedFiles(String repo,
			String sort, String limit) {
		Map<StatsResponse, Integer> resultMap = new HashMap<StatsResponse, Integer>();
		PopularFilesResponse popularFilesResponse = new PopularFilesResponse();
		
		try {
			List<Result> resultList = artifactoryConnection.searchRepo();
			resultList.forEach(result -> 
			{
				try {
					StatsResponse statsResponse = artifactoryConnection.getDownloadStats(result.getRepo(),result.getPath(),result.getName());
					resultMap.put(statsResponse, statsResponse.getDownloadCount());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<StatsResponse, Integer> sortedByPrice = resultMap.entrySet().stream()
				.sorted(Map.Entry.<StatsResponse, Integer>comparingByValue().reversed())
				.limit(Integer.valueOf(StringUtils.defaultIfEmpty(limit, "2")))
				.collect(Collectors.toMap(e -> e.getKey(),e -> e.getValue()));
		
		popularFilesResponse.setResults(new ArrayList<StatsResponse>(sortedByPrice.keySet()));
		return popularFilesResponse;
	}

}
