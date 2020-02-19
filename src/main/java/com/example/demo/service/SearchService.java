package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;

import com.example.demo.controller.SearchRequestDTO;
import com.example.demo.repository.SearchRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Service
public class SearchService {

	Mapper getDoctorMapper() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();
		return mapper.readValue(new File("src/main/resources/doctor.yml"), Mapper.class);		
	}
	
	public String getSearchResult(SearchRequestDTO searchSpecification,String indexName) throws IOException {
    	
    	Mapper doctorMapper=getDoctorMapper();
    	return new SearchRepository
    			   .SearchResultBuilder()
    			   .with(builder->{
			    		builder.index=indexName;
			    		builder.limit=searchSpecification.getLimit();
			    		builder.offset=searchSpecification.getOffset();
			    		builder.filters=searchSpecification.getFilters();
			    		builder.queryMap=doctorMapper.QUERY_MAP;
	    			 })
	    			.getSearchResult();
    }
}
