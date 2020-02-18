package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.repository.SearchRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Service
public class SearchService {

	private final Map<String,String> yamlIndexMapper;

	public SearchService() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();
		this.yamlIndexMapper = (Map<String,String>)mapper.readValue(new File("src/main/resources/index_mapper.yml"),Map.class);
	}

	QueryString2DTO getQS2DTO(String indexName) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();
		return mapper.readValue(new File(yamlIndexMapper.getOrDefault(indexName,"doctor")), QueryString2DTO.class);		
	}
	
	<T> List<T> getDTO (Map<String,T> dtoMapper, Map<String,String> searchSpecification){
    	
		return searchSpecification
		.values()
		.stream()
		.map(String::toLowerCase)
		.filter(value -> dtoMapper.containsKey(value))
		.map(key -> dtoMapper.get(key))
		.collect(Collectors.toList());		
	}
    	
	public String getSearchResult(Map<String,String> searchSpecification,String indexName) throws IOException {
    	
    	QueryString2DTO qs2dto=getQS2DTO(indexName);
    	List<QueryDTO> queryDTOS = getDTO(qs2dto.QUERY_MAP,searchSpecification);    	
    	List<SortDTO> sortDTOS = getDTO(qs2dto.SORT_MAP,searchSpecification);
    	int limit=Integer.parseInt(searchSpecification.getOrDefault("limit", "10"));
    	int offset=Integer.parseInt(searchSpecification.getOrDefault("offset", "0"));
    	SortDTO tieBreaker=qs2dto.SORT_MAP.getOrDefault(searchSpecification.getOrDefault("tie",""), qs2dto.SORT_MAP.get("relevance"));
    	
    	return new SearchRepository
    			   .SearchResultBuilder()
    			   .with(builder->{
			    		builder.index=indexName;
			    		builder.limit=limit;
			    		builder.offset=offset;
			    		builder.queryDTOS=queryDTOS;
			    		builder.sortDTOS=sortDTOS;
			    		builder.tieBreaker=tieBreaker;
	    			 })
	    			.getSearchResult();
    }
}
