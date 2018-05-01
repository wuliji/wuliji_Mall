package com.wuliji.search.service;

import com.wuliji.common.pojo.SearchResult;

public interface SearchService {
	
	SearchResult search(String keyword, int page, int rows) throws Exception;
}
