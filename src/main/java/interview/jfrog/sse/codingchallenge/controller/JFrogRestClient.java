package interview.jfrog.sse.codingchallenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import interview.jfrog.sse.codingchallenge.response.PopularFilesResponse;

public interface JFrogRestClient {
	
	
	@RequestMapping(value="/api/search/popular/downloads/{repo}", method = RequestMethod.GET)

	public @ResponseBody ResponseEntity<PopularFilesResponse> get(@PathVariable("repo") String repo,
			@RequestParam(value = "sort", required = false, defaultValue = "desc") String sort, @RequestParam(value = "limit", required = false, defaultValue = "2") String limit);


}