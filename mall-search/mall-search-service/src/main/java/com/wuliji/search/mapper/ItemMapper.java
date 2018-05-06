package com.wuliji.search.mapper;

import java.util.List;

import com.wuliji.common.pojo.SearchItem;

public interface ItemMapper {
	
	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
