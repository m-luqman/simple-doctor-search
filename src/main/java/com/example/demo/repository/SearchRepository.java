package com.example.demo.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import com.example.demo.service.QueryType;

@Repository
public class SearchRepository {
	
	private static final int FIRST=0;
	private static final int SECOND=1;
	
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
	
	static SearchSourceBuilder customQueryBuilder(Map<String,List<String>> filters,Map<String,QueryType> queryMap,SearchSourceBuilder searchSourceBuilder) {

        for (Entry <String,List<String>> entry : filters.entrySet())
            switch(queryMap.get(entry.getKey())) {
            	case TERM:
            		searchSourceBuilder=searchSourceBuilder.query(QueryBuilders.termQuery(entry.getKey(), entry.getValue().get(FIRST)));
            		break;
            	case PHRASE:
            		searchSourceBuilder=searchSourceBuilder.query(QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue().get(FIRST)));
            		break;
            	case RANGE:
            		searchSourceBuilder=searchSourceBuilder
            		.query(QueryBuilders
            				.rangeQuery(entry.getKey()).gte(entry.getValue().get(FIRST)).lte(entry.getValue().get(SECOND)));
            		break;
            }
        
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
	    public Map<String,QueryType> queryMap;
	    public Map<String,List<String>> filters;
	    public int limit;
	    public int offset;
	    public String index;

	    public SearchResultBuilder with(Consumer<SearchResultBuilder> builderFunction) {
	        builderFunction.accept(this);
	        return this;
	    }

	    public String getSearchResult() throws IOException {
			return getResultString(search(customQueryBuilder(filters, queryMap,baseBuilder(limit,offset)),index));
	    }
	}	
}
