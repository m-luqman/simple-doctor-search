package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.SearchService;

@RestController
public class SearchController {

	@Autowired
	SearchService searchService;
	
	@PostMapping(path = "/doctor/search")
	public String search(@RequestBody SearchRequestDTO searchSpecification) throws IOException {
		return searchService.getSearchResult(searchSpecification,"doctor");
	}
}
