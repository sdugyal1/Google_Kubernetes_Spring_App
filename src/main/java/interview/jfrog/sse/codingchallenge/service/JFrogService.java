package interview.jfrog.sse.codingchallenge.service;

import interview.jfrog.sse.codingchallenge.response.PopularFilesResponse;

public interface JFrogService {
	
	public PopularFilesResponse getMostDownloadedFiles(String repo,
			String sort, String limit);

}
