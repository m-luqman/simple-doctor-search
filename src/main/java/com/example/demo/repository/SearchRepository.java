package com.example.demo.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import com.example.demo.service.QueryDTO;
import com.example.demo.service.SortDTO;

@Repository
public class SearchRepository {
	
	private static SearchResponse search(SearchSourceBuilder searchSourceBuilder,String index) throws IOException {
		
		try(RestHighLevelClient client = new RestHighLevelClient(
		        RestClient.builder(
		                new HttpHost("localhost", 9200, "http"),
		                new HttpHost("localhost", 9201, "http")))){
						
			SearchRequest searchRequest = new SearchRequest(); 

			searchRequest.indices(index);

			searchRequest.source(searchSourceBuilder);
			
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			return searchResponse;
		}
	}
	
	static SearchSourceBuilder customQueryBuilder(List<QueryDTO> dtos,SearchSourceBuilder searchSourceBuilder) {

		Map<String,BiFunction<String,String,QueryBuilder>> mapper=new HashMap<>();        

        mapper.put("termQuery", QueryBuilders::termQuery);
        mapper.put("matchPhraseQuery",QueryBuilders::matchPhraseQuery);
        
        for (QueryDTO dto : dtos)
        	searchSourceBuilder=searchSourceBuilder.query(mapper.get(dto.type).apply(dto.key, dto.value));
        
        return searchSourceBuilder;
	}
	
	static SearchSourceBuilder customSortBuilder(List<SortDTO> dtos, SortDTO tieBreaker,SearchSourceBuilder searchSourceBuilder) {
        
		Map<String,SortOrder> mapper=new HashMap<>();        

        mapper.put("ascending", SortOrder.ASC);
        mapper.put("descending",SortOrder.DESC);

        for (SortDTO dto : dtos)
        	searchSourceBuilder=searchSourceBuilder.sort(new FieldSortBuilder(dto.key).order(mapper.get(dto.order)));

        searchSourceBuilder=searchSourceBuilder.sort(new FieldSortBuilder(tieBreaker.key).order(mapper.get(tieBreaker.order)));

        return searchSourceBuilder;
	}
	
	static SearchSourceBuilder baseBuilder(int limit,int offset) {
		return new SearchSourceBuilder().from(offset).size(limit);
	}
	static String getResultString(SearchResponse response) {
		String result="";

		for(SearchHit hit : response.getHits()) {
			result=result+hit+"\n";
		}
		
		return result;
	}
	public static class SearchResultBuilder {
	    public List<QueryDTO> queryDTOS;
	    public List<SortDTO> sortDTOS;
	    public SortDTO tieBreaker;
	    public int limit;
	    public int offset;
	    public String index;

	    public SearchResultBuilder with(Consumer<SearchResultBuilder> builderFunction) {
	        builderFunction.accept(this);
	        return this;
	    }

	    public String getSearchResult() throws IOException {
			return getResultString(search(customSortBuilder(sortDTOS,tieBreaker,customQueryBuilder(queryDTOS,baseBuilder(limit,offset))),index));
	    }
	}	
}
