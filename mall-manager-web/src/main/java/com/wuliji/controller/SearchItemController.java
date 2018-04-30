package com.wuliji.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.common.utils.MallResult;
import com.wuliji.search.service.SearchItemService;

/**
 * 导入商品到索引库
 * @author Administrator
 *
 */
@Controller
public class SearchItemController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public MallResult importItemList() {
		MallResult result = searchItemService.importAllItems();
		return result;
	}
}
