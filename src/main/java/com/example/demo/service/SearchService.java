package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.DoctorResultDTO;
import com.example.demo.dto.SearchRequestDTO;
import com.example.demo.mapper.Mapper;
import com.example.demo.repository.SearchRepository;

@Service
public class SearchService {

	
	public DoctorResultDTO getSearchResult(SearchRequestDTO searchSpecification,String indexName) throws Throwable {
    	
    	Mapper doctorMapper=Mapper.getDoctorMapper();
    	return DoctorResultDTO.getSearchResult(
    				new SearchRepository
    			   .SearchResultBuilder()
    			   .with(builder->{
			    		builder.index=indexName;
			    		builder.limit=searchSpecification.getLimit();
			    		builder.offset=searchSpecification.getOffset();
			    		builder.filters=searchSpecification.getFilters();
			    		builder.sortOrder=searchSpecification.getSortOrder();
			    		builder.sortField=searchSpecification.getSortField();
			    		builder.sortMap=doctorMapper.SORT_FIELD_MAP;
			    		builder.sortOrderMap=doctorMapper.SORT_ORDER_MAP;
			    		builder.queryMap=doctorMapper.QUERY_MAP;
	    			 })
	    			.getSearchResult());
    }
}
