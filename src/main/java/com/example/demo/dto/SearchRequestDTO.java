package com.example.demo.dto;
import java.util.List;
import java.util.Map;

public class SearchRequestDTO{
	Map<String,List<String>> filters;
	String sort_field,sort_order;
	int limit=10;
	int offset=0;
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
	public String getSortField() {
		return sort_field;
	}
	public void setSortField(String sortField) {
		this.sort_field = sortField;
	}
	public String getSortOrder() {
		return sort_order;
	}
	public void setSortOrder(String sortOrder) {
		this.sort_order = sortOrder;
	}
}
