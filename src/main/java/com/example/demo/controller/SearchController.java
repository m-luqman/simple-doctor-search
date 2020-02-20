package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.DoctorIndexConstants;
import com.example.demo.dto.DoctorResultDTO;
import com.example.demo.dto.SearchRequestDTO;
import com.example.demo.service.SearchService;

@RestController
public class SearchController {

	@Autowired
	SearchService searchService;
	
	@PostMapping(path = "/doctor/search")
	public List<DoctorResultDTO> search(@RequestBody SearchRequestDTO searchSpecification) throws Throwable {
		return searchService.getSearchResult(searchSpecification,DoctorIndexConstants.INDEX);
	}
}
