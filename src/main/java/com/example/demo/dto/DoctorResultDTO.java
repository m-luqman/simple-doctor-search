package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.example.demo.constants.DoctorIndexConstants;

public class DoctorResultDTO{

	public List<Map<String,List<String>>> data;
	public Map<String,List<String>> metadata;
	
	private DoctorResultDTO(List<Map<String,List<String>>> data,Map<String,List<String>> metadata){
		this.data=data;
		this.metadata=metadata;
	}
	
	public static DoctorResultDTO getSearchResult(SearchResponse response) {
		List<Map<String,List<String>>> data=new ArrayList<>();		
		Map<String,List<String>> metadata=new HashMap<>();
		metadata.put("total_doctors", Arrays.asList(Long.toString(response.getHits().totalHits)));

		for (SearchHit hit : response.getHits()) {
			Map<String,List<String>> datum=new HashMap<>();
			Map<String, Object> sourceMap=hit.getSourceAsMap();
			datum.put("doctor_ID", Arrays.asList(hit.getId()));
			datum.put("doctor_name", Arrays.asList(sourceMap.get(DoctorIndexConstants.NAME).toString()));
			datum.put("relevance",  Arrays.asList(sourceMap.get(DoctorIndexConstants.RELEVANCE).toString()));
			datum.put("score", Arrays.asList(Float.toString( hit.getScore())));
			datum.put("price", Arrays.asList(sourceMap.get(DoctorIndexConstants.PRICE).toString()));
			datum.put("rating", Arrays.asList(sourceMap.get(DoctorIndexConstants.RATING).toString()));
			data.add(datum);
		}
		
		return new DoctorResultDTO(data,metadata);
	}
}
