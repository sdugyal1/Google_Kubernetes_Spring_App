package interview.jfrog.sse.codingchallenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import interview.jfrog.sse.codingchallenge.response.PopularFilesResponse;
import interview.jfrog.sse.codingchallenge.service.JFrogService;



@RestController
public class JFrogRestController implements JFrogRestClient {

	
	@Autowired
	JFrogService JFrogService;
	
	@Override
	public ResponseEntity<PopularFilesResponse> get(String repo,
			String sort, String limit) {
		ResponseEntity<PopularFilesResponse> responseEntity = new ResponseEntity<PopularFilesResponse>(JFrogService.getMostDownloadedFiles(
				 repo,sort,limit),HttpStatus.OK);
		return responseEntity;
	}	


}
