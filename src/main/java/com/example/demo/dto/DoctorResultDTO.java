package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.example.demo.constants.DoctorIndexConstants;

public class DoctorResultDTO{

	public final String doctorID;
	public final String doctorName;
	public final double relevance;
	public final double price;
	
	private DoctorResultDTO(String doctorID,String doctorName,double doctorScore,double price){
		this.doctorID = doctorID;
		this.doctorName=doctorName;
		this.relevance=doctorScore;
		this.price=price;
	}
	
	public static List<DoctorResultDTO> getSearchResult(SearchResponse response) {
		
		List<DoctorResultDTO> searchResponse= new ArrayList<>();
		
		for (SearchHit hit : response.getHits()) {
			Map<String,Object> sourceMap=hit.getSourceAsMap();
			searchResponse.add(new DoctorResultDTO(hit.getId(), (String)sourceMap.get(DoctorIndexConstants.NAME), hit.getScore(), (Double)sourceMap.get(DoctorIndexConstants.PRICE)));			
		}
		
		return searchResponse;
	}

}
