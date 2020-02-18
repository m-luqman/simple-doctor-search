package com.example.demo.service;

import java.util.Map;

public class QueryString2DTO {
	
    public final Map<String, QueryDTO> QUERY_MAP;
    public final Map<String, SortDTO> SORT_MAP;

    public QueryString2DTO(Map<String, QueryDTO> QUERY_MAP,Map<String, SortDTO> SORT_MAP) {

    	this.QUERY_MAP=QUERY_MAP;
    	this.SORT_MAP=SORT_MAP;
	
    }
}
