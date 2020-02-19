package com.example.demo.service;

import java.util.Map;

public class Mapper {
	
    public Map<String, QueryType> QUERY_MAP;

    public Mapper() {
        super();
    }
    public Mapper(Map<String, QueryType> QUERY_MAP) {
    	this.QUERY_MAP=QUERY_MAP;
    }
}
