package com.wuliji.service;

import com.wuliji.common.pojo.EasyUIDataGridResult;
import com.wuliji.pojo.TbItem;

public interface ItemService {
	
	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page, int rows);
	
}
