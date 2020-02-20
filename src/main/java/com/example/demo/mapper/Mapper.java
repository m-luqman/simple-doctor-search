package com.example.demo.mapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.elasticsearch.search.sort.SortOrder;

import com.example.demo.constants.QueryType;
import com.example.demo.constants.SortType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Mapper {
	
    public Map<String, QueryType> QUERY_MAP;
    public Map<String, SortType> SORT_FIELD_MAP;
    public Map<String, SortOrder> SORT_ORDER_MAP;

    public Mapper() {
        super();
    }
    public Mapper(Map<String, QueryType> QUERY_MAP, Map<String, SortType> SORT_FIELD_MAP, Map<String,SortOrder> SORT_ORDER_MAP) {
    	this.QUERY_MAP=QUERY_MAP;
    	this.SORT_FIELD_MAP=SORT_FIELD_MAP;
    	this.SORT_ORDER_MAP=SORT_ORDER_MAP;
    }
    
	public static Mapper getDoctorMapper() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();
		return mapper.readValue(new File("src/main/java/com/example/demo/mapper/doctor.yml"), Mapper.class);		
	}

}
