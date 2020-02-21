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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import com.example.demo.constants.QueryType;
import com.example.demo.constants.SortType;
import com.example.demo.helper.DateHelper;

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
	
	static SearchSourceBuilder customQueryBuilder(Map<String,List<String>> filters,Map<String,QueryType> queryMap,SearchSourceBuilder searchSourceBuilder) throws Throwable {
		if(filters==null)
			return searchSourceBuilder;
		
    	QueryBuilder queryBuilder=QueryBuilders.matchAllQuery();
        
    	for (Entry <String,List<String>> entry : filters.entrySet())
        	
    		switch(queryMap.get(entry.getKey())) {
            case TERM:
            	queryBuilder=QueryBuilders.termQuery(entry.getKey(), entry.getValue().get(FIRST));
            	break;
        	case PHRASE:
        		queryBuilder=QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue().get(FIRST));
        		break;
        	case AVAILABILITY_RANGE:
        		List<String> range=DateHelper.fromAvailability(entry.getValue().get(FIRST), 15);
        		queryBuilder=QueryBuilders.rangeQuery(entry.getKey()).gte(range.get(FIRST)).lte(range.get(SECOND));
        		break;
            }
    	String script="doc['rating'].value>=4? doc['relevance'].value+1 : doc['relevance'].value";
		searchSourceBuilder=searchSourceBuilder.query(QueryBuilders.functionScoreQuery(queryBuilder,ScoreFunctionBuilders.scriptFunction(script)));

        return searchSourceBuilder;
	}
		
	static SearchSourceBuilder customSortBuilder(String sortOrder,String sortField,Map<String,SortType> sortMap,Map<String,SortOrder> sortOrderMap,SearchSourceBuilder searchSourceBuilder) {
		if(sortOrder==null || sortField==null)
			return searchSourceBuilder;
		
		switch(sortMap.get(sortField)) {
        case FIELD:
        	searchSourceBuilder=searchSourceBuilder.sort(SortBuilders.fieldSort(sortField).order(sortOrderMap.get(sortOrder)));
        	break;
		case GEO:
			break;
		case SCORE:
			break;
		default:
			break;
        }
        
        return searchSourceBuilder;
	}
		
	static SearchSourceBuilder baseBuilder(int limit,int offset) {
		return new SearchSourceBuilder()
					.from(offset)
					.size(limit);
	}

	public static class SearchResultBuilder {
	    public Map<String,QueryType> queryMap;
	    public Map<String,SortType> sortMap;
	    public Map<String,SortOrder> sortOrderMap;
	    public String sortOrder;
	    public String sortField;
	    public Map<String,List<String>> filters;
	    public int limit;
	    public int offset;
	    public String index;

	    public SearchResultBuilder with(Consumer<SearchResultBuilder> builderFunction) {
	        builderFunction.accept(this);
	        return this;
	    }

	    public SearchResponse getSearchResult() throws Throwable {
	    	SearchSourceBuilder searchSourceBuilder=baseBuilder(limit,offset);
	    	searchSourceBuilder=customQueryBuilder(filters, queryMap, searchSourceBuilder);
	    	searchSourceBuilder=customSortBuilder(sortOrder, sortField, sortMap, sortOrderMap, searchSourceBuilder);
	    	return search(searchSourceBuilder,index);
	    }
	}	
}
