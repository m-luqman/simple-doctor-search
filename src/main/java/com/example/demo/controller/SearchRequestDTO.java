package com.example.demo.controller;
import java.util.List;
import java.util.Map;

public class SearchRequestDTO{
	Map<String,List<String>> filters;
	int limit;
	int offset;
	public Map<String, List<String>> getFilters() {
		return filters;
	}
	public void setFilters(Map<String, List<String>> filters) {
		this.filters = filters;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
