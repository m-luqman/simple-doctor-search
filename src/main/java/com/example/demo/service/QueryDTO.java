package com.example.demo.service;


public class QueryDTO {
	public final String type;
	public final String key;
	public final String value;

	public QueryDTO(String type, String key,String value) {
		this.type=type;
		this.key=key;
		this.value=value;
	}
}
