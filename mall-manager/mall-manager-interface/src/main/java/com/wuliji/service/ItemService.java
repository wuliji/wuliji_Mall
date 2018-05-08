package com.wuliji.service;

import com.wuliji.common.pojo.EasyUIDataGridResult;
import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbItem;
import com.wuliji.pojo.TbItemDesc;

public interface ItemService {
	
	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page, int rows);
	MallResult addItem(TbItem item, String desc);
	MallResult deleteItem(String[] ids);
	MallResult editItem(Long id);
	MallResult editItemDesc(Long id);
	MallResult updateItem(TbItem item, String desc);
	MallResult instock(String[] ids);
	MallResult reshelf(String[] ids);
	TbItemDesc getItemDescById(long itemId);
}
