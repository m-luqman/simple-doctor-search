package com.example.demo.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.SearchService;

@RestController
public class SearchController {

	@Autowired
	SearchService searchService;
	
	@GetMapping(path = "/{index}/search")
	public String search(
	@RequestParam Map<String,String> searchSpecification, ModelMap model, @PathVariable("index") String index) throws IOException {
		return searchService.getSearchResult(searchSpecification,index);
	}
}
